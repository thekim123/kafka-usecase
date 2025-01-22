package com.namusd.jwtredis.api;

import com.namusd.jwtredis.facade.JwtFacade;
import com.namusd.jwtredis.model.dto.auth.JwtDto;
import com.namusd.jwtredis.service.JwtService;
import com.namusd.jwtredis.service.RedisService;
import com.namusd.jwtredis.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtFacade jwtFacade;
    private final RedisService redisService;
    private final JwtService jwtService;

    @PostMapping(value = "/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String requestUrl = request.getRequestURL().toString();
        String refreshToken = JwtUtil.getRefreshTokenFromCookie(request);
        JwtDto.Refresh tokens = jwtFacade.refreshJwt(refreshToken, requestUrl);
        JwtUtil.withAccessToken(tokens.getAccessToken(), response);
        JwtUtil.withRefreshToken(tokens.getRefreshToken(), response);
        Map<String, String> access = new HashMap<>();
        access.put("access", tokens.getAccessToken());
        return ResponseEntity.ok().body(access);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<?> logout(
            Authentication auth,
            HttpServletRequest request
    ) {
        String refreshToken = JwtUtil.getRefreshTokenFromCookie(request);
        jwtFacade.logout(refreshToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/auth/logout/device")
    public ResponseEntity<?> logoutDevice(
            Authentication auth,
            @RequestBody JwtDto.DeviceRemoteLogout device) {
        jwtFacade.logoutDevice(auth, device.getDeviceId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 유저네임으로 토큰을 찾고 지운다
    @DeleteMapping("/auth/logout/all")
    public ResponseEntity<?> logoutAll(Authentication auth) {
        jwtFacade.logoutAll(auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
