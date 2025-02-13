package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.dto.video.ImageDto;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import com.namusd.jwtredis.model.entity.video.Image;
import com.namusd.jwtredis.model.entity.video.ImageType;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.repository.ImageRepository;
import com.namusd.jwtredis.repository.VideoRepository;
import com.namusd.jwtredis.service.helper.ImageServiceHelper;
import com.namusd.jwtredis.service.helper.VideoServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public String insertImage(MultipartFile file, String videoId, ImageType imageType) {
        Video video = VideoServiceHelper.findVideoById(videoId, videoRepository);

        Image targetImage = Image.builder()
                .originalFilename(file.getOriginalFilename())
                .imageType(imageType)
                .video(video)
                .build();
        Image entity = imageRepository.save(targetImage);
        return entity.getId().toString();
    }

    @Transactional
    public String insertImage(MultipartFile file, Video video, ImageType imageType) {
        Image targetImage = Image.builder()
                .originalFilename(file.getOriginalFilename())
                .imageType(imageType)
                .video(video)
                .build();
        Image entity = imageRepository.save(targetImage);
        return entity.getId().toString();
    }


    @Transactional
    public void insertImages(List<ImageDto> imageDtoList) {
        if (imageDtoList == null || imageDtoList.isEmpty()) {
            return;
        }

        List<Image> images = imageDtoList.stream()
                .map(dto -> {
                    Video video = VideoServiceHelper.findVideoById(dto.getVideoId(), videoRepository);

                    return Image.builder()
                            .filename(dto.getFilename())
                            .originalFilename(dto.getOriginalFilename())
                            .imageType(ImageType.valueOf(dto.getImageType()))
                            .video(video)
                            .imageFile(dto.getImageFile())
                            .build();
                })
                .collect(Collectors.toList());

        imageRepository.saveAll(images);
    }


    @Transactional
    public void withImageFile(AttachFile attachFile, Long imageId) {
        Image image = ImageServiceHelper.findImageById(imageId, imageRepository);
        image.withImageFile(attachFile);
    }

}
