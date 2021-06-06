package org.acme.rest.json.service;

import java.time.LocalDate;
import java.time.Month;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.acme.rest.json.entities.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Transactional
public class ServiceStudentTest {

        // Execute these TESTS: ./mvnw -Dtest=ServiceStudentTest test

        @Inject
        ServiceStudent service;
    
        // @Test de jupiter, no el de junit
        @Test
        public void testAllStudent() {
            Assertions.assertThat(service.setStudents()).hasSize(2);
        }
    
        @Test
        public void containsTest() {
            Assertions.assertThat(service.setStudents().stream().anyMatch(s -> s.getName().equalsIgnoreCase("Mateo"))).isTrue();
            Assertions.assertThat(service.setStudents().stream().anyMatch(s -> s.getName().equalsIgnoreCase("Will"))).isTrue();
        }
    
        @Test
        public void addStudentTest() {
            service.add(new Student("Pedro", "Gimenez", LocalDate.of(1990, Month.DECEMBER, 17), "+34 687687878"));
    
            System.out.println(service.setStudents());
    
            Assertions.assertThat(service.setStudents()).hasSize(3);

            Assertions.assertThat(service.setStudents().stream().anyMatch(f -> f.getName().equalsIgnoreCase("Pedro"))).isTrue();

            Assertions.assertThat(service.setStudents().stream().anyMatch(f -> f.getSurname().equalsIgnoreCase("Gimenez"))).isTrue();

            Assertions.assertThat(service.setStudents().stream().anyMatch(f -> f.getDateBirth().equals(LocalDate.parse("1990-12-17")))).isTrue();
            Assertions.assertThat(service.setStudents().stream().anyMatch(f -> f.getPhone().equalsIgnoreCase("+34 687687878"))).isTrue();
    
            // handmade rollback debido antipatron ActiveRecord
            Student fruit = Student.find("name", "Pedro").firstResult();
            fruit.delete();
            Assertions.assertThat(Student.count()).isEqualTo(2);
        }
        @Test
        public void removeTest(){
            service.remove("Will");
            Assertions.assertThat(service.setStudents()).hasSize(1);
            Assertions.assertThat(service.setStudents().stream().anyMatch(f -> f.getName().equalsIgnoreCase("Will"))).isFalse();
    
            // handmade rollback debido al antipatron ActiveRecord 
            Student.persist(new Student("Will", "Smith", LocalDate.of(1999, Month.JUNE, 17), "+34 677878997"));
            Assertions.assertThat(Student.count()).isEqualTo(2);
        }
    
        @Test
        public void getStudentTest() {
            Assertions.assertThat(service.getStudentByName("Mateo")).get().hasFieldOrPropertyWithValue("name", "Mateo").hasFieldOrPropertyWithValue("dateBirth", LocalDate.parse("2005-06-05")).hasFieldOrPropertyWithValue("phone", "+34 666666666");

            // Test Student doesn't exist
            Assertions.assertThat(service.getStudentByName("Julio")).isEmpty();
        }

        @Test
        public void udpateStudentTest() {
            

            // Update Will Student with new Data
            service.updateStudent(new Student("Will", "James", LocalDate.of(1987, Month.SEPTEMBER, 06), "+34 687685598"));

            // Check if the total of student is still 2
            Assertions.assertThat(Student.count()).isEqualTo(2);

            // Check if the student was updated
            Assertions.assertThat(service.getStudentByName("Will")).get().hasFieldOrPropertyWithValue("name", "Will").hasFieldOrPropertyWithValue("dateBirth", LocalDate.parse("1987-09-06")).hasFieldOrPropertyWithValue("phone", "+34 687685598");




        }
    
}
