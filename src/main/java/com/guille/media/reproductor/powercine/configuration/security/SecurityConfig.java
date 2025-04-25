package com.guille.media.reproductor.powercine.configuration.security;

import java.util.Date;

import com.guille.media.reproductor.powercine.models.Account;
import com.guille.media.reproductor.powercine.repository.Accountrepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final Accountrepository accountrepository;

    @Bean
    @Order(value = 2)
    protected SecurityFilterChain configuration(HttpSecurity http)
            throws Exception {
        return http
                .sessionManagement(sessionConf -> sessionConf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    // authorize.requestMatchers("/h2-console/**").permitAll();
                    // authorize.requestMatchers("/api/v1/movie/auth").permitAll();
                    // authorize.requestMatchers("/api/v1/movie/login").permitAll();
                    // authorize.requestMatchers("/api/v1/movie/hello").hasAuthority(Permissions.GUEST.name());
                    // authorize.requestMatchers("/api/v1/movie/content").hasAnyRole(Roles.STANDARD_USER.name());
                    // authorize.requestMatchers("/api/v1/movie/post_content").hasAnyRole(Roles.GUEST_USER.name());
                    authorize.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailService());

        return new DaoAuthenticationProvider();
    }

    @Bean
    @Profile(value = "prod")
    UserDetailsService userDetailService() {
        log.info("UserDetailsService (prod)");

        return (username) -> this.accountrepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Ususario no encontrado."));
    }

    @Bean
    @Profile(value = "dev")
    UserDetailsService userDetailServiceDev() {
        log.info("UserDetailsService (dev)");

        var account1 = Account.builder()
                .username("piter")
                .password(this.passwordEncoder().encode("javi"))
                .email("javi@gmail.com")
                .createdAt(new Date())
                .build();

        return new InMemoryUserDetailsManager(account1);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
