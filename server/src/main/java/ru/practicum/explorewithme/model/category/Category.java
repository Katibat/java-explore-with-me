package ru.practicum.explorewithme.model.category;

import lombok.*;

import javax.persistence.*;

/**
 * Категория события
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}