package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.CommentDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.entity.CommentEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.article.repository.CommentRepository;
import io.zoooohs.realworld.domain.common.entity.BaseEntity;
import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.profile.service.ProfileService;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.exception.Error;
import io.zoooohs.realworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ProfileService profileService;

    @Transactional
    @Override
    public CommentDto addCommentsToAnArticle(String slug, CommentDto comment, AuthUserDetails authUserDetails) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = CommentEntity.builder()
                .body(comment.getBody())
                .author(UserEntity.builder()
                        .id(authUserDetails.getId())
                        .build())
                .article(articleEntity)
                .build();
        commentRepository.save(commentEntity);

        return convertToDTO(authUserDetails, commentEntity);
    }

    @Transactional
    @Override
    public void delete(String slug, Long commentId, AuthUserDetails authUserDetails) {
        Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(comment -> comment.getArticle().getId().equals(articleId))
                .orElseThrow(() -> new AppException(Error.COMMENT_NOT_FOUND));

        commentRepository.delete(commentEntity);
    }

    @Override
    public List<CommentDto> getCommentsBySlug(String slug, AuthUserDetails authUserDetails) {
        Long articleId = articleRepository.findBySlug(slug).map(BaseEntity::getId).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        List<CommentEntity> commentEntities = commentRepository.findByArticleId(articleId);
        return commentEntities.stream().map(commentEntity -> convertToDTO(authUserDetails, commentEntity)).collect(Collectors.toList());
    }

    private CommentDto convertToDTO(AuthUserDetails authUserDetails, CommentEntity commentEntity) {
        ProfileDto author = profileService.getProfileByUserId(commentEntity.getAuthor().getId(), authUserDetails);
        return CommentDto.builder()
                .id(commentEntity.getId())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .body(commentEntity.getBody())
                .author(author)
                .build();
    }
}
