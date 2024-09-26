package com.neo4j_ecom.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.security.Permission;


@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {}
