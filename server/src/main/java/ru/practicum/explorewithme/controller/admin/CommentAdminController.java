package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.admin.CommentAdminService;

/**
 * API для работы с отзывами на события на уровне администратора
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentAdminController {
    private final CommentAdminService service;

    @DeleteMapping("{commentId}") // удалить отзыв на событие
    public void delete(@PathVariable Long commentId) {
        service.delete(commentId);
    }

    @DeleteMapping // удалить все отзывы по идентификатору события
    public void deleteAllCommentsByEvent(@PathVariable Long eventId) {
        service.deleteAllCommentsByEvent(eventId);
    }
}