package ru.practicum.explorewithme.dto.comment;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Информация для отзыва на событие
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    @Length(min = 3, max = 7000)
    private String text;
    private Long eventId;
    private Long authorId;
    private Boolean edited;
    private LocalDateTime created = LocalDateTime.now();
}