package me.dong.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

// TODO: 2018. 7. 26. circuit breaker tutorial - 이거보고 해보기 https://spring.io/guides/gs/circuit-breaker/

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {

    private static final PropertySourceLoader propertySourceLoader = new PropertiesPropertySourceLoader();

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ServiceDiscoveryApplication.class, args);



		Resource resource = new ClassPathResource("application.properties");

        propertySourceLoader.load("application", resource);
	}
}

/*
https://springbootdev.com/2018/01/15/microservices-service-registration-and-discover-in-netflix-eureka/

eureka 여기보고 해보고
문서보면서 자세히 살펴보자
그리고 설명같은거 정리..!


bootstrap.yml 의 역할은?


UI에 보이는 형식

{host}:{service-id}:{port}
ex. ip-172-31-7-248.ap-southeast-1.compute.internal:BALANCE.API:7080

 */