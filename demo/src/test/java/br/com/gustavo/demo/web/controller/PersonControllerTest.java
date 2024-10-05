package br.com.gustavo.demo.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gustavo.demo.entity.Person;
import br.com.gustavo.demo.exception.EntityNotFoundException;
import br.com.gustavo.demo.service.PersonService;

@WebMvcTest
public class PersonControllerTest {

    // MockMvc é uma classe fornecida pelo Spring para simular chamadas HTTP como GET, POST, etc., sem iniciar um servidor real. 
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper é uma classe do Jackson usada para serializar/deserializar objetos Java para JSON e vice-versa.
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonService service;

    private Person person;
    
    @BeforeEach
    public void setup(){
        this.person = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - Brasil", "Male");
    }
        
    @Test
    public void testGivenCreatePerson_whenSavePerson_thenReturnSavedPersonWithStatus201() throws Exception {
        // Given
        // thenAnswer((invocation) -> invocation.getArgument(0)): Aqui o mock devolve o mesmo objeto Person que foi recebido, simulando o comportamento esperado do serviço.
        when(service.save(any(Person.class))).thenAnswer((invocation) -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(person))
        );

        // Then
        response.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName", is(person.getFirstName())));
    }
        
    @Test
    public void testGivenPeopleList_whenFindAll_thenReturnPeopleListWithStatus200() throws Exception {
        // Given
        List<Person> people = new ArrayList<Person>();
        people.add(person);
        people.add(new Person("Duda", "Santos", "duda@gmail.com", "São Paulo - Brasil", "Female"));
        when(service.findAll()).thenReturn(people);

        // When
        ResultActions response = mockMvc.perform(get("/"));

        // Then
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(people.size())));
    }
        
    @Test
    public void testGivenPerson_whenFindById_thenReturnPersonWithStatus200() throws Exception {
        // Given
        Long personId = 1L;
        when(service.findById(personId)).thenReturn(person);

        // When
        ResultActions response = mockMvc.perform(get("/{id}", personId));

        // Then
        response
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
            .andExpect(jsonPath("$.lastName", is(person.getLastName())))
            .andExpect(jsonPath("$.email", is(person.getEmail())));
    }
        
    @Test
    public void testGivenNotExistsPerson_whenFindById_thenReturnErrorMessageWithStatus404() throws Exception {
        // Given
        Long personId = 2L;
        when(service.findById(personId)).thenThrow(new EntityNotFoundException(String.format("Person com id %d não encontrado", personId)));

        // When
        ResultActions response = mockMvc.perform(get("/{id}", personId));

        // Then
        response
            .andExpect(status().isNotFound());
            // .andExpect(jsonPath("$.message", is(String.format("Person com id %d não encontrado", personId))));
    }
        
    @Test
    public void testGivenUpdatedPerson_whenUpdate_thenReturnUpdatedPersonWithStatus200() throws Exception {
        // Given
        Person updatedPerson = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - SP", "Male");
        when(service.update(1L, updatedPerson)).thenAnswer((invocation) -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform(put("/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updatedPerson))
        );

        // Then
        response
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
            .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }
        
    @Test
    public void testGivenNotExistsPerson_whenUpdate_thenReturnExceptionWithStatus404() throws Exception {
        // Given
        Person updatedPerson = new Person("Gustavo", "Oliveira", "gsouza@gmail.com", "São Paulo - SP", "Male");
        when(service.findById(1L)).thenThrow(new EntityNotFoundException(String.format("Person com id %d não encontrado", 1L)));
        when(service.update(1L, updatedPerson)).thenAnswer((invocation) -> invocation.getArgument(1));

        // When
        ResultActions response = mockMvc.perform(put("/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updatedPerson))
        );

        // Then
        response
            .andExpect(status().isNotFound());
    }
        
    @Test
    public void testDeletePerson_whenDelete_thenReturnNoContentWithStatus204() throws Exception {
        // Given
        willDoNothing().given(service).deleteById(1L);

        // When
        ResultActions response = mockMvc.perform(delete("/{id}", 1L));

        // Then
        response
            .andExpect(status().isNoContent());
    }

}