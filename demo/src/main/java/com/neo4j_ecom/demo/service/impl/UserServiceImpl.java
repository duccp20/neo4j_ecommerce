package com.neo4j_ecom.demo.service.impl;


import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.UserRepository;
import com.neo4j_ecom.demo.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServices {


    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return user;
    }

    @Override
    public List<User> getAllUser() {
        try {
            return userRepository.findAll();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        try {
            User oldUser = getUserById(id);
            if (user.getFullName()!=null){
                oldUser.setFullName(user.getFullName());
            }
            if (user.getAddress()!=null){
                oldUser.setAddress(user.getAddress());
            }
            if (user.getPhone()!=null){
                oldUser.setPhone(user.getPhone());
            }
            if (user.getEmail()!=null){
                oldUser.setEmail(user.getEmail());
            }
            return userRepository.save(oldUser);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
