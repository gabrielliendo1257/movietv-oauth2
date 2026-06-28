package com.guille.media.reproductor.powercine.storage.app.converters;

import com.guille.media.reproductor.powercine.storage.app.errors.InvalidConversion;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Component
public class StringToInstant implements Converter<String, Instant>
{
    @Nullable
    @Override
    public Instant convert(String source)
    {
        try {
            return Instant.parse(source);
        } catch (DateTimeParseException e) {
            throw new InvalidConversion(
                    "Invalid Instant format.",
                    e
            );
        }
    }
}
