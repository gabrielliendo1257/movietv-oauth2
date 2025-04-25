package com.guille.media.reproductor.powercine.repository;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Accountrepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);
}
