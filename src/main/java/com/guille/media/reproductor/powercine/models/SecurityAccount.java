package com.guille.media.reproductor.powercine.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.guille.media.reproductor.powercine.utils.enums.Roles;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record SecurityAccount(String username, String password, Roles rol) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities =  this.rol.getPermissions().stream()
                .map((perm) -> new SimpleGrantedAuthority(perm.name()))
                .toList();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>(grantedAuthorities);
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.rol.name()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
