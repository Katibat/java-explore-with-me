package ru.practicum.explorewithme;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    private Long id; // Идентификатор записи
    @NotBlank
    private String app; // Идентификатор сервиса для которого записывается информация
    @NotBlank
    private String uri; // URI для которого был осуществлен запрос
    @NotBlank
    private String ip; // IP-адрес пользователя, осуществившего запрос
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к эндпоинту
}