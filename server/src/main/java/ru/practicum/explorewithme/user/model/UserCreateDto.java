package ru.practicum.explorewithme.user.model;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @NotNull
    @Email
    private String email;
    @NotBlank
    private String name;
}
