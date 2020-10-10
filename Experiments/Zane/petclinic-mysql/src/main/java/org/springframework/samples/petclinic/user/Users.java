package org.springframework.samples.petclinic.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * Simple JavaBean domain object representing a user.
 *
 * @author Zane Seuser
 */
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String first_name;

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public String getLastName() {
        return this.lastName;
    }

}
