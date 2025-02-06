package com.namusd.jwtredis.model.entity.video;

/**
 * @apiNote 동영상 편집의 상태를 기록하는 상태값
 * @author yhkim
 */
public enum VideoStatus {

    // 동영상을 유저가 등록한 순간 ~ 타임라인 추출이 완료될 때까지의 상태
    REGISTERED,
    // 동영상의 타임라인이 추출된 상태로
    // 동영상에 대한 편집을 시작할 수 있음.
    READY,
    // 동영상 편집을 완료한 상태
    COMPLETE
}
