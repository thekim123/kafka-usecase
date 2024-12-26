package com.namusd.jwtredis.persistence.repository;

import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;
    private final SqlSession sqlSession;

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(userMapper.selectById(id));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMapper.findByUsername(username));
    }

    @SuppressWarnings("UnusedReturnValue")
    public User save(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.updateById(user);
        }

        // 강제로 SQL 실행
        sqlSession.flushStatements();
        return userMapper.selectById(user.getId());
    }


}
