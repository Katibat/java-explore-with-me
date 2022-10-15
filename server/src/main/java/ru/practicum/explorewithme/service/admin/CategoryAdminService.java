package ru.practicum.explorewithme.service.admin;

import ru.practicum.explorewithme.dto.category.*;

/**
 * Реализация API для работы с категориями на уровне администратора
 */

public interface CategoryAdminService {

    /**
     * Обновить информацию о категории событий
     * @param categoryDto
     * @return CategoryDto
     */
    CategoryDto update(CategoryDto categoryDto);

    /**
     * Сохранить новую категорию событий
     * @param categoryCreateDto
     * @return CategoryDto
     */
    CategoryDto save(CategoryCreateDto categoryCreateDto);

    /**
     * Удалить категорию событий по идентификатору
     * @param categoryId идентификатор категории событий
     */
    void delete(Long categoryId);
}