## AWS S3 Sample


## Stream 방식 사용

- 대용량 파일 전송시 client/server network 환경에 따라 속도 편차가 크기 때문에 속도에 대한 충분한 test 필요
- client에게 현황 제공이 불가하므로 client가 기다릴 수 있을 만큼의 적당한 파일 크기 제한 필요
- 중간에 에러 발생시 전체 파일을 다시 처음부터 진행해야하기 때문에 시간과 대역폭이 낭비될 수 있다

## MultipartFile 방식 사용

- [Spring MultipartFile](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html) 사용
- 임시 파일이 Disk에 저장
- 파일 전송 중 배포 or 장애 발생시 임시 파일의 garbage가 발생하여 관리 필요

## S3AsyncClient, S3TransferManager 사용

- AWS SDK의 S3AsyncClient, S3TransferManager를 이용한 stream, multipart 방식에서 다수의 동시 요청이 발생할 경우, long task로 인한 http request thread 소진의 이슈가 있다
    - resilience4j의 bulkhead 패턴을 활용하여 동시에 처리될 수 있는 작업의 최대 수를 제한
    - 별도의 thread pool을 사용(적절한 thread pool 설정 필요)
    - 파일 up/download 기능을 별도의 application으로 분리

## [AWS Multipart upload](https://docs.aws.amazon.com/AmazonS3/latest/userguide/mpuoverview.html)

https://docs.aws.amazon.com/AmazonS3/latest/userguide/example_s3_Scenario_MultipartUpload_section.html

- application을 경유하지 않고 client가 S3에 직접 upload하므로 application의 부하를 고려하지 않아도 된다
- 작은 part로 나누어 upload 후 모든 part가 upload되었다면 하나의 object로 합쳐서 저장
- 몇개의 part가 upload되었는지 확인하여 진행 사항 제공 가능

### Scenario

#### 1. multipart upload 시작

멀티파트 업로드 시작(initiate-upload)을 요청하면 서버는 멀티파트 업로드에 대한 고유 식별자인 Upload ID를 응답합니다. 부분 업로드, 업로드 완료 또는 업로드 중단 요청 시 항상 Upload ID를 포함해야 하기 때문에 클라이언트는 이 값을 잘 저장해야 합니다.

#### 2. presigned URL 발급

업로드를 위한 AWS의 서명된 URL을 발급받는 요청입니다. 멀티파트 시작 요청에서 받은 Upload ID 그리고 PartNumber 값을 함께 요청해야 합니다. PartNumber는 1부터 10,000까지 파트 번호 지정이 필요합니다. AWS에서 파트 번호를 이용하여 업로드하는 객체의 각 부분과 그 위치를 고유하게 식별하기 때문입니다. 파트 번호는
연속적인 시퀀스로 선택할 필요는 없습니다 (예를 들면 1, 5 및 14를 선택해도 됩니다).

만약 이전에 업로드한 부분과 동일한 부분 번호로 새 부분을 업로드할 경우 이전에 업로드한 부분을 덮어쓰게 됩니다.

#### 3. presigned URL part upload

#### 4. multipart upload 완료




## Conclusion
- 최대 20MB의 파일 전송 기능을 구현하면서 AWS Multipart upload로 구현한다면 over engineering
- 기술을 선택할 때 최고의 방식도 좋지만, 상황에 따라 타협적인 방식도 필요

|                       | Stream    | MultipartFile   | AWS Multipart   | 
|:----------------------|:----------|:----------------|:----------------|
| file size limit       | X         | disk/memory에 의존 | 최대 5TB          |
| binary application 경유 | O(buffer) | O(설정에 따라 발생)    | X               |
| 구현 복잡도                | 하         | 중               | 상               |
| 전송 과정 복잡도             | 하         | 중               | 상               |
| AWS S3 의존성            | 하         | 하               | 상               |
| 진행 상태 표시              | X         | X               | O               |
| CORS 설정 필요            | X         | X               | O               |
| 유지 보수                 | X         | O(주기적인 임시파일 정리) | X(S3 Lifecycle) |

https://techblog.woowahan.com/11392/ 의 AWS Multipart 업로드 참고

[AWS S3 Multi-part Upload using AWS CLI in Windows Host machine](https://hrshshh9.medium.com/aws-s3-multi-part-upload-using-aws-cli-in-windows-host-machine-4981a48bc95b)





