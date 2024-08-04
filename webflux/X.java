https://honinbo-world.tistory.com/97
이런식으로 WebFlux app 구현
@Service
public class ReactiveFileService {
    ....
            ....
            ....

    public Mono<String> uploadExcelFile(String filePath) throws IOException {
        return Mono.fromCallable(() -> {
                    logger.info("xlsToCsv 실행 쓰레드 : " + Thread.currentThread().getName());
                    return xlsToCsv(filePath);
                })
                .map(file -> {
                    logger.info("csvUpload 실행 쓰레드 : " + Thread.currentThread().getName());
                    return csvUpload(file);
                })
                .doOnNext(key -> {
                    logger.info("executeDb 실행 쓰레드 : " + Thread.currentThread().getName());
                    executeDb(key);
                })
                .subscribeOn(Schedulers.elastic())
                .switchIfEmpty(Mono.error(new RuntimeException("mono error")))
                .doOnError(e -> logger.info(e.getMessage()));
    }

    private File xlsToCsv(String filePath) throws IOException {
        File excelFile = new File(filePath);
        InputStream is = new BufferedInputStream(new FileInputStream(excelFile));
        return ConvertUtils.xlsToCsv(is, "test_serviceKey");
    }

    private String csvUpload(File csvFile) {
        String key = path + csvFile.getName();
        awss3Util.upload(bucketName, key, csvFile);

        return key;
    }

    private void executeDb(String key) {
        String tableName = "tbl_excel_upload_ssi_test";
        String csvPath = bucketName + "/" + key;

        fileMapper.createUserListTable(tableName);
        fileMapper.copyTableFromCsvUrl(tableName+"(member_id, phone_number)", csvPath);
    }
}