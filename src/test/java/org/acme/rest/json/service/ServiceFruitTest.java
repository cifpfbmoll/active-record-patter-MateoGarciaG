package org.acme.rest.json.service;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.acme.rest.json.PostgresqlDBContainer;
import org.acme.rest.json.entities.Fruit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Component Unit testing
 */

@QuarkusTest
@Transactional
public class ServiceFruitTest {

    // Execute these TESTS: ./mvnw -Dtest=ServiceFruitTest test

    @Inject
    ServiceFruit service;

    // @Test de jupiter, no el de junit
    @Test
    public void testList() {
        Assertions.assertThat(service.list()).hasSize(2);
    }

    @Test
    public void containsTest() {
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equalsIgnoreCase("Apple"))).isTrue();
    }

    @Test
    public void addTest() {
        service.add(new Fruit("Banana", "And an attached Gorilla"));

        System.out.println(service.list());

        Assertions.assertThat(service.list()).hasSize(3);
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equalsIgnoreCase("Banana"))).isTrue();

        // handmade rollback gracias al antipatron ActiveRecord ;)
        Fruit fruit = Fruit.find("name", "Banana").firstResult();
        fruit.delete();
        Assertions.assertThat(Fruit.count()).isEqualTo(2);
    }
    @Test
    public void removeTest(){
        service.remove("Apple");
        Assertions.assertThat(service.list()).hasSize(1);
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equalsIgnoreCase("Apple"))).isFalse();

        // handmade rollback gracias al antipatron ActiveRecord ;)
        Fruit.persist(new Fruit("Apple", "Winter fruit"));
        Assertions.assertThat(Fruit.count()).isEqualTo(2);
    }

    @Test
    public void getFruitTest() {
        Assertions.assertThat(service.getFruit("Apple")).get().hasFieldOrPropertyWithValue("name", "Apple").hasFieldOrPropertyWithValue("description", "Winter fruit");
        Assertions.assertThat(service.getFruit("Mandarina")).isEmpty();
    }
}
