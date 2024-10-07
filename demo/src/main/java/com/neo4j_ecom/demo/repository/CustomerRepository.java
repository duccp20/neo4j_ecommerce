package com.neo4j_ecom.demo.repository;

import com.neo4j_ecom.demo.model.entity.Customer;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Aggregation(pipeline = {
            "{$match:  {_id:  ?0}}"
    })
    public Optional<Customer> findById(String id);

    @Aggregation(pipeline = {
            "{$match: {}}"
    })
    public List<Customer> findAll();


}
