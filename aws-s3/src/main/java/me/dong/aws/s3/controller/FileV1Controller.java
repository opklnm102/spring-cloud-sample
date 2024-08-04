package me.dong.aws.s3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.response.ApiResponse;
import me.dong.aws.s3.service.StorageService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/files")
@Slf4j
public class FileV1Controller {
    private final StorageService storageService;

    @PostMapping(value = "/upload")
    public ApiResponse<?> upload(@RequestParam("files") List<MultipartFile> files) {
        storageService.upload(files);
        return ApiResponse.success();
    }

    @GetMapping(value = "/download/{downloadKey}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable("downloadKey") String downloadKey) {
        var result = storageService.download(downloadKey);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(result.left()).build().toString())
                             .body(result.right());
    }
}
