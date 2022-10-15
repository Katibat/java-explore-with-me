package ru.practicum.explorewithme.dto.user;

import lombok.*;

import javax.validation.constraints.*;

/**
 * Информация для создания пользователя
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String name;
}