package com.guille.media.reproductor.powercine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record FileUploadDto(
        @Size(min = 4, max = 200, message = "Debe tener un minimo de 4 caracteres.")
        @NotBlank(message = "No debe contener solo espacios en blanco.")
        @NotEmpty(message = "No debe ser nulo.")
        String filename
)
{
}
