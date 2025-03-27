package kr.co.sboard.controller;

import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FileController {


    private final FileService fileService;

    @GetMapping("/file/download")
    public ResponseEntity download(int fno) {
/*
        // 파일 조회
        FileDTO fileDTO = fileService.findById(fno);

        // 파일 자원 객체 생성
        fileService.downloadFile(fileDTO);

        // 헤더정보
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(fileDTO.getOName(), StandardCharsets.UTF_8)
                        .build());

        //headers.add(HttpHeaders.CONTENT_TYPE, contentType);




        return null;
  */
        return fileService.downloadFile(fno);
    }


}