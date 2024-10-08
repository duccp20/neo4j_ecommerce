package com.neo4j_ecom.demo.config;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.entity.User;
import com.neo4j_ecom.demo.repository.UserRepository;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceCustomer implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(username);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        };

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().toUpperCase())).collect(Collectors.toList()))
                .build();

    }
}
