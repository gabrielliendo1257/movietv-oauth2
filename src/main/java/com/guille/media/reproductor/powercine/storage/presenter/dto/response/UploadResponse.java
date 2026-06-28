package com.guille.media.reproductor.powercine.storage.presenter.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UploadResponse(
	@JsonProperty(value = "uploadId") String uploadId,
	@JsonProperty(value = "uploadUrl") String uploadUrl,
	@JsonProperty(value = "storageKey") String storageKey,
	@JsonProperty(value = "expiresAt") String expiresAt,
	@JsonProperty(value = "requiredHeaders") Map<String, String> headers,
	@JsonProperty String method
) {

}
