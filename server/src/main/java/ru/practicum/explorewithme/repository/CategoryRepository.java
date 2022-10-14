package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}