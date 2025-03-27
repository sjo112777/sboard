package kr.co.sboard.repository;

import jakarta.transaction.Transactional;
import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.entity.Article;
import kr.co.sboard.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;

    @DisplayName("관계 설정 테스트")
    @Transactional
    @Test
    public void test1(){

        // given
        int no = 3;

        // when
        Article article = articleRepository.findById(no).get();

        // then
        System.out.println(article);

    }

    @Test
    @Transactional
    public void test2(){
        ArticleDTO articleDTO = articleService.findById(8);
        System.out.println(articleDTO);

    }

}