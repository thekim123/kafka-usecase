package com.namusd.jwtredis.model.entity.user;

import com.namusd.jwtredis.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@Table(name = "user")
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserDto.Response toDto() {
        return UserDto.Response.builder()
                .id(this.id)
                .username(this.username)
                .roles(this.role)
                .build();
    }
}
