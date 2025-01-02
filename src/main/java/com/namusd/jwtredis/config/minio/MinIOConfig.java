package com.namusd.jwtredis.config.minio;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class MinIOConfig {
    final Environment env;

    @Bean
    public MinioClient minioClient() {
        System.out.println("minio-endpoint: "+env.getProperty("minio.endpoint", "http://localhost:9000"));
        return MinioClient.builder()
                .endpoint(env.getProperty("minio.endpoint", "http://localhost:9000"))
                .credentials(
                        env.getProperty("minio.accessKey", "admin"),
                        env.getProperty("minio.secretKey", "admin123")
                )
                .build();
    }

}
