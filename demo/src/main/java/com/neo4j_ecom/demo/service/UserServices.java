package com.neo4j_ecom.demo.service;


import com.neo4j_ecom.demo.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserServices {
    public User getUserById(String id);
    public List<User> getAllUser();
    public User saveUser(User user);
    public User updateUser(String id, User user);
    public void deleteUser(String id);
    public Optional<User> getUserByEmail(String email);

}
