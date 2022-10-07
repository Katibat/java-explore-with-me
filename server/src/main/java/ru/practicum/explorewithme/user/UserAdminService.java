package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.model.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAdminService {
    private final UserRepository repository;

    public List<UserDto> findAll(Long[] ids, int from, int size) {
        Page<User> users = repository.findAllByIdIn(ids, PageRequest.of(from, size));
        return users.map(UserMapper::toDto).toList();
    }

    @Transactional
    public UserDto save(UserCreateDto userCreateDto) {
        log.info("UserAdminService: Для сохранения передан пользователь с name=" + userCreateDto.getName());
        User user = repository.save(UserMapper.toModel(userCreateDto));
        return UserMapper.toDto(user);
    }

    @Transactional
    public void delete(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("UserAdminService: Не найден пользователь для удаления с id=" + userId));
        repository.delete(user);
        log.info("UserAdminService: Удален пользователь с id=" + userId);
    }
}