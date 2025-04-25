package com.guille.media.reproductor.powercine.service.security;

import java.util.Map;
import java.util.Optional;

import com.guille.media.reproductor.powercine.dto.LoginDto;
import com.guille.media.reproductor.powercine.models.Account;
import com.guille.media.reproductor.powercine.models.Otp;
import com.guille.media.reproductor.powercine.repository.Accountrepository;
import com.guille.media.reproductor.powercine.repository.OtpRepository;
import com.guille.media.reproductor.powercine.service.JwtService;
import com.guille.media.reproductor.powercine.utils.GenerateCode;
import com.guille.media.reproductor.powercine.utils.enums.Roles;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final Accountrepository accountrepository;
    private final OtpRepository otRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public Account saveNewAccount(Account account) {
        String pE = this.passwordEncoder.encode(account.getPassword());
        account.setPassword(pE);
        account.setRoles(Roles.STANDARD_USER);
        return this.accountrepository.save(account);
    }

    public void login(String username, String password) {
        var authRequest = new LoginDto();
        authRequest.setUsername(username);
        authRequest.setPassword(password);
        this.login(authRequest);
    }

    public String login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = this.authenticationProvider.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return this.jwtService.generateToken(loginDto.getUsername(),
                Map.of("username", loginDto.getUsername(), "authority", "ROLE_" + Roles.STANDARD_USER.name()));

    }

    private void renewOtp(Account account) {
        String code = GenerateCode.generateCode();
        log.info("Code -> " + code);
        Optional<Otp> existOtp = this.otRepository.findByUsername(account.getUsername());

        if (existOtp.isPresent()) {
            Otp otp = existOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp();
            otp.setCode(code);
            otp.setUsername(account.getUsername());
            this.otRepository.save(otp);
        }
    }

    public Boolean check(Otp otpValidate) {
        Optional<Otp> existOtp = this.otRepository.findByUsername(otpValidate.getUsername());

        if (existOtp.isPresent()) {
            Otp otp = existOtp.get();

            if (otpValidate.getCode().equals(otp.getCode())) {
                return true;
            }
        }

        return false;
    }
}
