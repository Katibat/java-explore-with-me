package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.UserCreateDto;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.admin.UserAdminService;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

/**
 * API для работы с пользователями на уровне администратора
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {
    private final UserAdminService service;

    @GetMapping // Получение информации о пользователях
    public List<UserDto> findAll(@RequestParam(required = false) Set<Long> ids,
                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                 @Positive @RequestParam(defaultValue = "10") int size) {
        if (ids != null && !ids.isEmpty()) {
            return service.getUsersByIds(ids);
        }
        return service.findAll(ids, from, size);
    }

    @PostMapping // Добавление нового пользователя
    public UserDto save(@Valid @RequestBody UserCreateDto userCreateDto) {
        return service.save(userCreateDto);
    }

    @DeleteMapping("/{userId}") // Удаление пользователя
    public void delete(@PathVariable Long userId) {
        service.deleteById(userId);
    }
}