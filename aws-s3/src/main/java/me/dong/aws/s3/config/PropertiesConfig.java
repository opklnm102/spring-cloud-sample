package me.dong.aws.s3.config;

import me.dong.aws.s3.AwsS3Application;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = AwsS3Application.class)
public class PropertiesConfig {
}
