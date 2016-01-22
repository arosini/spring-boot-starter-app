package ar.repository;

import ar.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Manipulates {@link User} resources in the database.
 * 
 * @author adam
 *
 */
public interface UserRepository extends MongoRepository<User, String> {

  public List<User> findByLastName(@Param("lastName") String lastName);

}
