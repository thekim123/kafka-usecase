package com.namusd.jwtredis.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.namusd.jwtredis.model.domain.PageRequest;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.model.entity.video.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    int countAll(@Param("loginUser") User loginUser);

    List<Video> findVideoPage(
            @Param("page") PageRequest pageable,
            @Param("loginUser") User loginUser
    );

}
