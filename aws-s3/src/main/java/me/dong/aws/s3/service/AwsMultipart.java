package me.dong.aws.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

@Service
@RequiredArgsConstructor
public class AwsMultipart {
    private final S3AsyncClient s3Client;

    public void upload(MultipartFile uploadFile) throws IOException {
        var partSize = 5 * MB;
        var contentLength = uploadFile.getSize();
        var partETags = new ArrayList<>();


        var response = s3Client.createMultipartUpload(builder -> builder.bucket("bucket").key("key")).join();
        var uploadId = response.uploadId();

        var partRequest = UploadPartRequest.builder()
                                           .bucket("bucket")
                                           .key("key")
                                           .uploadId(uploadId)
                                           .partNumber(1)
                                           .build();


        var eTag1 = s3Client.uploadPart(builder -> builder.bucket("bucket")
                                                          .key("key")
                                                          .uploadId(uploadId)
                                                          .partNumber(1),
                                    AsyncRequestBody.fromByteBuffer(getRandomByteBuffer(5 * 1024 * 1024))).join()
                            .eTag();
        var completedPart1 = CompletedPart.builder().partNumber(1).eTag(eTag1).build();

        var eTag2 = s3Client.uploadPart(builder -> builder.bucket("bucket")
                                                          .key("key")
                                                          .uploadId(uploadId)
                                                          .partNumber(2),
                                    AsyncRequestBody.fromByteBuffer(getRandomByteBuffer(3 * 1024 * 1024))).join()
                            .eTag();

        var completedPart2 = CompletedPart.builder().partNumber(2).eTag(eTag2).build();

        s3Client.completeMultipartUpload(builder -> builder.bucket("bucket")
                                                           .key("key")
                                                           .uploadId(uploadId)
                                                           .multipartUpload(CompletedMultipartUpload.builder()
                                                                                                    .parts(List.of(completedPart1, completedPart2))
                                                                                                    .build())).join();
    }

    private static ByteBuffer getRandomByteBuffer(int size) throws IOException {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }

    private String createPutPresignedUrl(String bucketName, String key) {

        UploadPartPresignRequest.builder()
                                .uploadPartRequest(builder -> builder.bucket(bucketName).key(key).uploadId("uploadId").partNumber(1));

        return s3Presigner.presignUploadPart(builder -> builder
                                  .uploadPartRequest(b -> b.bucket(bucketName)
                                                           .key(key)
                                                           .uploadId("uploadId")
                                                           .partNumber(1))
                                  .signatureDuration(Duration.ofMinutes(10)))
                          .url()
                          .toString();

    }

    private final S3Presigner s3Presigner;
}
/*

            // Initiate the multipart upload.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
            InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

            // Upload the file parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Because the last part could be less than 5 MB, adjust the part size as
                // needed.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create the request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(keyName)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload the part and add the response's ETag to our list.
                UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
                partETags.add(uploadResult.getPartETag());

                filePosition += partSize;
            }

            // Complete the multipart upload.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName,
                    initResponse.getUploadId(), partETags);
            s3Client.completeMultipartUpload(compRequest);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        abort
        InitiateMultipartUploadRequest initRequest =
    new InitiateMultipartUploadRequest(existingBucketName, keyName);
InitiateMultipartUploadResult initResponse =
               s3Client.initiateMultipartUpload(initRequest);

AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
            existingBucketName, keyName, initResponse.getUploadId()));
 */
