package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.user.dto.UserDto;

public interface ArticleService {
    ArticleDto createArticle(final ArticleDto article, final UserDto.Auth authUser);
}
