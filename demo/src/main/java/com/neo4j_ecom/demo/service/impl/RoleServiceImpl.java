package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.model.dto.request.RoleRequest;
import com.neo4j_ecom.demo.model.dto.response.RoleResponse;
import com.neo4j_ecom.demo.model.entity.Role;
import com.neo4j_ecom.demo.model.mapper.RoleMapper;
import com.neo4j_ecom.demo.repository.PermissionRepository;
import com.neo4j_ecom.demo.repository.RoleRepository;
import com.neo4j_ecom.demo.service.RoleService;

import java.security.Permission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    @Override
    public RoleResponse create(RoleRequest request) {
       return null;
    }

    @Override
    public List<RoleResponse> getAll() {
        return null;
    }

    @Override
    public void delete(String role) {

    }
}
