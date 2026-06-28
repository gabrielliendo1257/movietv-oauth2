package com.guille.media.reproductor.powercine.auth.presenter.filters;


import com.guille.media.reproductor.powercine.auth.domain.models.UserIdentity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Profile(value = {"dev"})
public class DevAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		log.info("DevAuthenticationFilter: doFilterInternal");
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("DevAuthenticationFilter: doFilterInternal: authentication is null");
			Collection<GrantedAuthority> authorities =
				List.of(
					new SimpleGrantedAuthority("ROLE_ADMIN"),
					new SimpleGrantedAuthority("ROLE_USER")
				);

			UserIdentity identity =
				new UserIdentity(
					1,
					"developer",
					List.of("ADMIN", "USER")
				);
			log.info("UserIdentity is {}", identity);

			Authentication authentication =
				new UsernamePasswordAuthenticationToken(
					identity,
					null,
					authorities
				);

			SecurityContextHolder.getContext()
				.setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
