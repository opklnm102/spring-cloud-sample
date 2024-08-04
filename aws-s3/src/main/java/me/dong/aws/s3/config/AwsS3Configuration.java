package me.dong.aws.s3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

/**
 * Created by huekim on 2019. 11. 06..
 */
@Configuration
public class AwsS3Configuration {

    @Bean
    public S3AsyncClient s3AsyncClient(AwsS3Properties awsS3Properties) {
        return S3AsyncClient.crtBuilder()
                            .region(Region.of(awsS3Properties.region()))
                            .credentialsProvider(DefaultCredentialsProvider.create())
                            .targetThroughputInGbps(20.0)
                            .minimumPartSizeInBytes(8 * MB)
                            .build();
    }

    @Bean
    public S3TransferManager transferManager(S3AsyncClient s3AsyncClient) {
        return S3TransferManager.builder()
                                .s3Client(s3AsyncClient)
                                .build();
    }

    @Bean
    public S3Presigner s3Presigner(AwsS3Properties awsS3Properties) {
        return S3Presigner.builder()
                          .region(Region.of(awsS3Properties.region()))
                          .credentialsProvider(DefaultCredentialsProvider.create())
                          .build();
    }
}
