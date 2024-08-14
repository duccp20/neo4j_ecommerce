package com.neo4j_ecom.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "child_sub_categories")
public class ChildSubCategory {

    @Id
    @GeneratedValue
    private int id;
    private String name;
}
