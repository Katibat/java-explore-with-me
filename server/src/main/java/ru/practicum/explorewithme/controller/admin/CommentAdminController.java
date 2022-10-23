package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.comment.Comment;
import ru.practicum.explorewithme.service.admin.CommentAdminService;

import java.util.List;

/**
 * API для работы с отзывами на события на уровне администратора
 */

@RestController
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentAdminService service;

    @DeleteMapping("/admin/comments/{commentId}") // удалить отзыв на событие
    public void delete(@PathVariable Long commentId) {
        service.deleteById(commentId);
    }

    @DeleteMapping ("/admin/events/{eventId}/comments")// удалить все отзывы по идентификатору события
    public void deleteAllCommentsByEvent(@PathVariable Long eventId) {
        service.deleteAllCommentsByEvent(eventId);
    }

    @GetMapping("/admin/events/{eventId}/comments") // получить все отзывы по идентификатору события
    public List<Comment> findAllCommentsByEventId(@PathVariable Long eventId) {
        return service.findAllCommentsByEventId(eventId);
    }
}