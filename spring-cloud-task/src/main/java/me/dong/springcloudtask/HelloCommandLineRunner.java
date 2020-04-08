package me.dong.springcloudtask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by huekim on 2020. 04. 08..
 */
@Component
public class HelloCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello");
    }
}
