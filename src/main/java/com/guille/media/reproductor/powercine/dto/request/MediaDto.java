package com.guille.media.reproductor.powercine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record MediaDto(String title, String description, @Size(max = 200) String thumbnail,
                       @Size(max = 100) @JsonProperty(value = "id_file") String idFile) {
}
