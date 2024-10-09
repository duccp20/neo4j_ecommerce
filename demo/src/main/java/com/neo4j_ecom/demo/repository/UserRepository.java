package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);

    User findUserByEmail(String email);

    Optional<User> findByEmail(String email);



}
