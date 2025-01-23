package com.namusd.jwtredis.util;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
//import org.apache.tika.*

public class VideoUtil {
    public static double getVideoDuration(MultipartFile file) {
        Parser parser = new MP4Parser();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        // 파서 설정
        context.set(Parser.class, parser);

        try (InputStream inputStream = file.getInputStream()) {
            parser.parse(inputStream, new BodyContentHandler(), metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException(e);
        }
        String duration = metadata.get("xmpDM:duration");
        return duration != null ? Double.parseDouble(duration) : 0;
    }
}
