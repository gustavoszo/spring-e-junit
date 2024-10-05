package br.com.gustavo.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.gustavo.demo.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    @Query("select p from Person p where p.firstName =:firstName and p.lastName =:lastName")
    Optional<Person> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Consulta com SQL
    // @Query(value = "select * from person p where first_name =:firstName and last_name =:lastName", nativeQuery = true)
    // Optional<Person> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
}
