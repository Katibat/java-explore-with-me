package ru.practicum.explorewithme.service.admin;

/**
 * Реализация API для работы с отзывами на события на уровне администратора
 */

public interface CommentAdminService {

    /**
     * Удалить отзыв на событие пользователя, не соответсвующий правилам сообщества
     * @param commentId идентификатор отзыва на событие
     */
    void delete(Long commentId);

    /**
     * Удалить все отзывы по идентификатору события
     * @param eventId идентификатор события
     */
    void deleteAllCommentsByEvent(Long eventId);
}