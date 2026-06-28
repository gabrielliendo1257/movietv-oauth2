package com.guille.media.reproductor.powercine.storage.domain.ports;

import com.guille.media.reproductor.powercine.storage.domain.models.StorageObject;

public interface StorageRepository
{
    StorageObject save(StorageObject storageObject);

    StorageObject findById(String storageId);
}
