package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.*;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriesPublicService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public List<CategoryDto> findAll(int from, int size) {
        log.info("PublicCategoriesService: Получение списка категорий.");
        return repository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Long categoryId) {
        log.info("PublicCategoriesService: Получение категории по ее идентификатору.");
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("PublicCategoriesService: Не найдена категория с id=" +
                        categoryId));
        return mapper.toDto(category);
    }
}