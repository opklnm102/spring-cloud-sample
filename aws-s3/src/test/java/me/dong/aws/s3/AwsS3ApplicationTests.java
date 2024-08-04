package me.dong.aws.s3;

import me.dong.aws.s3.operation.AwsS3TransferManagerDownloader;
import me.dong.aws.s3.operation.AwsS3Uploader;
import me.dong.aws.s3.operation.AwsS3TransferManagerUploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.io.File;

@SpringBootTest
public class AwsS3ApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private S3AsyncClient s3AsyncClient;

	@Autowired
	private S3TransferManager s3TransferManager;

	private AwsS3Uploader sut;

	private AwsS3TransferManagerUploader sut2;

	private AwsS3TransferManagerDownloader sut3;

	@DisplayName("")
	@Test
	void test_() {
	    // given
		var uploadFile = new File("test.txt");
		sut = new AwsS3Uploader(s3AsyncClient);

	    // when
		sut.upload("yanolja-dev-pf-bucket", "partner-payment/test", uploadFile);
	}

	@DisplayName("")
	@Test
	void test_1() {
		sut = new AwsS3Uploader(s3AsyncClient);

		sut.upload("yanolja-dev-pf-bucket", "partner-payment/test", "content-test", "aaabbbccc");
	}

	@DisplayName("")
	@Test
	void test_3() {

		var uploadFile = new File("test.txt");
		sut2 = new AwsS3TransferManagerUploader(s3TransferManager);

		// when
		sut2.upload("yanolja-dev-pf-bucket", "partner-payment/test", uploadFile);
	}

	@DisplayName("")
	@Test
	void test_4() {
		sut2 = new AwsS3TransferManagerUploader(s3TransferManager);

		sut2.upload("yanolja-dev-pf-bucket", "partner-payment/test", "content-test2", "aaabbbccc");
	}



	@DisplayName("")
	@Test
	void test_5() {
		sut3 = new AwsS3TransferManagerDownloader(s3TransferManager);

		var result = sut3.download("yanolja-dev-pf-bucket", "partner-payment/test/content-test2");

		System.out.println(result);

	}

}
