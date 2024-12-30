package com.namusd.jwtredis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.namusd.jwtredis.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@TableName(value = "User")
public class User {
    @TableId(value = "id", type = IdType.AUTO) // AUTO_INCREMENT 사용
    private Long id;
    private String username;
    private String password;
    private UserRole role;

    public UserDto.Response toDto() {
        return UserDto.Response.builder()
                .id(this.id)
                .username(this.username)
                .roles(this.role)
                .build();
    }
}
