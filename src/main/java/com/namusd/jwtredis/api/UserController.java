package com.namusd.jwtredis.api;

import com.namusd.jwtredis.model.dto.UserDto;
import com.namusd.jwtredis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserDto.Join user) {
        userService.join(user);
        return ResponseEntity.created(URI.create("/api/user/join")).body("가입 완료");
    }


}
