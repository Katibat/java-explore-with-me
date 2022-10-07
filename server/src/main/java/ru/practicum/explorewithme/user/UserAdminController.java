package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.model.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {
    private final UserAdminService service;

    @GetMapping // Получение информации о пользователях
    public List<UserDto> findAll(@RequestParam(required = false) Long[] ids,
                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                 @Positive @RequestParam(defaultValue = "10") int size) {
        return service.findAll(ids, from, size);
    }

    @PostMapping // Добавление нового пользователя
    public UserDto save(@Valid @RequestBody UserCreateDto userCreateDto) {
        return service.save(userCreateDto);
    }

    @DeleteMapping("/{userId}") // Удаление пользователя
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }
}