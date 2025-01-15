package com.namusd.jwtredis.persistence.repository;

import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.persistence.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VideoRepository {
    private final VideoMapper videoMapper;
    private final SqlSession sqlSession;

    public Optional<Video> findById(String id) {
        return Optional.ofNullable(videoMapper.selectById(id));
    }

    @SuppressWarnings("UnusedReturnValue")
    public Video save(Video video) {
        Video existVideo = videoMapper.selectById(video.getVideoId());
        if (existVideo == null) {
            videoMapper.insert(video);
        } else {
            videoMapper.updateById(video);
        }

        sqlSession.flushStatements();
        return videoMapper.selectById(video.getVideoId());
    }

}
