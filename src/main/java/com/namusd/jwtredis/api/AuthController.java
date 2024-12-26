package com.namusd.jwtredis.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.namusd.jwtredis.facade.JwtFacade;
import com.namusd.jwtredis.model.dto.JwtDto;
import com.namusd.jwtredis.model.entity.RefreshToken;
import com.namusd.jwtredis.service.JwtService;
import com.namusd.jwtredis.service.RedisService;
import com.namusd.jwtredis.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtFacade jwtFacade;
    private final RedisService redisService;
    private final JwtService jwtService;

    @PostMapping(value = "/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(
            @RequestBody JwtDto.RefreshRequest refreshToken,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String requestUrl = request.getRequestURL().toString();
        JwtDto.Refresh tokens = jwtFacade.refreshJwt(refreshToken.getSendRefreshToken(), requestUrl);
        JwtUtil.withAccessToken(tokens.getAccessToken(), response);
        JwtUtil.withRefreshToken(tokens.getRefreshToken(), response);
        return ResponseEntity.ok().body("리프레시 토큰 발급 완료");
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestBody JwtDto.RefreshRequest refreshToken) {
        jwtFacade.logout(refreshToken.getSendRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 유저네임으로 토큰을 찾고 지운다
    @DeleteMapping("/auth/logout/all")
    public ResponseEntity<?> logoutAll(
            @RequestBody JwtDto.RefreshRequest refreshToken
    ) {
        DecodedJWT jwt = jwtService.decodeRefreshToken(refreshToken.getSendRefreshToken());
        List<RefreshToken> tokens = redisService.getRefreshTokensByUsername(jwt.getSubject());
        System.out.println(tokens);
        String tokenid = jwt.getClaim("tokenId").asString();
//        RefreshToken token = redisService.getRefreshToken(tokenid)
//                .orElse(null);
//        System.out.println(token);
        return null;
    }


}
