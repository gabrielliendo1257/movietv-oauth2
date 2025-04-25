package com.guille.media.reproductor.powercine.filters;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.guille.media.reproductor.powercine.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    @Value(value = "${restcontroller.api.path.base}")
    private String pathBase;

    @Value(value = "${jwt.signing.key}")
    private String signinKey;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");
        if (jwt == null) {
            String token = this.jwtService.generateToken();

            log.info("Token generado para Usuario visitante -> " + token);
            response.setHeader("Authorization", token);

        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPathFilter = request.getServletPath();
        log.info("ServletPath -> " + servletPathFilter + " -> " + servletPathFilter.equals(this.pathBase + "/signin"));
        return servletPathFilter.equals(this.pathBase + "/signup")
                || servletPathFilter.equals(this.pathBase + "/signin");
    }
}
