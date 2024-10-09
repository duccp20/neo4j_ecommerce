package com.neo4j_ecom.demo.service;


import com.neo4j_ecom.demo.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getUserById(String id);

    List<User> getAllUser();

    User saveUser(User user);

    User updateUser(String id, User user);

    void deleteUser(String id);

    Optional<User> getUserByEmail(String email);

    void updateForgotPasswordToken(String token, String id);

    Optional<User> findById(String id);

    void updatePassword(String newPass,  User user);

    void updateVerificationToken(String token, String id);

}
