package com.namusd.jwtredis.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.namusd.jwtredis.model.entity.video.OriginalFrame;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OriginalFrameMapper extends BaseMapper<OriginalFrame> {
}
