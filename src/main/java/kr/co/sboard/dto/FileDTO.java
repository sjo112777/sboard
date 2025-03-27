package kr.co.sboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {
    private int fno;
    private int ano;
    private String oName;
    private String sName;
    private int download;
    private String rdate;

    // 추가 필드
    private String contentType;
    private Resource resource;
}