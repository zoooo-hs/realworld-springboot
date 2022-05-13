package io.zoooohs.realworld.domain.article.controller;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.servie.ArticleService;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticleService articleService;

    @PostMapping
    public ArticleDto.SingleArticle<ArticleDto> createArticle(@Valid @RequestBody ArticleDto.SingleArticle<ArticleDto> article, @AuthenticationPrincipal UserDto.Auth authUser) {
        return new ArticleDto.SingleArticle<>(articleService.createArticle(article.getArticle(), authUser));
    }

    @GetMapping("/{slug}")
    public ArticleDto.SingleArticle<ArticleDto> getArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        return new ArticleDto.SingleArticle<>(articleService.getArticle(slug, authUser));
    }

    @PutMapping("/{slug}")
    public ArticleDto.SingleArticle<ArticleDto> createArticle(@PathVariable String slug, @Valid @RequestBody ArticleDto.SingleArticle<ArticleDto.Update> article, @AuthenticationPrincipal UserDto.Auth authUser) {
        return new ArticleDto.SingleArticle<>(articleService.updateArticle(slug, article.getArticle(), authUser));
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        articleService.deleteArticle(slug, authUser);
    }
}
