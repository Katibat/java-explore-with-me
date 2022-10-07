package ru.practicum.explorewithme.compilation.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationCreateDto {
    @NotBlank
    @Length(min = 5, max = 256)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}