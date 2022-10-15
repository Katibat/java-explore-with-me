package ru.practicum.explorewithme.service.pub;

import ru.practicum.explorewithme.dto.category.CategoryDto;

import java.util.List;

/**
 * Реализация публичного API для работы с категориями
 */

public interface CategoriesPublicService {

    /**
     * Получить категории события по параметрам
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<CategoryDto>
     */
    List<CategoryDto> findAll(int from, int size);

    /**
     * Получить информацию о категории по идентификатору
     * @param categoryId идентификатор категории
     * @return CategoryDto
     */
    CategoryDto findById(Long categoryId);
}