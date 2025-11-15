package com.guille.media.reproductor.powercine.advisors;

import com.guille.media.reproductor.powercine.exceptions.BucketNotExistException;
import com.guille.media.reproductor.powercine.exceptions.GetPresignedObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class S3ExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An uncontrolled error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(BucketNotExistException.class)
    public ResponseEntity<?> buckedNotExistException(BucketNotExistException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal error occurred on bucked: " + ex.getMessage()));
    }

    @ExceptionHandler(GetPresignedObjectException.class)
    public ResponseEntity<?> presignedObjectException(GetPresignedObjectException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", ex.getMessage()));
    }
}
