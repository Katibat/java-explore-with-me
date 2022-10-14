package ru.practicum.explorewithme.dto.category;

import lombok.*;

import javax.validation.constraints.*;

/**
 * Информация для создания категории события
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String name;
}