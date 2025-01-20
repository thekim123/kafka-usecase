package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.RefreshTokenIndex;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenIndexRepository extends CrudRepository<RefreshTokenIndex, String> {

    Optional<RefreshTokenIndex> findByUsername(String username);
}
