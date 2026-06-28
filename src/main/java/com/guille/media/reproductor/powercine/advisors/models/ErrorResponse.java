package com.guille.media.reproductor.powercine.advisors.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse
{
    private Integer code;
    private String message;
    private LocalDateTime timestamp;
}
