package com.guille.media.reproductor.powercine.advisors;

import com.guille.media.reproductor.powercine.advisors.models.ErrorResponse;
import com.guille.media.reproductor.powercine.exceptions.media.MediaAlreadyExistException;
import com.guille.media.reproductor.powercine.exceptions.media.MediaNotContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class MediaExceptionsHandler
{
    @ExceptionHandler(MediaAlreadyExistException.class)
    public ResponseEntity<?> handleMediaAlreadyExistException(MediaAlreadyExistException ex)
    {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MediaNotContentException.class)
    public ResponseEntity<?> handleMediaNotContentException(MediaNotContentException ex)
    {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ErrorResponse(HttpStatus.NO_CONTENT.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}
