package io.zoooohs.realworld.domain.tag.service;

import io.zoooohs.realworld.domain.tag.entity.ArticleTagRelationEntity;
import io.zoooohs.realworld.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<String> listOfTags() {
        return tagRepository.findAll().stream().map(ArticleTagRelationEntity::getTag).distinct().collect(Collectors.toList());
    }
}
