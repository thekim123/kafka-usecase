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
        return MinioClient.builder()
                .endpoint(env.getProperty("spring.minio.endpoint", "http://localhost:9000"))
                .credentials(
                        env.getProperty("spring.minio.accessKey", "admin"),
                        env.getProperty("spring.minio.secretKey", "admin123")
                )
                .build();
    }

}
