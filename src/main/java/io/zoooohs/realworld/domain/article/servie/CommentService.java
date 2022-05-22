package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.CommentDto;
import io.zoooohs.realworld.domain.user.dto.UserDto;

import java.util.List;

public interface CommentService {
    CommentDto addCommentsToAnArticle(final String slug, final CommentDto comment, final UserDto.Auth authUser);

    void delete(final String slug, final Long commentId, final UserDto.Auth authUser);

    List<CommentDto> getCommentsBySlug(final String slug, final UserDto.Auth authUser);
}
