package com.guille.media.reproductor.powercine.restcontroller.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
public class JwtCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Cookie cookie = WebUtils.getCookie(request, "access_token");
        log.info("Filter Cookie: {}", cookie);

        String token = extractTokenFromCookie(request);
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Inyectamos el header Authorization
        HttpServletRequest wrapper = new AuthorizationHeaderRequestWrapper(request, "Authorization", "Bearer " + token);

        filterChain.doFilter(wrapper, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if ("access_token".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    private static class AuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, List<String>> additionalHeaders = new HashMap<>();

        AuthorizationHeaderRequestWrapper(HttpServletRequest request, String headerName, String headerValue) {
            super(request);
            additionalHeaders.put(headerName.toLowerCase(Locale.ROOT), List.of(headerValue));
        }

        @Override
        public String getHeader(String name) {
            if (name == null) return super.getHeader(name);
            String key = name.toLowerCase(Locale.ROOT);
            List<String> values = additionalHeaders.get(key);
            if (values != null && !values.isEmpty()) {
                return values.get(0);
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (name == null) return super.getHeaders(name);
            String key = name.toLowerCase(Locale.ROOT);
            List<String> values = additionalHeaders.get(key);
            if (values != null) {
                return Collections.enumeration(values);
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            // combinamos nombres existentes + adicionales (sin duplicados)
            List<String> headerNames = Collections.list(super.getHeaderNames());
            for (String k : additionalHeaders.keySet()) {
                // original header name case: buscamos en la request original si existe
                boolean found = headerNames.stream().anyMatch(h -> h.equalsIgnoreCase(k));
                if (!found) {
                    headerNames.add(k);
                }
            }
            return Collections.enumeration(headerNames);
        }
    }
}
