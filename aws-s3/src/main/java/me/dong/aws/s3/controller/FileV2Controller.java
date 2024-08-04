package me.dong.aws.s3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.response.ApiResponse;
import me.dong.aws.s3.service.MultipartStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v2/files")
@Slf4j
public class FileV2Controller {
    private final MultipartStorageService multipartStorageService;

    @PostMapping(value = "/upload")
    public ApiResponse<?> upload(@RequestParam("files") List<MultipartFile> files) {
//        TODO: @RequestPart 로 수정, @RequestParam을 써야하는지?
        multipartStorageService.upload(files);
        return ApiResponse.success();
    }

    @GetMapping(value = "/download/{downloadKey}")
    public ResponseEntity<Resource> download(@PathVariable("downloadKey") String downloadKey) {
        var result = multipartStorageService.download(downloadKey);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(result.left()).build().toString())
                             .body(result.right());
    }

    @GetMapping(value = "/download/{downloadKey}/async")
    public DeferredResult<ResponseEntity<Resource>> downloadAsync(@PathVariable("downloadKey") String downloadKey) {
        var deferredResult = new DeferredResult<ResponseEntity<Resource>>(300_000L);
        multipartStorageService.downloadAsync(downloadKey)
                               .thenAccept(result ->
                                       deferredResult.setResult(ResponseEntity.ok()
                                                                              .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                                                              .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(result.left()).build().toString())
                                                                              .body(result.right()))
                               )
                               .exceptionally(throwable -> {
                                   deferredResult.setErrorResult(throwable.getCause());
                                   return null;
                               });

        return deferredResult;
    }
}
