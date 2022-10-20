package ru.practicum.explorewithme.service.pub;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;

/**
 * Реализация публичного API для просмотра отзывов на события
 */

public interface CommentPublicService {

    /**
     * Получить событие со всеми отзывами
     * @param eventId идентификатор события
     * @return EventFullDto
     */
    EventFullDto getEventWithAllComments(Long eventId);

    /**
     * Получить отзыв на событие по идентификатору
     * @param eventId идентификатор события
     * @param commentId идентификатор отзыва на событие
     * @return CommentDto
     */
    CommentDto findCommentById(Long eventId, Long commentId);
}