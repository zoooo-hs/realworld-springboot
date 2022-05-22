package io.zoooohs.realworld.domain.article.controller;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.dto.CommentDto;
import io.zoooohs.realworld.domain.article.model.ArticleQueryParam;
import io.zoooohs.realworld.domain.article.model.FeedParams;
import io.zoooohs.realworld.domain.article.servie.ArticleService;
import io.zoooohs.realworld.domain.article.servie.CommentService;
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
    private final CommentService commentService;

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

    @GetMapping("/feed")
    public ArticleDto.MultipleArticle feedArticles(@ModelAttribute @Valid FeedParams feedParams, @AuthenticationPrincipal UserDto.Auth authUser) {
       return ArticleDto.MultipleArticle.builder().articles(articleService.feedArticles(authUser, feedParams)).build();
    }

    @PostMapping("/{slug}/favorite")
    public ArticleDto.SingleArticle<ArticleDto> favoriteArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        return new ArticleDto.SingleArticle<>(articleService.favoriteArticle(slug, authUser));
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleDto.SingleArticle<ArticleDto> unfavoriteArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        return new ArticleDto.SingleArticle<>(articleService.unfavoriteArticle(slug, authUser));
    }

    @GetMapping
    public ArticleDto.MultipleArticle listArticles(@ModelAttribute ArticleQueryParam articleQueryParam, @AuthenticationPrincipal UserDto.Auth authUser) {
        return ArticleDto.MultipleArticle.builder().articles(articleService.listArticle(articleQueryParam, authUser)).build();
    }

    @PostMapping("/{slug}/comments")
    public CommentDto.SingleComment addCommentsToAnArticle(@PathVariable String slug, @RequestBody @Valid CommentDto.SingleComment comment, @AuthenticationPrincipal UserDto.Auth authUser) {
        return CommentDto.SingleComment.builder()
                .comment(commentService.addCommentsToAnArticle(slug, comment.getComment(), authUser))
                .build();
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public void deleteComment(@PathVariable("slug") String slug, @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDto.Auth authUser) {
        commentService.delete(slug, commentId, authUser);
    }

    @GetMapping("/{slug}/comments")
    public CommentDto.MultipleComments getCommentsFromAnArticle(@PathVariable String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        return CommentDto.MultipleComments.builder()
                .comments(commentService.getCommentsBySlug(slug, authUser))
                .build();
    }
}
