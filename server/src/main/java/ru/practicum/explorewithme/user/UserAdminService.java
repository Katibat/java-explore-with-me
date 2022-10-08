package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAdminService {
    private final UserRepository repository;

    public List<UserDto> findAll(Set<Long> ids, int from, int size) {
        log.info("UserAdminService: Получение информации о пользователях с параметрами from={}, size={}", from, size);
        return repository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto save(UserCreateDto userCreateDto) {
        log.info("UserAdminService: Для сохранения передан пользователь с name=" + userCreateDto.getName());
        User user = repository.save(UserMapper.toModel(userCreateDto));
        return UserMapper.toDto(user);
    }

    @Transactional
    public void deleteById(Long userId) {
        boolean isExists = repository.existsById(userId);
        if (!isExists) {
            throw new NotFoundException("UserAdminService: Не найден пользователь для удаления с id=" + userId);
        }
        repository.deleteById(userId);
        log.info("UserAdminService: Удален пользователь с id=" + userId);
    }

    public List<UserDto> getUsersByIds(Set<Long> ids) {
        return repository.findUsersByIdIn(ids)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}