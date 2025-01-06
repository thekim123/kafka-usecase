package com.namusd.jwtredis.persistence.repository;

import com.namusd.jwtredis.model.entity.AttachFile;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.persistence.mapper.AttachFileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttachFileRepository {
    private final AttachFileMapper fileMapper;
    private final SqlSession sqlSession;

    public Optional<AttachFile> findById(Long id) {
        return Optional.ofNullable(fileMapper.selectById(id));
    }

    @SuppressWarnings("UnusedReturnValue")
    public AttachFile save(AttachFile file) {
        if (file.getId() == null) {
            fileMapper.insert(file);
        } else {
            fileMapper.updateById(file);
        }

        // 강제로 SQL 실행
        sqlSession.flushStatements();
        return fileMapper.selectById(file.getId());
    }

    public void deleteById(Long fileId) {
        fileMapper.deleteById(fileId);
    }
}
