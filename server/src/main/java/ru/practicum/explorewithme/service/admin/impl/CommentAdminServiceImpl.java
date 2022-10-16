package ru.practicum.explorewithme.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.service.admin.CommentAdminService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository repository;

    @Override
    public void delete(Long commentId) {
        Comment comment = findCommentById(commentId);
        repository.delete(comment);
        log.info("CommentAdminService: Удален отзыв c id={}.", commentId);
    }

    @Override
    public void deleteAllCommentsByEvent(Long eventId) {
        List<Comment> comments = repository.findAllCommentsByEventId(eventId);
        for (Comment c : comments) {
            comments.remove(c);
        }
        log.info("CommentAdminService: Удалены все отзывы на событие c id={}.", eventId);
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