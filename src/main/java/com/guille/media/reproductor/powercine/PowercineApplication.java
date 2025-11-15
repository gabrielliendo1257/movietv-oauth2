package com.guille.media.reproductor.powercine;

import com.guille.media.reproductor.powercine.models.AccountJpaEntity;
import com.guille.media.reproductor.powercine.repository.Accountrepository;
import com.guille.media.reproductor.powercine.utils.enums.Roles;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@EnableCaching
@SpringBootApplication
public class PowercineApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowercineApplication.class, args);
    }

    @Bean
    ApplicationRunner persistMovies(Accountrepository accountrepository, PasswordEncoder passwordEncoder) {
        return args -> {
            AccountJpaEntity adminEntity = new AccountJpaEntity(null, "adminusername", passwordEncoder.encode("adminpassword"), "admin@gmail.com",
                    null, null, Roles.ADMIN);
            AccountJpaEntity standardEntity = new AccountJpaEntity(null, "piterusername", passwordEncoder.encode("piterpassword"), "piter@gmail.com", null, null, Roles.STANDARD_USER);
            AccountJpaEntity premiumEntity = new AccountJpaEntity(null, "premiumusername", passwordEncoder.encode("premiumpassword"), "premium@gmail.com", LocalDateTime.now(), LocalDateTime.now(), Roles.PREMIUM_USER);

            accountrepository.save(premiumEntity);
            accountrepository.save(standardEntity);
            accountrepository.save(adminEntity);
        };
    }

}
