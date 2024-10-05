package br.com.gustavo.demo.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gustavo.demo.entity.Person;
import br.com.gustavo.demo.service.PersonService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class PersonController {
    
    @Autowired
    private PersonService service;

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        List<Person> people  = service.findAll();
        return ResponseEntity.ok(people);
    }

    @GetMapping("{id}")
    public ResponseEntity<Person> findById(@PathVariable("id") Long id) {
        Person person = service.findById(id);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        Person entity = service.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @PutMapping("{id}")
    public ResponseEntity<Person> update(@Valid @RequestBody Person person, @PathVariable("id") Long id) {
        Person entity = service.update(id, person);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
