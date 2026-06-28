package com.guille.media.reproductor.powercine.advisors;

import com.guille.media.reproductor.powercine.advisors.models.ErrorResponse;
import com.guille.media.reproductor.powercine.exceptions.s3.BucketNotExistException;
import com.guille.media.reproductor.powercine.exceptions.s3.GetPresignedObjectException;
import com.guille.media.reproductor.powercine.exceptions.s3.MakeBucketException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class S3ExceptionHandler {

    @ExceptionHandler(BucketNotExistException.class)
    public ResponseEntity<?> buckedNotExistException(BucketNotExistException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(GetPresignedObjectException.class)
    public ResponseEntity<?> presignedObjectException(GetPresignedObjectException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MakeBucketException.class)
    public ResponseEntity<?> makeBucketException(MakeBucketException ex)
    {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}
