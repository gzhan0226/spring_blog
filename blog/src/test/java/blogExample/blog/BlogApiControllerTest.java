package blogExample.blog;

import blogExample.blog.domain.Article;
import blogExample.blog.dto.AddArticleRequestDto;
import blogExample.blog.dto.UpdateArticleRequestDto;
import blogExample.blog.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }
    
    @DisplayName("addArticle: 블로그 글 추가 성공")
    @Test
    public void addArticle() throws Exception {
        String url = "/api/articles";
        String title = "title";
        String content = "content";
        AddArticleRequestDto userRequest = new AddArticleRequestDto(title,content);
        String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles : 전체 글 조회")
    @Test
    public void findAllArticles() throws Exception {
        String url ="/api/articles";
        String title = "title";
        String content = "content";
        blogRepository.save(Article.builder().title(title).content(content).build());
        ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));

    }

    @DisplayName("findArticle : id로 글 조회")
    @Test
    public void findArticle() throws Exception {
        String url ="/api/articles/{id}";
        String title = "title";
        String content = "content";
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());
        ResultActions result = mockMvc.perform(get(url,savedArticle.getId()));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));

    }

    @DisplayName("deleteArticle : id로 글 삭제")
    @Test
    public void deleteArticle() throws Exception {
        String url = "/api/articles/{id}";
        String title = "title";
        String content = "content";
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());
        ResultActions result =  mockMvc.perform(delete(url,savedArticle.getId()));
        result.andExpect(status().isOk());
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.isEmpty()).isTrue();
    }

    @DisplayName("updateArticle : id로 글 수정")
    @Test
    public void updateArticle() throws Exception {
        String url = "/api/articles/{id}";
        String title = "title";
        String content = "content";
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        String newTitle = "수정된 title";
        String newContent = "수정된 content";
        UpdateArticleRequestDto request = new UpdateArticleRequestDto(newTitle,newContent);

        ResultActions result = mockMvc.perform(patch(url,savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        Article article = blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}
