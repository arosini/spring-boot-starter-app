package ar.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import ar.entity.User;

/**
 * Manipulates {@link User} entities in the database.
 * 
 * @author adam
 *
 */
public interface UserRepository extends MongoRepository<User, String> {

  public List<User> findByLastName(@Param("lastName") String lastName);

}
