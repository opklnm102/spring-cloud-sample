package me.dong.awss3;

/**
 * Created by huekim on 2019. 11. 11..
 */
public class AwsS3Downloader {


}


/*
    @Async
    public Future<File> download(Link link) throws Exception {
        File destFile = File.createTempFile("content-", ".gz", new File("."));
        if (destFile.getFreeSpace() < 5 * GB) { // Freespace 5GB
            throw new IOException("디스크 여유 공간이 부족합니다. 최소 5기가가 필요합니다: 여유공간 - " + (destFile.getFreeSpace() / (GB * 1.0d)) + "GB");
        }

        log.info("다운로드 시작 : {} from {}", destFile.getAbsolutePath(), link.getHref());
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(findAmazonRegion(link.getHref())).build();
        TransferManager xfer_mgr = TransferManagerBuilder.standard()
                                                         .withS3Client(amazonS3)
                                                         .withMultipartCopyPartSize(300 * MB)   // 분할 크기
                                                         .withMultipartCopyThreshold(GB) // 1GB 이상이면 분할 다운로드
                                                         .build();
        try {
            PresignedUrlDownloadRequest request = new PresignedUrlDownloadRequest(new URL(link.getHref()));
            PresignedUrlDownload download = xfer_mgr.download(request, destFile);
            download.waitForCompletion();
            log.info("다운로드 완료 : {} ({}GB)", destFile.getAbsolutePath(), (destFile.length() / (GB * 1.0d)));
        } finally {
            xfer_mgr.shutdownNow(true);
        }

        return new AsyncResult<>(destFile);

*/

