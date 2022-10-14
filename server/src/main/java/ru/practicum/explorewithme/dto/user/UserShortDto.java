package ru.practicum.explorewithme.dto.user;

import lombok.*;

/**
 * Краткая информация о пользователи
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}