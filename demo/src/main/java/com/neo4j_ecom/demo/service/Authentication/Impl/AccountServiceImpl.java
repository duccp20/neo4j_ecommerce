package com.neo4j_ecom.demo.service.Authentication.Impl;


import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.Auth.Account;
import com.neo4j_ecom.demo.model.entity.Customer;
import com.neo4j_ecom.demo.repository.AuthRepository.AccountRepository;
import com.neo4j_ecom.demo.service.Authentication.AccountService;
import com.neo4j_ecom.demo.service.impl.CustomerServiceImpl;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerServiceImpl customerService;


    @Override
    public Account save(Account account){
        try {
            return accountRepository.save(account);
        }catch (AppException e){
            throw e;
        }
    }
    @Override
    public Account registration(Account account) {
        try{
            if (accountRepository.existsByEmail(account.getEmail())) {
                throw new AccountException("User already exists");
            }

            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()) {
                throw new AccountException("Username or password cannot be empty");
            }
            else {

                account.setEmail(account.getEmail().trim());
                PasswordEncoder passwordEndcoder = new BCryptPasswordEncoder(10);
                account.setPassword(passwordEndcoder.encode(account.getPassword()).trim());

                Customer customer = new Customer(account);
                customerService.saveCustomer(customer);
                account.setCustomer(customer);

                return accountRepository.save(account);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Account> updateAccount(Account account) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> deleteAccount(Account account) {
        return Optional.empty();
    }


    @Override
    public Optional<Account> getAccountById(String id) {
        try{
            if(id ==null){
                throw new RuntimeException("Id cannot be empty");
            }
            return accountRepository.findById(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Optional<Account> findAccountByEmail(String email){
        try {
            return accountRepository.findByEmail(email);
        }catch (Exception e){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }


    @Override
    public List<Account> getAllAccounts() {
        try{
            return accountRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean existsEmail(String email) {
        return accountRepository.existsByEmail(email);
    }


    @Override
    public void updateForgotPasswordToken(String token, String id) {
        Optional<Account> account = accountRepository.findById(id);
        account.ifPresent(u -> {
            u.setForgotPasswordToken(token);
            accountRepository.save(u);
        });
    }


    @Override
    public void updateVerificationToken(String token, String id){
        Optional<Account> user = accountRepository.findById(id);
        user.ifPresent(u -> {
            u.setVerificationToken(token);
            accountRepository.save(u);
        });
    }

}
