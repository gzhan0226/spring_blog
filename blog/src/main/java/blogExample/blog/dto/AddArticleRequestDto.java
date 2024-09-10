package blogExample.blog.dto;

import blogExample.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequestDto {
    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder().title(title).content(content).build();
    }
}
