package com.guille.media.reproductor.powercine.dto.response;

import java.time.LocalDateTime;

public record AccountDto(String username, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
