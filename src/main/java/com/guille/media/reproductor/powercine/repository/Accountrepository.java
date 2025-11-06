package com.guille.media.reproductor.powercine.repository;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.AccountJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Accountrepository extends JpaRepository<AccountJpaEntity, Integer> {

    Optional<AccountJpaEntity> findByUsername(String username);

    Optional<AccountJpaEntity> findByEmail(String email);
}
