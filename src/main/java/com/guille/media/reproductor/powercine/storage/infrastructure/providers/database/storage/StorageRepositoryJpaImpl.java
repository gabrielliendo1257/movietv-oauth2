package com.guille.media.reproductor.powercine.storage.infrastructure.providers.database.storage;

import com.guille.media.reproductor.powercine.storage.domain.models.StorageObject;
import com.guille.media.reproductor.powercine.storage.domain.ports.StorageRepository;
import com.guille.media.reproductor.powercine.storage.infrastructure.errors.EntityNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile(value = {"dev"})
public class StorageRepositoryJpaImpl implements StorageRepository {

	private final StorageDataRepository storageDataRepository;
	private final StorageMapper storageMapper;

	public StorageRepositoryJpaImpl(StorageDataRepository storageDataRepository, StorageMapper storageMapper) {
		this.storageDataRepository = storageDataRepository;
		this.storageMapper = storageMapper;
	}

	@Override
	public StorageObject save(StorageObject storageObject) {
		log.info("Saving storage object: {}", storageObject);
		return this.storageMapper.toStorageObject(
			this.storageDataRepository.save(
				this.storageMapper.toJpaEntity(storageObject)
			)
		);
	}

	@Override
	public StorageObject findById(String storageId) {
		log.info("Finding storage object by id: {}", storageId);
		var storeConverted = this.storageMapper.toStorageObject(
			this.storageDataRepository.findById(storageId)
				.orElseThrow(() -> new EntityNotFound("Storage entity not found from id: " + storageId))
		);
		log.info("Converted storage object: {}", storeConverted);
		return storeConverted;
	}
}
