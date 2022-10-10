package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.*;
import ru.practicum.explorewithme.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryAdminService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category categoryUpdate = mapper.toUpdate(categoryDto);
        Category category = repository.findById(categoryDto.getId())
                .orElseThrow(() ->
                        new NotFoundException("CategoryAdminService: Не найдена категория с id=" + categoryDto.getId()));
        category.setName(categoryUpdate.getName());
        Category updated = repository.save(category);
        log.info("CategoryAdminService: Обновлена информация о категории №={}.", categoryDto.getId());
        return mapper.toDto(updated);
    }

    @Transactional
    public CategoryDto save(CategoryCreateDto categoryCreateDto) {
        log.info("CategoryAdminService: Сохранение категории с названием={}.", categoryCreateDto.getName());
        Category category = mapper.toModel(categoryCreateDto);
        Category saved = repository.save(category);
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() ->
                        new NotFoundException("CategoryAdminService: Не найдена категория событий с id=" + categoryId));
        repository.delete(category);
        log.info("CategoryAdminService: Удалена информация о категории №={}.", categoryId);
    }
}