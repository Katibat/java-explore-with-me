package ru.practicum.explorewithme.category.model;

import lombok.*;

import javax.validation.constraints.*;

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