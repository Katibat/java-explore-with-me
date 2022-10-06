package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.*;

import javax.validation.Valid;

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