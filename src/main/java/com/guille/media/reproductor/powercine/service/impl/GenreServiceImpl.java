package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.exceptions.media.GenreAlreadyExistException;
import com.guille.media.reproductor.powercine.exceptions.media.GenreNoContentException;
import com.guille.media.reproductor.powercine.models.GenreJpaEntity;
import com.guille.media.reproductor.powercine.repository.GenreRepository;
import com.guille.media.reproductor.powercine.service.interfaces.GenreService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(value = {"test"})
public class GenreServiceImpl implements GenreService
{
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository)
    {
        this.genreRepository = genreRepository;
    }

    @Override
    public void saveGenre(GenreJpaEntity genre)
    {
        try
        {
            this.genreRepository.save(genre);
        } catch (Exception ex)
        {
            throw new GenreAlreadyExistException("Genre does exist.");
        }
    }

    @Override
    public GenreJpaEntity findGenreByName(String name)
    {
        return this.genreRepository.findByName(name)
                .orElseThrow(() -> new GenreNoContentException("Not exist genre with name: " + name));
    }

    @Override
    public GenreJpaEntity findGenreById(Long id)
    {
        return this.genreRepository.findById(id)
                .orElseThrow(() -> new GenreNoContentException("Not exist genre with id: " + id));
    }

    @Override
    public void updateGenre(GenreJpaEntity genre)
    {
        if (this.genreRepository.findById(genre.getId()).isPresent())
        {
            this.genreRepository.save(genre);
        }

        throw new GenreNoContentException("Not exist genre with id: " + genre.getId());
    }

    @Override
    public void deleteGenre(GenreJpaEntity genre)
    {
        if (this.genreRepository.findById(genre.getId()).isPresent())
        {
            this.genreRepository.delete(genre);
        }

        throw new GenreNoContentException("Not exist genre with id: " + genre.getId());
    }
}
