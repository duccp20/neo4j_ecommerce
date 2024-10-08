package com.neo4j_ecom.demo.repository.AuthRepository;


import com.neo4j_ecom.demo.model.Auth.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByEmail(String email);

    public boolean existsByEmail(String email);

}
