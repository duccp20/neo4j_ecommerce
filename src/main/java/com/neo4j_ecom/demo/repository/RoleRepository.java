package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
