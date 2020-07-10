package com.firstProject.springBoot.web.dto;

import com.firstProject.springBoot.domain.posts.Posts;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostsListRespoonseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListRespoonseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
