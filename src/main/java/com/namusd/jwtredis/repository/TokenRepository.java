package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<RefreshToken, String> {

    List<RefreshToken> findByUsername(String username);
}
