package com.neo4j_ecom.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sub_categories")
public class SubCategory {

    @Id
    @GeneratedValue
    private int id;
    private String name;
}
