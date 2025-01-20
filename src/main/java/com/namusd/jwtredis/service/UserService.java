package com.namusd.jwtredis.service;

import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.dto.UserDto;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.model.entity.UserRole;
import com.namusd.jwtredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void join(UserDto.Join dto) {
        User user = User.builder()
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));
    }


}
