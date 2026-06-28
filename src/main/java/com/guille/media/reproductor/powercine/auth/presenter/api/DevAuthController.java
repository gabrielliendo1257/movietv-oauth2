package com.guille.media.reproductor.powercine.auth.presenter.api;

import com.guille.media.reproductor.powercine.auth.domain.models.DevTokenRequest;
import com.guille.media.reproductor.powercine.auth.domain.models.UserIdentity;
import com.guille.media.reproductor.powercine.auth.infrastructure.jwt.JwtService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
	value = "/api/v1/movie/dev/auth",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE
)
@Profile(value = {"dev"})
public class DevAuthController {

	private final JwtService jwtService;

	public DevAuthController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping(value = "/token")
	public ResponseEntity<?> token(@RequestBody DevTokenRequest request) {
		UserIdentity userIdentity = new UserIdentity(
			request.userId(),
			request.username(),
			request.roles()
		);

		return ResponseEntity.ok(this.jwtService.generate(userIdentity));
	}
}
