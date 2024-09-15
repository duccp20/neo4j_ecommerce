package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.ERole;
import com.neo4j_ecom.demo.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
