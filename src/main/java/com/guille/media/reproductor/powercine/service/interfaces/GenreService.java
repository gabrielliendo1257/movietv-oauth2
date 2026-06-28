package com.guille.media.reproductor.powercine.service.interfaces;

import com.guille.media.reproductor.powercine.models.GenreJpaEntity;


public interface GenreService
{
    void saveGenre(GenreJpaEntity genre);

    GenreJpaEntity findGenreByName(String name);

    GenreJpaEntity findGenreById(Long id);

    void updateGenre(GenreJpaEntity genre);

    void deleteGenre(GenreJpaEntity genre);
}
