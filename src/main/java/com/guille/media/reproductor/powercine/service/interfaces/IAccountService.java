package com.guille.media.reproductor.powercine.service.interfaces;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.AccountJpaEntity;

public interface IAccountService {
    AccountJpaEntity save(AccountJpaEntity accountJpaEntity);

    Optional<AccountJpaEntity> getAccountByUsername(String username);

    Optional<AccountJpaEntity> getAccountByEmail(String email);

    Optional<AccountJpaEntity> getAccountById(Integer id);
}
