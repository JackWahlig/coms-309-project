package org.springframework.samples.petclinic.user;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Owners</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Zane Seuser
 */
@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

    Collection<Users> findAll();

    Collection<Users> findById(@Param("id") int id);

    Collection<Users> findByLastNameContains(@Param("lastName") String lastName);

    Users findDistinctById(@Param("id") int id);

    @Override
    void delete(Users users);

    Users save(Users user);
}
