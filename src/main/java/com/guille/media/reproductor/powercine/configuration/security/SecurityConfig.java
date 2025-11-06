package com.guille.media.reproductor.powercine.configuration.security;

import java.util.List;

import com.guille.media.reproductor.powercine.mapper.AccountMapper;
import com.guille.media.reproductor.powercine.models.AccountJpaEntity;
import com.guille.media.reproductor.powercine.models.SecurityAccount;
import com.guille.media.reproductor.powercine.repository.Accountrepository;
import com.guille.media.reproductor.powercine.utils.enums.Roles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

  @Value(value = "${api.path.base}")
  private String apiPathBase;

  @Bean
  @Order(value = 2)
  SecurityFilterChain configuration(HttpSecurity http)
      throws Exception {
    return http
        .csrf(CsrfConfigurer::disable)
        .httpBasic(HttpBasicConfigurer::disable)
        .oauth2ResourceServer((resourceServer) -> resourceServer
            .jwt(jwtConfig -> jwtConfig
                .jwkSetUri("http://localhost:8080/oauth2/jwks")))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, this.apiPathBase + "/hello").permitAll()
            .requestMatchers(HttpMethod.GET, this.apiPathBase + "/all").permitAll()
            .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .build();
  }

  @Bean
  AuthenticationManager authenticationProvider(PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService) {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);

    return new ProviderManager(daoAuthenticationProvider);
  }

  @Bean
  OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(
      @Value(value = "${spring.application.name}") String applicationName) {
    return (context) -> {
      var authenticatio = context.getPrincipal();

      if (authenticatio instanceof UsernamePasswordAuthenticationToken authenticationToken) {
        if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
          List<String> roles = authenticationToken.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .toList();

          context.getClaims().audience(List.of(applicationName));
          context.getClaims().subject(authenticationToken.getName());
          context.getClaims().claim("roles", roles);
        }
      }
    };
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

  @Bean
  @Profile(value = { "test", "dev" })
  UserDetailsService userDetailService(Accountrepository accountrepository, AccountMapper accountMapper) {
    return (username) -> {
      AccountJpaEntity accountEntity = accountrepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("Ususario no encontrado."));
      SecurityAccount securityAccount = accountMapper.toSecurityAccount(accountEntity);
      log.info("SecurityAccount after mapper: {}", securityAccount);

      return securityAccount;
    };
  }

  @Bean
  @Profile(value = { "test" })
  UserDetailsService userDetailServiceDev(PasswordEncoder passwordEncoder) {
    SecurityAccount securityAccount = new SecurityAccount("adminusername", passwordEncoder.encode("adminpassword"),
        Roles.ADMIN);

    return new InMemoryUserDetailsManager(securityAccount);
  }

  @Bean
  @Profile(value = { "test", "dev" })
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
