package com.namusd.jwtredis.service.helper;

import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.entity.video.Image;
import com.namusd.jwtredis.repository.ImageRepository;


public class ImageServiceHelper {

    public static Image findImageById(Long imageId, ImageRepository repository) {
        return repository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("이미지 없음"));
    }
}
