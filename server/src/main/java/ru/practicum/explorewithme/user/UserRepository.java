package ru.practicum.explorewithme.user;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByIdIn(Long[] ids, Pageable pageable);
}