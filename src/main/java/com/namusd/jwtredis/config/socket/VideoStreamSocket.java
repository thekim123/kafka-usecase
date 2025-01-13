package com.namusd.jwtredis.config.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint("/video-stream")
public class VideoStreamSocket {

    private final FrameStreamer frameStreamer = new FrameStreamer();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Session opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // 요청 파싱 (JSON 형식 예: {"start":10, "end":20})
            ObjectMapper mapper = new ObjectMapper();
            FrameRequest request = mapper.readValue(message, FrameRequest.class);

            // 프레임 스트리밍 처리
            frameStreamer.streamFrames(request, session);
        } catch (Exception e) {
            try {
                session.getBasicRemote().sendText("Error: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}
