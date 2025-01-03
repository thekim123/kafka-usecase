package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.dto.ConvertDto;

public interface ConvertService {
    //    @Override
    //    public void sendVideoUrl(String videoUrl) {
    //        kafkaTemplate.send("videoUrl", videoUrl);
    //    }
    ConvertDto.Response sendUrlAndGetProcessedUrl(ConvertDto.Request request);
}
