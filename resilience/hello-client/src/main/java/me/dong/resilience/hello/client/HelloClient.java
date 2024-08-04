package me.dong.resilience.hello.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "HelloClient", url = "http://localhost:8081", fallback = HelloClientFallback.class,
        configuration = {HelloClientConfiguration.class})
public interface HelloClient extends HelloService {
}
