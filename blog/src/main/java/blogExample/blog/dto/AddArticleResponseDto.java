package blogExample.blog.dto;

import blogExample.blog.domain.Article;
import lombok.Getter;

@Getter
public class AddArticleResponseDto {
    private final Long id;
    private final String title;
    private final String content;

    public AddArticleResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }

}
