package ru.practicum.explorewithme.service.pub;

import ru.practicum.explorewithme.dto.comment.CommentDto;

import java.util.List;

/**
 * Реализация публичного API для просмотра отзывов на события
 */

public interface CommentPublicService {

    /**
     * Получить список отзывов на события по параметрам
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<CommentDto>
     */
    List<CommentDto> findAll(int from, int size);

    /**
     * Получить отзыв на событие по идентификатору
     * @param commentId идентификатор отзыва на событие
     * @return CommentDto
     */
    CommentDto findCommentById(Long commentId);
}