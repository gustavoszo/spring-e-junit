package br.com.gustavo.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Person implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 80, message = "{Size.person.firstName}")
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    
    @Size(min = 2, max = 80, message = "{Size.person.lastName}")
    @NotBlank
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    
    @Size(min = 10, max = 100)
    @NotBlank
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String address;
    
    @NotBlank
    @Size(min = 4, max = 10)
    @Column(nullable = false, length = 10)
    private String gender;

    public Person(String firstName, String lastName, String email, String address, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.gender = gender;
    }

}
