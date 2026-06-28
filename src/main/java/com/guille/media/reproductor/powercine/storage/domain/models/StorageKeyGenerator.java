package com.guille.media.reproductor.powercine.storage.domain.models;

import com.guille.media.reproductor.powercine.storage.domain.vos.StorageKey;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class StorageKeyGenerator
{
    public StorageKey generate()
    {
        String key = UUID.randomUUID().toString();
        return new StorageKey(key);
    }
}
