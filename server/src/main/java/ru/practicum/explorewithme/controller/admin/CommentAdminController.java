package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.admin.CommentAdminService;

/**
 * API для работы с отзывами на события на уровне администратора
 */

@RestController
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentAdminService service;

    @DeleteMapping("/admin/comments/{commentId}") // удалить отзыв на событие
    public void delete(@PathVariable Long commentId) {
        service.delete(commentId);
    }

    @DeleteMapping ("/admin/events/{eventId}")// удалить все отзывы по идентификатору события
    public void deleteAllCommentsByEvent(@PathVariable Long eventId) {
        service.deleteAllCommentsByEvent(eventId);
    }
}