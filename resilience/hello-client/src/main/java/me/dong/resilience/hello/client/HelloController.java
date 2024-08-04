package me.dong.resilience.hello.client;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloService {

    private final HelloClient helloClient;

    public HelloController(HelloClient helloClient) {
        this.helloClient = helloClient;
    }

    @Override
    public String hello() {
        return helloClient.hello();
    }

    @Override
    public String heavyHello() {
        return helloClient.heavyHello();
    }

    @Override
    public String errorHello() {
        return helloClient.errorHello();
    }
}
