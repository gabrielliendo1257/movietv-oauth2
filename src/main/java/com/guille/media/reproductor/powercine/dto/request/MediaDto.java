package com.guille.media.reproductor.powercine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MediaDto(Integer id, String title, String overview, @JsonProperty(value = "poster_path") String posterPath,
                       @JsonProperty(value = "release_date") String releaseDate, Double popularity) {
}
