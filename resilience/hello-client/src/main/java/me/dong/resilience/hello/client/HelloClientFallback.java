package me.dong.resilience.hello.client;

import org.springframework.stereotype.Component;

@Component
public class HelloClientFallback implements HelloClient {

    @Override
    public String hello() {
        return "hello fallback";
    }

    @Override
    public String heavyHello() {
        return "heavyHello fallback";
    }

    @Override
    public String errorHello() {
        return "errorHello fallback";
    }
}
