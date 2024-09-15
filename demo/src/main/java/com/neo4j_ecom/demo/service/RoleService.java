package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.entity.Role;
import com.neo4j_ecom.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }


}
