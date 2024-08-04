package me.dong.resilience.hello.client;

import org.springframework.web.bind.annotation.GetMapping;

public interface HelloService {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/heavy-hello")
    String heavyHello();

    @GetMapping("/error-hello")
    String errorHello();
}
