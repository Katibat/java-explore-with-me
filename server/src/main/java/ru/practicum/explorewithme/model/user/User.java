package ru.practicum.explorewithme.model.user;

import lombok.*;

import javax.persistence.*;

/**
 * Пользователь
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}