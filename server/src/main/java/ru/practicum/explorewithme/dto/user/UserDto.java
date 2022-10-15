package ru.practicum.explorewithme.dto.user;

import lombok.*;

/**
 * Информация о пользователи
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}