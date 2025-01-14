package com.namusd.jwtredis.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoTest {

    @GetMapping("/stream/test")
    public String test() {
        return "frame_stream_test";
    }
}
