package com.guille.media.reproductor.powercine.storage.app.converters;

import com.guille.media.reproductor.powercine.storage.domain.vos.MimeType;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMimeType implements Converter<String, MimeType>
{
    @Nullable
    @Override
    public MimeType convert(String source)
    {
        return new MimeType(source);
    }
}
