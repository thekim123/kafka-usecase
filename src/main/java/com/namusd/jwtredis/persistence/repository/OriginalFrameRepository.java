package com.namusd.jwtredis.persistence.repository;

import com.namusd.jwtredis.model.entity.video.OriginalFrame;
import com.namusd.jwtredis.persistence.mapper.OriginalFrameMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OriginalFrameRepository {

    private final OriginalFrameMapper frameMapper;
    private final SqlSession sqlSession;

    public Optional<OriginalFrame> findById(long id) {
        return Optional.ofNullable(frameMapper.selectById(id));
    }

    @SuppressWarnings("UnusedReturnValue")
    public OriginalFrame save(OriginalFrame originalFrame) {
        if (originalFrame.getId() == null) {
            frameMapper.insert(originalFrame);
        } else {
            frameMapper.updateById(originalFrame);
        }

        sqlSession.flushStatements();
        return frameMapper.selectById(originalFrame.getId());
    }

}
