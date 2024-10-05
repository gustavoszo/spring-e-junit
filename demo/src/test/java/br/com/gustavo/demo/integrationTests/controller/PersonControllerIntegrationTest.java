package br.com.gustavo.demo.integrationTests.controller;

import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gustavo.demo.config.TestConfigs;
import br.com.gustavo.demo.entity.Person;
import br.com.gustavo.demo.integrationTests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

// Esta anotação é usada para especificar a ordem em que os métodos de teste serão executados. O uso da OrderAnnotation permite que você defina a ordem com a anotação @Test em cada método.
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        // O método configura o ObjectMapper para que não falhe ao encontrar propriedades desconhecidas durante a deserialização. Isso é útil ao lidar com dados JSON que podem ter campos adicionais que não estão presentes na classe Person.
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
            .setBasePath("/")
            .setPort(TestConfigs.SERVER_PORT)
                // Adiciona filtros para registrar detalhes das requisições e respostas
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        person = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
    }

    @DisplayName("Integration Test Given Person When Create A Person Should Returns A Person With Status 201")
    @Test
    @Order(1)
    public void integrationTestGivenPerson_WhenCreateAPerson_ShouldReturnsAPersonWithStatus201() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(201)
                    .extract()
                        .body()
                            .asString();

        Person createdPerson = mapper.readValue(content, Person.class);
        person = createdPerson;

        assertNotNull(createdPerson);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getEmail());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Gustavo", createdPerson.getFirstName());
        assertEquals("Oliveira", createdPerson.getLastName());
    }

    @DisplayName("Integration Test Given Person When Update A Person Should Returns A Updated Person With Status 200")
    @Test
    @Order(2)
    public void integrationTestGivenPerson_WhenUpdateAPerson_ShouldReturnsAUpdatedPersonWithStatus200() throws JsonProcessingException {
        person.setFirstName("João");
        person.setLastName("Souza");
        person.setEmail("joão@gmail.com");

        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
            .when()
                .put("{id}")
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person updatedPerson = mapper.readValue(content, Person.class);
        person = updatedPerson;

        assertNotNull(updatedPerson);

        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getEmail());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("João", updatedPerson.getFirstName());
        assertEquals("Souza", updatedPerson.getLastName());
        assertEquals("joao@gmail.com", updatedPerson.getEmail());
    }

    @DisplayName("Integration Test Given Person When FindById A Person Should Returns A Found Person With Status 200")
    @Test
    @Order(3)
    public void integrationTestGivenPerson_WhenFindByIdAPerson_ShouldReturnsAFoundPersonWithStatus200() throws JsonProcessingException {

        var content = given().spec(specification)
                .pathParam("id", person.getId())
            .when()
                .get("{id}")
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person foundPerson = mapper.readValue(content, Person.class);

        assertNotNull(foundPerson);

        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getEmail());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("João", foundPerson.getFirstName());
        assertEquals("Souza", foundPerson.getLastName());
        assertEquals("joao@gmail.com", foundPerson.getEmail());
    }

    @DisplayName("Integration Test Given Person List When FindAll Should Returns A Person List With Status 200")
    @Test
    @Order(4)
    public void integrationTestFindAll() throws JsonProcessingException {

        Person anotherPerson = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(anotherPerson)
            .when()
                .post()
            .then()
                .statusCode(201);
                

        var content = given().spec(specification)
            .when()
                .get()
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person people[] = mapper.readValue(content, Person[].class);
        List<Person> personList = Arrays.asList(people);

        Person foundPersonOne = personList.get(0);
        assertNotNull(foundPersonOne);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getEmail());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertTrue(foundPersonOne.getId() > 0);
        assertEquals("João", foundPersonOne.getFirstName());
        assertEquals("Souza", foundPersonOne.getLastName());
        assertEquals("joao@gmail.com", foundPersonOne.getEmail());

        Person foundPersonTwo = personList.get(1);
        assertNotNull(foundPersonTwo);

        assertNotNull(foundPersonTwo.getId());
        assertNotNull(foundPersonTwo.getFirstName());
        assertNotNull(foundPersonTwo.getLastName());
        assertNotNull(foundPersonTwo.getEmail());
        assertNotNull(foundPersonTwo.getAddress());
        assertNotNull(foundPersonTwo.getGender());

        assertTrue(foundPersonTwo.getId() > 0);
        assertEquals("Gustavo", foundPersonTwo.getFirstName());
        assertEquals("Oliveira", foundPersonTwo.getLastName());
        assertEquals("gsouza@gmail.com", foundPersonTwo.getEmail());
    }

    
    @DisplayName("Integration Test When DeleteById A Person Should Returns No Content With Status 204")
    @Test
    @Order(5)
    public void integrationTestDeleteById() {

        given().spec(specification)
                .pathParam("id", person.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);

    }

}
