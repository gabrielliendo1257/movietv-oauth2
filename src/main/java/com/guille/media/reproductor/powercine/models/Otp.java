package com.guille.media.reproductor.powercine.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Otp {

    @Id
    private String username;
    private String code;
}
