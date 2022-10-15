package ru.practicum.explorewithme.dto.compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Информация для создания подборки событий
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationCreateDto {
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}