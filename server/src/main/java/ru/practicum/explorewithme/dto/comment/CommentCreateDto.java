package ru.practicum.explorewithme.dto.comment;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Информация для сохранения отзыва на событие
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    @NotBlank
    @Length(min = 3, max = 7000)
    private String text;
    private Boolean edited = false;
    private LocalDateTime created = LocalDateTime.now();
}