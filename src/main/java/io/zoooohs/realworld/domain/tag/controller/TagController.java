package io.zoooohs.realworld.domain.tag.controller;

import io.zoooohs.realworld.domain.tag.dto.TagDto;
import io.zoooohs.realworld.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public TagDto.TagList listOfTags() {
        return TagDto.TagList.builder().tags(tagService.listOfTags()).build();
    }
}
