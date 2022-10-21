package ru.practicum.explorewithme.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public void delete(Long commentId) {
        repository.deleteById(commentId);
        log.info("CommentAdminService: Удален отзыв c id={}.", commentId);
    }

    @Transactional
    @Override
    public void deleteAllCommentsByEvent(Long eventId) {
        List<Comment> comments = repository.findAllCommentsByEventId(eventId);
        for (Comment c : comments) {
            comments.remove(c);
        }
        log.info("CommentAdminService: Удалены все отзывы на событие c id={}.", eventId);
    }
}