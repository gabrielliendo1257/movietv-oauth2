package com.guille.media.reproductor.powercine.mapper;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    MediaJpaEntity toEntity(MediaDto mediaDto);
}
