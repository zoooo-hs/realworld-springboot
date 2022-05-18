package io.zoooohs.realworld.domain.article.entity;

import io.zoooohs.realworld.domain.common.entity.BaseEntity;
import io.zoooohs.realworld.domain.tag.entity.ArticleTagRelationEntity;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "articles")
@NamedEntityGraph(name = "fetch-author-tagList", attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("tagList")})
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

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ArticleTagRelationEntity> tagList;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FavoriteEntity> favoriteList;

    @Builder
    public ArticleEntity(Long id, String slug, String title, String description, String body, UserEntity author) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
        this.tagList = new ArrayList<>();
    }
}
