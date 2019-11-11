package me.dong.servicediscovery;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huekim on 2019. 11. 11..
 */
@Configuration
public class UploaderConfiguration {

    @Bean(value = "dh-testing-aws-s3-uploader")
    public FileUploader awsS3Uploader(AmazonS3Client amazonS3Client) {
        return new AwsS3Uploader(amazonS3Client, "testing");
    }
}
