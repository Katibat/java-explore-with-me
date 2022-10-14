package ru.practicum.explorewithme.service.admin;

import ru.practicum.explorewithme.dto.event.EventAdminUpdate;
import ru.practicum.explorewithme.dto.event.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация API для работы с событиями на уровне администратора
 */

public interface EventAdminService {

    /**
     * Искать события по параметрам
     * @param users список пользователей
     * @param states список названий статусов событий (класс EventState)
     * @param categories список идентификаторов категорий событий
     * @param rangeStart дата и время начала периода поиска
     * @param rangeEnd дата и время окончания периода поиска
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<EventFullDto>
     */
    List<EventFullDto> searchEvents(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    int from,
                                    int size);

    /**
     * Обновить информацию о событии
     * @param eventAdminUpdate
     * @param eventId идентификатор события
     * @return EventFullDto
     */
    EventFullDto update(EventAdminUpdate eventAdminUpdate, Long eventId);

    /**
     * Опубликовать или отклонить публикацию события
     * @param eventId идентификатор события
     * @param isPublish true - публикация, false - отклонение публикации
     * @return
     */
    EventFullDto changeEventState(Long eventId, boolean isPublish);
}