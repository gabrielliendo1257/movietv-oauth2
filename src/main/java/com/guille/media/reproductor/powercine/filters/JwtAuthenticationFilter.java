package com.guille.media.reproductor.powercine.filters;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.guille.media.reproductor.powercine.management.UsernamePasswordAuthentication;
import com.guille.media.reproductor.powercine.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value(value = "${restcontroller.api.path.base}")
    private String pathBase;

    @Value(value = "${jwt.signing.key}")
    private String signinKey;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        GrantedAuthority granted;
        String jwt = request.getHeader("Authorization");

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.split(" ")[1];
            Claims claims = this.jwtService.getClaims(jwt);

            String authority = (String) claims.get("authority");
            String username = (String) claims.get("username");

            log.info("Data Authorization (authority/username) -> " + authority + "/" + username);

            granted = new SimpleGrantedAuthority(authority);
            var auth = new UsernamePasswordAuthentication(username, null, List.of(granted));
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPathFilter = request.getServletPath();
        log.info("ServletPath -> " + servletPathFilter + " -> " + servletPathFilter.equals(this.pathBase + "/signup"));
        return servletPathFilter.equals(this.pathBase + "/signup")
                || servletPathFilter.equals(this.pathBase + "/signin");
    }

}
