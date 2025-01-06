package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.dto.ConvertDto;

public interface ConvertService {

    ConvertDto.Response sendUrlAndGetProcessedUrl(ConvertDto.Request request);
}
