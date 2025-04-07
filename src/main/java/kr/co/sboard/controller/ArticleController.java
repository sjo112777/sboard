package kr.co.sboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.dto.PageResponseDTO;
import kr.co.sboard.service.ArticleService;
import kr.co.sboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;

    @GetMapping("/article/search")
    public String search(PageRequestDTO pageRequestDTO, Model model){
        log.info("pageRequestDTO : {}", pageRequestDTO);

        // 서비스 호출
        PageResponseDTO pageResponseDTO = articleService.searchAll(pageRequestDTO);

        model.addAttribute(pageResponseDTO);

        return "/article/searchList";
    }


    @GetMapping("/article/list")
    public String list(Model model, PageRequestDTO pageRequestDTO){

        // 전체 글 조회 서비스 호출(JPA)
        PageResponseDTO pageResponseDTO = articleService.findAll(pageRequestDTO);

        // 전체 글 조회 서비스 호출(Mybatis)

        model.addAttribute(pageResponseDTO);

        return "/article/list";
    }

    @GetMapping("/article/modify")
    public String modify(){
        return "/article/modify";
    }

    @GetMapping("/article/view")
    public String view(int no, Model model){

        // 글 조회 서비스 호출
        ArticleDTO articleDTO = articleService.findById(no);

        model.addAttribute(articleDTO);

        return "/article/view";
    }

    @GetMapping("/article/write")
    public String write(){
        return "/article/write";
    }

    @PostMapping("/article/write")
    public String write(ArticleDTO articleDTO, HttpServletRequest request){

        String regip = request.getRemoteAddr();
        articleDTO.setRegip(regip);
        log.info("articleDTO : {}", articleDTO);

        // 파일 업로드 서비스 호출
        List<FileDTO> files = fileService.uploadFile(articleDTO);

        // 글 저장 서비스 호출
        articleDTO.setFile(files.size());
        int no = articleService.register(articleDTO);

        // 파일 저장 서비스 호출
        for(FileDTO fileDTO : files) {
            fileDTO.setAno(no);
            fileService.save(fileDTO);
        }

        // 리다이렉트
        return "redirect:/article/list";
    }
}