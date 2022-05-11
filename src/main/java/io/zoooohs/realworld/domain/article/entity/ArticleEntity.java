package io.zoooohs.realworld.domain.article.entity;

import io.zoooohs.realworld.domain.common.entity.BaseEntity;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "articles")
@NamedEntityGraph(name = "fetch-author", attributeNodes = {@NamedAttributeNode("author")})
public class ArticleEntity extends BaseEntity {
    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserEntity author;

    @Builder
    public ArticleEntity(Long id, String slug, String title, String description, String body, UserEntity author) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
    }

    // TODO: need another relation model
//    private List<String> tagList;
//    private Boolean favorited;
//    private Long favoritesCount;

}
