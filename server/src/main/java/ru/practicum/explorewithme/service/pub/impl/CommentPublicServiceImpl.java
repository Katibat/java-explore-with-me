package ru.practicum.explorewithme.service.pub.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.service.priv.RequestPrivateService;
import ru.practicum.explorewithme.service.pub.CommentPublicService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPublicServiceImpl implements CommentPublicService {
    private final CommentRepository repository;
    private final RequestPrivateService requestPrivateService;
    private final CommentMapper mapper;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto getEventWithAllComments(Long eventId) {
        log.info("CommentPrivateService: Запрошено событие с id={} с полным списком отзывов.", eventId);
        Event event = requestPrivateService.findEventById(eventId);
        List<CommentDto> comments = repository.findAllCommentsByEventId(eventId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return eventMapper.toFullDtoWithComments(event, comments);
    }

    @Override
    public CommentDto findCommentById(Long eventId, Long commentId) {
        requestPrivateService.findEventById(eventId);
        Comment comment = repository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("CommentPublicService: Не найден отзыв на событие с id=" + commentId));
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ForbiddenException("CommentPublicService: Неверно указан идентификатор " +
                    "события id=" + eventId);
        }
        return mapper.toDto(comment);
    }
}