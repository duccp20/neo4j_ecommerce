package com.neo4j_ecom.demo.model.mapper;

import com.neo4j_ecom.demo.model.dto.request.UserRequest;
import com.neo4j_ecom.demo.model.dto.response.UserResponse;
import com.neo4j_ecom.demo.model.Auth.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {
    Account toUser(UserRequest userRequest);
    UserResponse toUserResponse(Account account);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget Account account, UserRequest request);

}
