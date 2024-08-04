package me.dong.resilience.hello.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

// @Configuration 을 설정하면 global하게 적용된다
@Slf4j
public class HelloClientConfiguration {

    // ErrorDecoder -> try-catch -> @ExceptionHander 순서로 처리된다
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {

            @Override
            public Exception decode(String instance, Response response) {
                log.info("decode instance: {}, response: {}", instance, response);
                var responseBody = extractResponseBody(response);
                log.info("decode responseBody: {}", responseBody);

                if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                    return new RuntimeException("bad request" + responseBody);
                }

                return new RuntimeException("unknown error" + responseBody);
            }

            private String extractResponseBody(Response response) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                } catch (IOException e) {
                    throw new RuntimeException("변환 실패");
                }
            }
        };
    }
}
