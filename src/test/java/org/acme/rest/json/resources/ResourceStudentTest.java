package org.acme.rest.json.resources;


import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import net.bytebuddy.asm.Advice.Local;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
// importado a mano equalTo
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

// @QuarkusTestResource para indicarle que use el TestContainers, pero al parecer Testcontainer ya lo aplica por defecto sin necesidad de llamarlo explicitamente

@Transactional
@QuarkusTest
public class ResourceStudentTest {

    // Execute these TESTS: ./mvnw -Dtest=ResourceStudentTest test


    @Test
    public void testListStudentsEndpoint() {
        // Si el content-type de la peticion es JSON 
        // responde el endpoint list
        // list() endpoint devuelve lista de maps [{}, {}]
        // new TypeRef<List<Map<String, Object>>>() {} Tiene Object porque los valores de las claves pueden ser obviamente cualquier posible tipo de Dato
        List<Map<String, Object>> products = 
            given()
                .contentType(ContentType.JSON)
                .when()
                .get("/students/all")
                .as(new TypeRef<List<Map<String, Object>>>() {});

        Assertions.assertThat(products).hasSize(2);
        // Test if a JSON to Map have the correct Keys
        // The JSON KEYS must be the same as the names of Properties of Entity
        Assertions.assertThat(products.get(0)).containsKeys("name", "surname", "dateBirth", "phone");
    }

    @Test
    public void testList() {

        // When we do a request, DATE parameter is still a String type in ResourceStudents, so when we test in dateBirth containsInAnyOrder() still doesn't convert into LocalDate Type, they're still String type
        // For this reason, i don't use LocalDate.parse() yet, Because some methods like GET, return JSON, and JSON obviously doens't have LocalDate Type. Jackson transform LocalDate into String thanks to @JsonFormat in the definition of property dateBirth

        given()
            .contentType(ContentType.JSON)
        .when().get("/students/all")
        .then()
            .statusCode(200)
            .body("$.size()", is(2),
            "name", containsInAnyOrder("Mateo", "Will"),
            "surname", containsInAnyOrder("Alvarez", "Smith"),
            "dateBirth", containsInAnyOrder("2005-06-05", "1999-06-17"),
            "phone", containsInAnyOrder("+34 666666666", "+34 677878997"));
    }

    @Test
    public void testAddDelete() {

        given()
            .body("{\"name\": \"Pedro\", \"surname\": \"Gimenez\", \"dateBirth\": \"1990-12-17\", \"phone\": \"+34 687687878\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/students/add")
        .then()
            .statusCode(200)
            .body("$.size()", is(3),
            "name", containsInAnyOrder("Mateo", "Will", "Pedro"),
            "surname", containsInAnyOrder("Alvarez", "Smith", "Gimenez"),
            "dateBirth", containsInAnyOrder("2005-06-05", "1999-06-17", "1990-12-17"),
            "phone", containsInAnyOrder("+34 666666666", "+34 677878997", "+34 687687878"));
        
        // Rollback Handle because @Transaction doesn't work
        given()
            .body("{\"name\": \"Pedro\", \"surname\": \"Gimenez\", \"dateBirth\": \"1990-12-17\", \"phone\": \"+34 687687878\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .delete("/students/delete")
        .then()
            .statusCode(200)
            .body("$.size()", is(2),
            "name", containsInAnyOrder("Mateo", "Will"),
            "surname", containsInAnyOrder("Alvarez", "Smith"),
            "dateBirth", containsInAnyOrder("2005-06-05", "1999-06-17"),
            "phone", containsInAnyOrder("+34 666666666", "+34 677878997"));
    }
    
    @Test
    public void getPathParamTest() {

        // equalTo() para valores unicos
        // containsInAnyOrder para lista de valores

        given()
            .pathParam("name", "Mateo")
        .when()
            .get("/students/{name}")
        .then()
            .contentType(ContentType.JSON)
            .body("name", equalTo("Mateo"),
            "surname", equalTo("Alvarez"),
            "dateBirth", equalTo("2005-06-05"),
            "phone", equalTo("+34 666666666"));

        

        // no Student
        given()
            .pathParam("name", "Julian")
        .when()
            .get("/students/{name}")
        .then()
            .statusCode(404);
    }

    @Test
    public void updateStudentTest() {

        // equalTo() para valores unicos
        // containsInAnyOrder para lista de valores

        given()
            .body("{\"name\": \"Mateo\", \"surname\": \"Gomez\", \"dateBirth\": \"2000-10-17\", \"phone\": \"+34 688888888\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .put("/students/put")
        .then()
            .contentType(ContentType.JSON)
            .body("name", equalTo("Mateo"),
            "surname", equalTo("Gomez"),
            "dateBirth", equalTo("2000-10-17"),
            "phone", equalTo("+34 688888888"));

        // ROLLBACK to put the original Student
        given()
            .body("{\"name\": \"Mateo\", \"surname\": \"Alvarez\", \"dateBirth\": \"2005-06-05\", \"phone\": \"+34 666666666\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .put("/students/put")
        .then()
            .contentType(ContentType.JSON)
            .body("name", equalTo("Mateo"),
            "surname", equalTo("Alvarez"),
            "dateBirth", equalTo("2005-06-05"),
            "phone", equalTo("+34 666666666"));

    }

}
