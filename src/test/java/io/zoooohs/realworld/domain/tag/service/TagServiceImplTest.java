package io.zoooohs.realworld.domain.tag.service;

import io.zoooohs.realworld.domain.tag.entity.ArticleTagRelationEntity;
import io.zoooohs.realworld.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {
    TagServiceImpl tagService;

    @Mock
    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    void whenThereAreTags_thenReturnAllTags() {
        when(tagRepository.findAll()).thenReturn(List.of(ArticleTagRelationEntity.builder().tag("a").build()));

        List<String> actual = tagService.listOfTags();

        assertTrue(actual.size() > 0);
    }
}
