package me.dong.resilience.hello.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        log.info("hello");
        return "Hello, World!";
    }

    @GetMapping("/heavy-hello")
    public String heavyHello() {
        log.info("heavy hello");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }
        return "Heavy, Hello, World!";
    }

    @GetMapping("/error-hello")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String errorHello() {
        log.info("error hello");
        return "Error, Hello, World!";
    }
}
