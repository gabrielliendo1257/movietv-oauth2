package com.guille.media.reproductor.powercine.pipes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class FilenameConvert implements Converter<String, String> {

    @Override
    public String convert(String source) {
        log.info("Converting URI: {}", source);

        String[] sourceSplit = source.split("\\.");
        String extension = sourceSplit[sourceSplit.length - 1];

        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");
        String formatedDate = currentDate.format(formatter);

        String updateName = formatedDate + "." + extension;
        log.info("Converting filename: {}", updateName);

        return updateName;
    }
}
