package com.guille.media.reproductor.powercine.repository;

import java.util.Optional;

import com.guille.media.reproductor.powercine.models.Otp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findByUsername(String username);
}
