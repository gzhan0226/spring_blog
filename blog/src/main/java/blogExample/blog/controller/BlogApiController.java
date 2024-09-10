package blogExample.blog.controller;

import blogExample.blog.domain.Article;
import blogExample.blog.dto.AddArticleRequestDto;
import blogExample.blog.dto.AddArticleResponseDto;
import blogExample.blog.dto.ArticleResponse;
import blogExample.blog.dto.UpdateArticleRequestDto;
import blogExample.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity <AddArticleResponseDto> addArticle(
            @RequestBody AddArticleRequestDto addArticleRequestDto) {
        Article saveArticle = blogService.save(addArticleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AddArticleResponseDto(saveArticle));
    }

    @GetMapping("/api/articles")
    public ResponseEntity <List<AddArticleResponseDto>> findAllArticles () {
        List<AddArticleResponseDto> articlesCollect = blogService.findAll()
                .stream()
                .map(AddArticleResponseDto::new)
                .toList();
        return ResponseEntity.ok().body(articlesCollect);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle (@PathVariable("id") Long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
        blogService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<AddArticleResponseDto> updateArticle(@PathVariable("id") Long id
            , @RequestBody UpdateArticleRequestDto request) {
        Article updateArticle = blogService.update(id,request);
        return ResponseEntity.ok().body(new AddArticleResponseDto(updateArticle));
    }
}
