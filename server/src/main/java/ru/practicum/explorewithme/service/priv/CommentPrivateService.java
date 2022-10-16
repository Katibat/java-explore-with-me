package ru.practicum.explorewithme.service.priv;

import ru.practicum.explorewithme.dto.comment.CommentCreateDto;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentUpdateDto;

import java.util.List;

/**
 * Реализация закрытого API для работы с отзывами на события
 * (для авторизованных пользователей)
 */

public interface CommentPrivateService {

    /**
     * Сохранить отзыв на событие
     * @param commentCreateDto объект отзыва на событие
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @return CommentDto
     */
    CommentDto save(CommentCreateDto commentCreateDto, Long userId, Long eventId);

    /**
     * Редактировать отзыв на событие его автором
     * @param commentUpdateDto объект измененного отзыва
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @return CommentDto
     */
    CommentDto update(CommentUpdateDto commentUpdateDto, Long userId, Long eventId);

    /**
     * Удалить отзыв на событие его автором
     * @param userId идентификатор автора отзыва
     * @param eventId идентификатор события
     * @param commentId идентификатор отзыва
     */
    void delete(Long userId, Long eventId, Long commentId);

    /**
     * Получить список отзывов по идентификатору события
     * @param userId идентификатор автора отзыва
     * @param eventId идентификатор события
     * @return List<CommentDto>
     */
    List<CommentDto> findCommentsByEventId(Long userId, Long eventId);
}