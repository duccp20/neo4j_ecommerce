package com.neo4j_ecom.demo.service;

import com.neo4j_ecom.demo.model.dto.request.RoleRequest;
import com.neo4j_ecom.demo.model.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    public RoleResponse create(RoleRequest request);
    public List<RoleResponse> getAll();
    public void delete(String role);
}
