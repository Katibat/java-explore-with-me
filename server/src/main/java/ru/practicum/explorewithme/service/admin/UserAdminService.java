package ru.practicum.explorewithme.service.admin;

import ru.practicum.explorewithme.dto.user.UserCreateDto;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;
import java.util.Set;

/**
 * Реализация API для работы с пользователями на уровне администратора
 */

public interface UserAdminService {

    /**
     * Получить информацию о пользователях по параметрам
     * @param ids сортированный список идентификаторов пользователей
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<UserDto>
     */
    List<UserDto> findAll(Set<Long> ids, int from, int size);

    /**
     * Сохранить нового пользователя
     * @param userCreateDto
     * @return UserDto
     */
    UserDto save(UserCreateDto userCreateDto);

    /**
     * Удалить пользователя по идентификатору
     * @param userId идентификатор пользователя
     */
    void deleteById(Long userId);

    /**
     * Получить список пользователей
     * @param ids сортированный список идентификаторов пользователей
     * @return List<UserDto>
     */
    List<UserDto> getUsersByIds(Set<Long> ids);
}