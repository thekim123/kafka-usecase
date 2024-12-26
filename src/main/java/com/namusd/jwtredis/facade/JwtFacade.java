package com.namusd.jwtredis.facade;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.namusd.jwtredis.service.JwtService;
import com.namusd.jwtredis.model.dto.JwtDto;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.service.UserService;
import com.namusd.jwtredis.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtFacade {

    private final JwtService jwtService;
    private final UserService userService;


    public JwtDto.Refresh buildRefreshJwt(String refreshToken, String requestUrl) {
        JwtUtil.isValidJwt(refreshToken);
        DecodedJWT decodedJWT = jwtService.decodeRefreshToken(refreshToken);
        String username = decodedJWT.getSubject();
        String tokenId = decodedJWT.getClaim("tokenId").asString();
        User user = userService.findByUsername(username);

        String access = jwtService.generateAccessToken(user, requestUrl);
        String refresh = jwtService.updateRefreshToken(user, tokenId, requestUrl);

        return JwtDto.Refresh.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }

}
