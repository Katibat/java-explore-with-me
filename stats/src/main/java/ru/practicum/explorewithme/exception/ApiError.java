package ru.practicum.explorewithme.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сведения об ошибке
 */

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
    private LocalDateTime timestamp;
}