package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.dto.video.ConvertDto;

public interface ConvertService {

    ConvertDto.Response sendUrlAndGetProcessedUrl(ConvertDto.Request request);
}
