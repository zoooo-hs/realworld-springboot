package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.dto.CommentDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.entity.CommentEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.article.repository.CommentRepository;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto addCommentsToAnArticle(String slug, CommentDto comment, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = CommentEntity.builder()
                .body(comment.getBody())
                .author(UserEntity.builder()
                        .id(authUser.getId())
                        .name(authUser.getName())
                        .bio(authUser.getBio())
                        .image(authUser.getImage())
                        .build())
                .article(articleEntity)
                .build();
        commentRepository.save(commentEntity);

        return CommentDto.builder()
                .id(commentEntity.getId())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .body(commentEntity.getBody())
                .author(ArticleDto.Author.builder()
                        .name(commentEntity.getAuthor().getName())
                        .bio(commentEntity.getAuthor().getBio())
                        .image(commentEntity.getArticle().getAuthor().getImage())
                        .following(false)
                        .build())
                .build();
    }
}
