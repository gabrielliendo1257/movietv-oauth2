package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.dto.request.CreateMediaRequest;
import com.guille.media.reproductor.powercine.dto.request.FileUploadDto;
import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import com.guille.media.reproductor.powercine.dto.response.MediaSignatureDto;
import com.guille.media.reproductor.powercine.exceptions.media.MediaAlreadyExistException;
import com.guille.media.reproductor.powercine.exceptions.media.MediaNotContentException;
import com.guille.media.reproductor.powercine.exceptions.s3.BucketNotExistException;
import com.guille.media.reproductor.powercine.mapper.MediaMapper;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.pipes.FilenameConvert;
import com.guille.media.reproductor.powercine.repository.MediaRepository;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;
import com.guille.media.reproductor.powercine.service.interfaces.MessagingService;
import com.guille.media.reproductor.powercine.service.interfaces.S3Service;
import io.minio.http.Method;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Profile(value = {"test"})
public class MediaServiceImpl implements IMediaService {

	private final MediaRepository mediaRepository;
	private final S3Service s3Service;
	private final MediaMapper mediaMapper;
	private final MessagingService messagingService;
	private final FilenameConvert filenameConvert;

	public MediaServiceImpl(MediaRepository mediaRepository, S3Service s3Service, MediaMapper mediaMapper,
		MessagingService messagingService, FilenameConvert filenameConvert) {
		this.mediaRepository = mediaRepository;
		this.s3Service = s3Service;
		this.mediaMapper = mediaMapper;
		this.messagingService = messagingService;
		this.filenameConvert = filenameConvert;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<MediaJpaEntity> findByTitle(String title) {
		return this.mediaRepository.findByTitle(title);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<MediaJpaEntity> findByid(Integer id) {
		return this.mediaRepository.findById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<MediaJpaEntity> findByLikeTitle(String likeTitle) {
		return this.mediaRepository.findByLikeTitle(likeTitle);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'STANDARD_USER', 'PREMIUM_USER') or hasAnyAuthority('PLATFORM_MANAGER', 'ADMININTRATOR')")
	@Transactional
	@Override
	public MediaJpaEntity save(MediaJpaEntity entity) {

		try {
			return this.mediaRepository.save(entity);
		} catch (Exception ex) {
			log.info("Exception class: {}", ex.getClass());
			throw new MediaAlreadyExistException("Media already exists.");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<MediaJpaEntity> findAllMedias() {
		List<MediaJpaEntity> mediaEntity = this.mediaRepository.findAll();

		if (mediaEntity.isEmpty()) {
			throw new MediaNotContentException("Empty medias.");
		}

		return mediaEntity;
	}

	@Override
	public String getPresignedUrl(String bucket, String filename, Method method, Integer expire) {
		if (!this.s3Service.existBucket(bucket)) {
			throw new BucketNotExistException("Bucket " + bucket + " not exist");
		}

		return this.s3Service.getSignatureUrl(bucket, filename, method, expire);
	}

	@Override
	public MediaSignatureDto getMediaSignature(String bucket, FileUploadDto upload, Method method, Integer expire,
		Boolean fileConvert) {
		if (!this.s3Service.existBucket(bucket)) {
			throw new BucketNotExistException("Bucket " + bucket + " not exist");
		}

		String filename = upload.filename();
		if (fileConvert) {
			filename = this.filenameConvert.convert(upload.filename());
		}
		String presignedUrl = this.s3Service.getSignatureUrl(bucket, filename, method, expire);

		return new MediaSignatureDto(presignedUrl, upload.filename());
	}

	@Override
	public void createMedia(CreateMediaRequest request) {
		MediaJpaEntity mediaJpaEntity = this.mediaMapper.toEntity(request.media());
		mediaJpaEntity.addMediaSignature(new MediaJpaSignature(request.file().filename()));
		log.info("MediaJpaEntity: {}", mediaJpaEntity);

		MediaJpaEntity mediaResult = this.mediaRepository.save(mediaJpaEntity);

		log.info("Sending media: {}", mediaResult);
		MediaDto mediaDto = this.mediaMapper.toDto(mediaResult);
		log.info("MediaDto: {}", mediaDto);

		this.messagingService.sendMovie(mediaDto);
	}

}
