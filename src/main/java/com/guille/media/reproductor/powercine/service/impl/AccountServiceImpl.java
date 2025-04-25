package com.guille.media.reproductor.powercine.service.impl;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.Account;
import com.guille.media.reproductor.powercine.repository.Accountrepository;
import com.guille.media.reproductor.powercine.service.IAccountService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    private Accountrepository accountrepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(Accountrepository accountrepository, PasswordEncoder passwordEncoder) {
        this.accountrepository = accountrepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Account> getAccountByUsername(String username) {
        return this.accountrepository.findByUsername(username);
    }

    @Override
    public Optional<Account> getAccountByEmail(String email) {
        return this.accountrepository.findByEmail(email);
    }

    @Override
    public Optional<Account> getAccountById(Integer id) {
        return this.accountrepository.findById(id);
    }

}
