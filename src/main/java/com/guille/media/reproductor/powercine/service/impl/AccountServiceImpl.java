package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.exceptions.customer.AccountAlreadyExistsException;
import com.guille.media.reproductor.powercine.models.AccountJpaEntity;
import com.guille.media.reproductor.powercine.repository.Accountrepository;
import com.guille.media.reproductor.powercine.service.interfaces.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Profile(value = {"test"})
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
        try {
            return this.accountrepository.save(accountJpaEntity);
        } catch (Exception ex) {
            log.info("Save account failed");
            throw new AccountAlreadyExistsException("Account " + accountJpaEntity.getUsername() + " already exists");
        }

    }

}
