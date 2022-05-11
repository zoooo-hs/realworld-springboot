package io.zoooohs.realworld.domain.article.controller;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.servie.ArticleService;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticleService articleService;

    @PostMapping
    public ArticleDto.SingleArticle createArticle(@Valid @RequestBody ArticleDto.SingleArticle article, @AuthenticationPrincipal UserDto.Auth authUser) {
       return ArticleDto.SingleArticle.builder().article(articleService.createArticle(article.getArticle(), authUser)).build();
    }
}
