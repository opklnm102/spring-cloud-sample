package me.dong.aws.s3.controller;

import me.dong.aws.s3.service.StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FileV1Controller test")
@WebMvcTest(FileV1Controller.class)
class FileV1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @Captor
    private ArgumentCaptor<List<MultipartFile>> argumentCaptor;

    @Nested
    @DisplayName("upload")
    class Upload {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given

            // when & then
            mockMvc.perform(multipart("/v1/files/upload")
                           .file(new MockMultipartFile("files", "test1.txt", "plain/text", "aaa".getBytes()))
                           .file(new MockMultipartFile("files", "test2.txt", "plain/text", "bbb".getBytes()))
                           .contentType(MediaType.MULTIPART_FORM_DATA))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.result").value("SUCCESS"))
                   .andExpect(jsonPath("$.data").doesNotExist())
                   .andExpect(jsonPath("$.error").doesNotExist())
                   .andDo(print());

            verify(storageService, times(1)).upload(argumentCaptor.capture());
            assertThat(argumentCaptor.getValue()).hasSize(2);
        }
    }
}
