package com.guille.media.reproductor.powercine.service.impl;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.AccountJpaEntity;
import com.guille.media.reproductor.powercine.repository.Accountrepository;
import com.guille.media.reproductor.powercine.service.interfaces.IAccountService;

import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    private final Accountrepository accountrepository;

    public AccountServiceImpl(Accountrepository accountrepository) {
        this.accountrepository = accountrepository;
    }

    @Override
    public Optional<AccountJpaEntity> getAccountByUsername(String username) {
        return this.accountrepository.findByUsername(username);
    }

    @Override
    public Optional<AccountJpaEntity> getAccountByEmail(String email) {
        return this.accountrepository.findByEmail(email);
    }

    @Override
    public Optional<AccountJpaEntity> getAccountById(Integer id) {
        return this.accountrepository.findById(id);
    }

    @Override
    public AccountJpaEntity save(AccountJpaEntity accountJpaEntity) {
        return this.accountrepository.save(accountJpaEntity);
    }

}
