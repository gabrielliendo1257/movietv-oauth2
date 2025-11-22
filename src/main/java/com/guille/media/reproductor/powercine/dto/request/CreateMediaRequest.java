package com.guille.media.reproductor.powercine.dto.request;


public record CreateMediaRequest(
        MediaDto media,
        FileUploadDto file
) {}