package ru.practicum.explorewithme.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.pub.CategoriesPublicService;
import ru.practicum.explorewithme.dto.category.CategoryDto;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Публичный API для работы с категориями
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesPublicController {
    private final CategoriesPublicService categoryService;

    @GetMapping // Получение категорий
    public List<CategoryDto> findAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                     @Positive @RequestParam(defaultValue = "10") int size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{categoryId}") // Получение информации о категории по ее идентификатору
    public CategoryDto findById(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }
}