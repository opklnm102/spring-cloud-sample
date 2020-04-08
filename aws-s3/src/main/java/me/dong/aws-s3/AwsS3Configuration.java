package me.dong.servicediscovery;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huekim on 2019. 11. 06..
 */
@Configuration
public class AwsS3Configuration {

    //    @Bean
//    public AmazonS3Client amazonS3Client() {
//        AmazonS3 amazonS3 = AmazonS3ClientBuilder
//                .standard()
////                .withCredentials()
//                .withRegion(Regions.AP_NORTHEAST_1)
//                .build();
//    }
}
