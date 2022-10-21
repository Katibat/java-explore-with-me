package ru.practicum.explorewithme.service.pub.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.service.pub.CommentPublicService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPublicServiceImpl implements CommentPublicService {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    @Override
    public List<CommentDto> findAll(int from, int size) {
        log.info("PublicCategoriesService: Получение списка отзывов на события, где from={} и size={}.", from, size);
        return repository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findCommentById(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("CommentPublicService: Не найден отзыв на событие с id=" + commentId));
        return mapper.toDto(comment);
    }
}