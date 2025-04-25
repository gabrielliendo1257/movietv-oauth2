package com.guille.media.reproductor.powercine.restcontroller;

import jakarta.servlet.http.HttpServletResponse;

import com.guille.media.reproductor.powercine.dto.JwtAuthResponse;
import com.guille.media.reproductor.powercine.dto.LoginDto;
import com.guille.media.reproductor.powercine.models.Account;
import com.guille.media.reproductor.powercine.models.Otp;
import com.guille.media.reproductor.powercine.service.security.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${restcontroller.api.path.base}")
public class AuthenticationAccountController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<?> saveAccount(@RequestBody Account account) {
        return new ResponseEntity<Account>(this.authenticationService.saveNewAccount(account), HttpStatus.CREATED);
    }

    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<?> loginAccount(@RequestBody LoginDto loginDto) {
        String token = this.authenticationService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<JwtAuthResponse>(jwtAuthResponse,
                HttpStatus.OK);
    }

    @PostMapping(value = "/otp/check", consumes = "application/json")
    public void otpCheck(@RequestBody Otp otpValidate, HttpServletResponse response) {
        if (this.authenticationService.check(otpValidate)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
