package com.guille.media.reproductor.powercine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MediaSignatureDto(@JsonProperty(value = "presigned_url") String presignedUrl,
                                @JsonProperty(value = "upload_id") String id) {
}
