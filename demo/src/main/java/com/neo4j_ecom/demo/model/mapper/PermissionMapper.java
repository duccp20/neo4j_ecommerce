package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.PermissionRequest;
import com.neo4j_ecom.demo.model.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

import java.security.Permission;


@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
