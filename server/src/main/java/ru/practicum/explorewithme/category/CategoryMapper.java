package ru.practicum.explorewithme.category;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.model.*;

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