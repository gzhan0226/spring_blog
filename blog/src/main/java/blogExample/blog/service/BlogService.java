package blogExample.blog.service;

import blogExample.blog.domain.Article;
import blogExample.blog.dto.AddArticleRequestDto;
import blogExample.blog.dto.UpdateArticleRequestDto;
import blogExample.blog.repository.BlogRepository;
import jakarta.transaction.TransactionScoped;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;

    @Transactional
    public Article save (AddArticleRequestDto request) {
        return blogRepository.save(request.toEntity());
    }

    @Transactional
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Not Found :" + id));
    }

    @Transactional
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequestDto request) {
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Not Founbd : " + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }

}
