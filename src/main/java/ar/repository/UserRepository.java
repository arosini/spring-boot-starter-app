package ar.repository;

import ar.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

  List<User> findByLastName(@Param("lastName") String lastName);

}
