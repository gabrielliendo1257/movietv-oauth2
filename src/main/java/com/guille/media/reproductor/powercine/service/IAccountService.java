package com.guille.media.reproductor.powercine.service;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.Account;

public interface IAccountService {

    Optional<Account> getAccountByUsername(String username);

    Optional<Account> getAccountByEmail(String email);

    Optional<Account> getAccountById(Integer id);
}
