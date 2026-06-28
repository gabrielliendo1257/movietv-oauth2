package com.guille.media.reproductor.powercine.storage.config;

import com.guille.media.reproductor.powercine.shared.core.SecurityRuleContributor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class StorageSecurityRules implements SecurityRuleContributor {

	@Value(value = "${api.v2.path.base}")
	private String apiV2PathBase;

	@Override
	public void contribute(
		AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry
	) {
		registry
			.requestMatchers(HttpMethod.POST, this.apiV2PathBase + "/storage/upload").hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, this.apiV2PathBase + "/storage/streaming").permitAll();
	}
}
