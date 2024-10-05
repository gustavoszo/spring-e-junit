package br.com.gustavo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gustavo.demo.entity.Person;
import br.com.gustavo.demo.exception.EmailUniqueValidationException;
import br.com.gustavo.demo.exception.EntityNotFoundException;
import br.com.gustavo.demo.repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;
    
    @Transactional(readOnly=true)
    public List<Person> findAll() {
        return repository.findAll();
    }
    
    @Transactional(readOnly=true)
    public Person findById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("Person com id %d não encontrado", id));
        });
    }

    @Transactional
    public Person save(Person person) {
        try {
            return repository.save(person);
        } catch (DataIntegrityViolationException e) {
            throw new EmailUniqueValidationException(String.format("O e-mail %s já está cadastrado", person.getEmail()));
        }
    }

    @Transactional
    public Person update(Long id, Person person) {
        Person entity = findById(id);
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setEmail(person.getEmail());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new EmailUniqueValidationException(String.format("O e-mail %s já está cadastrado", person.getEmail()));
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Person person = findById(id);
        repository.delete(person);
    }

}
