package com.guille.media.reproductor.powercine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record FileUploadDto(@Size(max = 50) String filename) {
}
