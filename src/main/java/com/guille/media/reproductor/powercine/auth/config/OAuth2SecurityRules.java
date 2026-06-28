package com.guille.media.reproductor.powercine.auth.config;

import com.guille.media.reproductor.powercine.shared.core.SecurityRuleContributor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SecurityRules implements SecurityRuleContributor {

	@Value(value = "${api.path.base}")
	private String apiPathBase;

	@Override
	public void contribute(
		AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
		registry
			.requestMatchers(HttpMethod.POST, this.apiPathBase + "/auth/exchange").permitAll()
			.requestMatchers(HttpMethod.GET, this.apiPathBase + "/auth/me").permitAll()
			.requestMatchers(HttpMethod.GET, this.apiPathBase + "/auth/admin").permitAll()

			.requestMatchers(HttpMethod.POST, this.apiPathBase + "/dev/auth/token").permitAll();
	}
}
