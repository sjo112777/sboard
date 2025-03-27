package kr.co.sboard.service;

import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.entity.File;
import kr.co.sboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public void save(FileDTO fileDTO) {
        File file = modelMapper.map(fileDTO, File.class);
        fileRepository.save(file);
    }

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public List<FileDTO> uploadFile(ArticleDTO articleDTO) {
        // 파일 업로드 디렉터리 객체 생성
        java.io.File fileUploadDir = new java.io.File(uploadDir);

        if(!fileUploadDir.exists()){
            // 파일 업로드 디렉터리가 존재하지 않으면 생성
            fileUploadDir.mkdirs();
        }

        // 파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();
        log.info("fileUploadPath : {}", fileUploadPath);

        // 파일 정보 객체 가져오기
        List<MultipartFile> multipartFiles = articleDTO.getMultipartFiles();

        // 업로드 파일 정보 리스트 생성(반환용)
        List<FileDTO> fileDTOList = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){

            // 파일 첨부 했으면
            if(!multipartFile.isEmpty()){

                String oName = multipartFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String sName = UUID.randomUUID().toString() + ext;

                // 파일 저장
                try {
                    multipartFile.transferTo(new java.io.File(fileUploadPath, sName));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }

                // 반환용 객체 생성
                FileDTO fileDTO = FileDTO.builder()
                        .oName(oName)
                        .sName(sName)
                        .build();

                fileDTOList.add(fileDTO);
            }
        }
        return fileDTOList;
    }

    public ResponseEntity downloadFile(int fno){

        Optional<File> optFile = fileRepository.findById(fno);

        File file = null;

        if(optFile.isPresent()){
            file = optFile.get();
        }

        try{
            Path path = Paths.get(uploadDir + java.io.File.separator + file.getSName());
            String contentType = Files.probeContentType(path);

            // 헤더정보
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(file.getOName(), StandardCharsets.UTF_8)
                            .build());

            headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            // 파일 다운로드 스트림 작업
            Resource resource = new InputStreamResource(Files.newInputStream(path));


            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(resource);

        }catch (Exception e){
            log.error(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }


}