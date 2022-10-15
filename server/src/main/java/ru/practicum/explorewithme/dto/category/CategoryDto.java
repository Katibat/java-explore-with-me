package ru.practicum.explorewithme.dto.category;

import lombok.*;

import javax.validation.constraints.*;

/**
 * Информация о категории события
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String name;
}