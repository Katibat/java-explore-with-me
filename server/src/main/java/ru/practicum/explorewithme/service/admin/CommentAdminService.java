package ru.practicum.explorewithme.service.admin;

import ru.practicum.explorewithme.model.comment.Comment;

import java.util.List;

/**
 * Реализация API для работы с отзывами на события на уровне администратора
 */

public interface CommentAdminService {

    /**
     * Удалить отзыв на событие пользователя, не соответсвующий правилам сообщества
     * @param commentId идентификатор отзыва на событие
     */
    void deleteById(Long commentId);

    /**
     * Удалить все отзывы по идентификатору события
     * @param eventId идентификатор события
     */
    void deleteAllCommentsByEvent(Long eventId);

    /**
     * Получить список все отзывов по идентификатору события
     * @param eventId идентификатор события
     * @return List<Comment>
     */
    List<Comment> findAllCommentsByEventId(Long eventId);
}