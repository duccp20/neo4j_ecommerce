package com.neo4j_ecom.demo.service.Authentication;


import com.neo4j_ecom.demo.model.Auth.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public Account save(Account account);
    public Account registration(Account account);
    void updateForgotPasswordToken(String token, String id);
    public Optional<Account> getAccountById(String id);
    public Optional<Account> findAccountByEmail(String email);
    public List<Account> getAllAccounts();

    public Optional<Account> updateAccount(Account account);
    public Optional<Account> deleteAccount(Account account);

    public boolean existsEmail(String email);

    public void updateVerificationToken(String token, String id);
}
