package com.guille.media.reproductor.powercine.configuration;

import com.guille.media.reproductor.powercine.auth.config.OAuth2SecurityRules;
import com.guille.media.reproductor.powercine.auth.presenter.filters.DevAuthenticationFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${powercine.env.frontendapp.endpoint}")
    private String frontendAddress;

    @Value(value = "${api.path.base}")
    private String apiPathBase;

    private final DevAuthenticationFilter devAuthenticationFilter;
    private final OAuth2SecurityRules oAuth2SecurityRules;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    SecurityFilterChain configuration(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(
                        jwtConfig -> jwtConfig
                                .jwkSetUri("http://127.0.0.1:9090/oauth2/jwks")))
                .addFilterBefore(this.devAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(authorize -> {
                    this.oAuth2SecurityRules.contribute(authorize);

                    authorize
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers(HttpMethod.GET, this.apiPathBase + "/hello").permitAll()
                            .requestMatchers(HttpMethod.GET, this.apiPathBase + "/all").permitAll()
                            .requestMatchers("/ws/**")
                            .permitAll()

                            .anyRequest().authenticated();
                }).formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter authenticationConverter() {
        var jwtConverter = new JwtGrantedAuthoritiesConverter();
        jwtConverter.setAuthorityPrefix("");
        jwtConverter.setAuthoritiesClaimName("roles");

        var jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtConverter);

        return jwtAuthConverter;
    }

}
