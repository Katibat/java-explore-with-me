package ru.practicum.explorewithme.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}