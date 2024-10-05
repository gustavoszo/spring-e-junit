package br.com.gustavo.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.gustavo.demo.entity.Person;

// O Spring Boot, por padrão, usa o H2 como um banco de dados em memória se não encontrar outra configuração de banco de dados. Se você não tiver uma configuração explícita para outro banco de dados (como o MySQL), ele pode usar o H2 por padrão (No meu caso, não esta configurado. As configurações do H2 estão comentadas).
// O @DataJpaTest configura um banco de dados H2 em memória (ou outro banco configurado) que é criado antes do teste e descartado após a execução do teste.
// Por padrão, a transação é revertida após cada teste. Isso é útil para garantir que os dados persistidos durante o teste não sejam visíveis em testes subsequentes.
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository repository;

    private Person person;

    @BeforeEach
    private void setup() {
        // Given
        this.person = new Person("Gustavo", "Souza", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
    }
        
    @DisplayName("Test Given Person When Save Then Return Saved Person")
    @Test
    public void testGivenPerson_whenSave_thenReturnSavedPerson() {
        Person savedPerson = repository.save(person);

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }
        
    @DisplayName("Test Given People When Find All Then Return Person List")
    @Test
    public void testGivenPeople_whenFindAll_thenReturnPersonList() {
        // Given
        Person person2 = new Person("Duda", "Santos", "duda@gmail.com", "São Paulo - Brasil", "Female");
        repository.save(person);
        repository.save(person2);

        // When
        List<Person> people = repository.findAll();

        // Then
        assertNotNull(people);
        assertEquals(2, people.size());
    }
  
    @Test
    public void testGivenPerson_whenFindById_thenReturnPerson() {
        // Given
        Person person = new Person("Gustavo", "Souza", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
        repository.save(person);

        // When
        Person savedPerson = repository.findById(person.getId()).get();

        // Then
        assertNotNull(savedPerson);
        assertEquals(savedPerson.getId(), person.getId());
    }
  
    @Test
    public void testGivenPerson_whenFindByEmail_thenReturnPerson() {
        // Given
        repository.save(person);

        // When
        Person savedPerson = repository.findByEmail(person.getEmail()).get();

        // Then
        assertNotNull(savedPerson);
        assertEquals(savedPerson.getId(), person.getId());
    }
  
    @Test
    public void testGivenPerson_whenUpdate_thenReturnUpdatedPerson() {
        // Given
        repository.save(person);
        Person savedPerson = repository.findById(person.getId()).get();

        savedPerson.setLastName("Oliveira");
        
        // When
        Person updatedPerson = repository.save(savedPerson);

        // Then
        assertNotNull(updatedPerson);
        assertEquals(savedPerson.getLastName(), updatedPerson.getLastName());
    }
    
    @Test
    public void testDeletePerson_whenDeleteById_thenRemovePerson() {
        // Given
        repository.save(person);
        
        // When
        repository.deleteById(person.getId());

        // Then
        Optional<Person> optionalPerson = repository.findById(person.getId());
        assertTrue(optionalPerson.isEmpty());
    }
    
    @DisplayName("Test Given firstName And lastName When FindBy FirstName And LastName Then Return Person")
    @Test
    public void testGivenFirstNameAndLastName_whenFindByFirstNameAndLastName_thenReturnPerson() {
        // Given
        repository.save(person);

        String firstName = "Gustavo";
        String lastName = "Souza";
        
        // When
        Person savedPerson = repository.findByFirstNameAndLastName(firstName, lastName).get();

        // Then
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }
    
}