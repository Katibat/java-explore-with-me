package ru.practicum.explorewithme.user.model;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
    @Email
    @NotBlank
    @NotNull
    private String email;
}