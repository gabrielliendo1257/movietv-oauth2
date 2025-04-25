package com.guille.media.reproductor.powercine.models.listeners;

import java.util.Date;

import jakarta.persistence.PrePersist;

import com.guille.media.reproductor.powercine.models.Account;

public class AccountListener {

    @PrePersist
    public void setCreatedAt(Account account) {
        account.setCreatedAt(new Date());
    }
}
