package ru.practicum.explorewithme.dto.comment;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Информация для обновления отзыва на событие
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
    @NotBlank
    @Length(min = 3, max = 7000)
    private String text;
}