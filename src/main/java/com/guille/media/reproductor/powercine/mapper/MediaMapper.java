package com.guille.media.reproductor.powercine.mapper;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import com.guille.media.reproductor.powercine.dto.response.MediaSignatureDto;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;

import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    // resquest
    MediaJpaEntity toEntity(MediaDto mediaDto);

    MediaDto toDto(MediaJpaEntity mediaJpaEntity);

    //response
    MediaSignatureDto toSignatureDto(MediaJpaSignature mediaJpaSignature);
}
