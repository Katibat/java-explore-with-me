package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.admin.CategoryAdminService;
import ru.practicum.explorewithme.dto.category.CategoryCreateDto;
import ru.practicum.explorewithme.dto.category.CategoryDto;

import javax.validation.Valid;

/**
 * API для работы с категориями на уровне администратора
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryAdminService service;

    @PatchMapping // изменение категории
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        return service.update(categoryDto);
    }

    @PostMapping // добавление новой категории
    public CategoryDto save(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        return service.save(categoryCreateDto);
    }

    @DeleteMapping("/{catId}") // удаление категории
    public void deleteCategory(@PathVariable Long catId) {
        service.delete(catId);
    }
}