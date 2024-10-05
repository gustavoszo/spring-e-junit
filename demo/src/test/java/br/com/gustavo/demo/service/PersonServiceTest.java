package br.com.gustavo.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gustavo.demo.entity.Person;
import br.com.gustavo.demo.exception.EmailUniqueValidationException;
import br.com.gustavo.demo.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    // Cria a instancia e injeta os mocks necessários
    @InjectMocks
    private PersonService service;

    private Person person;

    @BeforeEach
    public void setup() {
        this.person = new Person("Gustavo", "Souza", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
    }
        
    @Test
    public void testSavePerson_whenSavePerson_thenReturnPersonObject() {
        // Given
        when(repository.save(person)).thenReturn(person);

        // When
        Person savedPerson = service.save(person);

        // Then
        assertNotNull(savedPerson);
        assertEquals("Gustavo", savedPerson.getFirstName());
    }
        
    @Test
    public void testGivenExistsEmail_whenSavePerson_thenReturnException() {
        // Given
        Person person2 = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
        String message = "O e-amil gsouza@gmail.com já está cadastrado";

        when(repository.save(person)).thenReturn(person);
        when(repository.save(person2)).thenThrow(new EmailUniqueValidationException(message));
        service.save(person);
        
        // When
        EmailUniqueValidationException exception = assertThrows(EmailUniqueValidationException.class, () -> {
            service.save(person2);
        });

        // Then
        assertEquals(message, exception.getMessage());
    }
        
    @Test
    public void testGivenPersonList_whenFindAllPerson_thenReturnPersonList() {
        // Given
        Person person2 = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
        when(repository.findAll()).thenReturn(List.of(person, person2));
        
        // When
        List<Person> personList = service.findAll();
        
        // Then
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }
        
    @Test
    public void testGivenPersonById_whenFindById_thenReturnPerson() {
        // Given
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        
        // When
        Person person = service.findById(1L);
        
        // Then
        assertNotNull(person);
    }
        
    @Test
    public void testGivenUpdatedPerson_whenUpdate_thenReturnPerson() {
        // Given
        Person personWithUpdate = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(person);

        // When
        Person person = service.update(1L, personWithUpdate);
        
        // Then
        assertNotNull(person);
        assertEquals("Oliveira", person.getLastName());
    }
        
    @Test
    public void testGivenPersonId_whenDeletePersonById_thenDoNothing() {
        // Given
        person.setId(1L);
        willDoNothing().given(repository).deleteById(person.getId());

        // When
        service.deleteById(1L);
        
        // Then
        verify(repository, times(1)).deleteById(1L);
    }
    
}
    