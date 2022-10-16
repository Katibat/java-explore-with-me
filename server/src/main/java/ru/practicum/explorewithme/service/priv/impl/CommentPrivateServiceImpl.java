package ru.practicum.explorewithme.service.priv.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentCreateDto;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentUpdateDto;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.service.priv.CommentPrivateService;
import ru.practicum.explorewithme.service.priv.RequestPrivateService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final RequestPrivateService requestPrivateService;

    @Override
    @Transactional
    public CommentDto save(CommentCreateDto commentCreateDto, Long userId, Long eventId) {
        Event event = requestPrivateService.findEventById(eventId);
        User user = requestPrivateService.findUserById(userId);
        Comment comment = mapper.toNewModel(commentCreateDto, user, event);
        Comment saved = repository.save(comment);
        log.info("Сохранен отзыв с id={} на событие с id={}.", saved.getId(), eventId);
        return mapper.toDto(saved);
    }

    @Transactional
    @Override
    public CommentDto update(CommentUpdateDto commentUpdateDto, Long userId, Long eventId) {
        Comment comment = findCommentById(commentUpdateDto.getId());
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new ForbiddenException("CommentPrivateService: Для обновления отзыва неверно указан идентификатор " +
                    "события id=" + eventId);
        }
        isAuthor(comment, userId);
        if (commentUpdateDto.getText() != null) {
            comment.setText(commentUpdateDto.getText());
        }
        comment.setEdited(true);
        Comment updated = repository.save(comment);
        log.info("CommentPrivateService: Обновлен отзыв c id={} на событие c id={}.", updated.getId(), eventId);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long userId, Long eventId, Long commentId) {
        Comment comment = findCommentById(commentId);
        isAuthor(comment, userId);
        repository.delete(comment);
        log.info("CommentPrivateService: Удален отзыв c id={} на событие c id={}.", commentId, eventId);

    }

    @Override
    public List<CommentDto> findCommentsByEventId(Long userId, Long eventId) {
        Event event = requestPrivateService.findEventById(eventId);
        isInitiator(event, userId);
        log.info("CommentPrivateService: Запрошен список отзывов автором с id={}.", userId);
        return repository.findAllCommentsByEventId(eventId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Проверить является ли пользователь автором отзыва на событие
     * если нет, то вернуть ForbiddenException
     * @param comment  объект отзыва на событие
     * @param userId идентификатор проверяемого пользователя
     */
    private void isAuthor(Comment comment, Long userId) {
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ForbiddenException("CommentPrivateService: Попытка не автором внести изменения или получить " +
                    "информацию об отзыве на событие с id=" + comment.getId());
        }
    }

    /**
     * Проверить является ли пользователь инициатором события
     * если нет, то вернуть ForbiddenException
     * @param event объект события
     * @param userId идентификатор проверяемого пользователя
     */
    private void isInitiator(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("CommentPrivateService: Попытка не инициатором внести изменения или получить " +
                    "информацию об отзыве на событие с id=" + event.getId());
        }
    }

    /**
     * Найти в репозитории отзыв на событие по идентификатору
     * @param commentId идентификатор отзыва на событие
     * @return Comment
     */
    private Comment findCommentById(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("CommentPrivateService: Не найден отзыв на событие с id=" + commentId));
    }
}