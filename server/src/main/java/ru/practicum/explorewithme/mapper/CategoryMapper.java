package ru.practicum.explorewithme.mapper;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.category.CategoryCreateDto;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.model.category.Category;


@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public Category toModel(CategoryCreateDto categoryCreateDto) {
        return Category.builder()
                .name(categoryCreateDto.getName())
                .build();
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toUpdate(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}