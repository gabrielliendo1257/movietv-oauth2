package com.guille.media.reproductor.powercine.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "${restcontroller.api.path.base}")
public class MovieController {

    @GetMapping(value = "/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<String>("Hello STANDARD_CONTENT.", HttpStatus.OK);
    }

    @GetMapping(value = "/content")
    public ResponseEntity<?> content() {
        return new ResponseEntity<String>("Hello usuario autenticado.", HttpStatus.OK);
    }

    @PostMapping(value = "/post_content")
    public String posted() {
        return "Content posted succesfully";
    }
}
