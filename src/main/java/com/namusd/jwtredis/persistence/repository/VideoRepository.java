package com.namusd.jwtredis.persistence.repository;

import com.namusd.jwtredis.model.domain.PageRequest;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.persistence.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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

    public List<Video> findVideoPage(PageRequest page, User loginUser) {
        return videoMapper.findVideoPage(page, loginUser);
    }

    public int countAllVideos(User loginUser) {
        return videoMapper.countAll(loginUser);
    }

}
