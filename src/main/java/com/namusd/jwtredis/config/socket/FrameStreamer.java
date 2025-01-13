package com.namusd.jwtredis.config.socket;

import javax.websocket.Session;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FrameStreamer {

    public void streamFrames(FrameRequest request, Session session) {
        int start = request.getStart();
        int end = request.getEnd();

        // 1. 입력 값 검증
        if (start < 0 || end <= start) {
            sendMessage(session, "Invalid range: start=" + start + ", end=" + end);
            return;
        }

        // 2. 프레임 처리 (프레임 생성 및 전송)
        for (int i = start; i <= end; i++) {
            try {
                // 이미지 파일 로드/생성 (예: "frame_<i>.jpg" 형식)
                StringBuilder number = new StringBuilder();
                int numberOfIndex = Integer.toString(i).length();
                for (int j = 0; j <= 3 - numberOfIndex; j++) {
                    number.append("0");
                }
                number.append(i);

                String framePath = "frames/frame_" + number + ".jpg";
                // InputStream으로 리소스를 읽음
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(framePath);
                byte[] frameData;
                if (inputStream != null) {
                    frameData = inputStream.readAllBytes();
                    inputStream.close();
                } else {
                    throw new FileNotFoundException("File not found: " + framePath);
                }
                // Base64 인코딩 후 전송
                String base64Frame = Base64.getEncoder().encodeToString(frameData);
                sendMessage(session, base64Frame);

                // 지연 처리 (옵션, 클라이언트가 동영상처럼 보이게 하려면 딜레이 추가)
                Thread.sleep(100);
            } catch (Exception e) {
                sendMessage(session, "Error processing frame: " + i);
            }
        }
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
