package ru.practicum.explorewithme.service.priv;

import ru.practicum.explorewithme.dto.event.EventCreateDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventUpdateDto;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.model.event.Event;

import java.util.List;

/**
 * Реализация закрытого API для работы с событиями
 * (для авторизованных пользователей)
 */
public interface EventPrivateService {

    /**
     * Получить список событий добавленных текущим пользователем с параметрами
     * @param userId идентификатор текущего пользователя
     * @param from количество объектов, default value = 0
     * @param size размер страницы default value = 10
     * @return List<EventFullDto>
     */
    List<EventFullDto> findEventByInitiatorId(Long userId, int from, int size);

    /**
     * Изменить событие добавленное текущим пользователем
     * @param eventUpdateDto
     * @param userId идентификатор текущего пользователя
     * @return EventFullDto
     */
    EventFullDto update(EventUpdateDto eventUpdateDto, Long userId);

    /**
     * Сохранить новое событие добавленное текущим пользователем
     * @param eventCreateDto
     * @param userId идентификатор текущего пользователя
     * @return EventFullDto
     */
    EventFullDto save(EventCreateDto eventCreateDto, Long userId);

    /**
     * Получмит полную информацию о событии добавленном текущим пользователем
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @return EventFullDto
     */
    EventFullDto findEventInfoByInitiator(Long userId, Long eventId);

    /**
     * Отменить событие добавленное текущим пользователем
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @return EventFullDto
     */
    EventFullDto cancel(Long userId, Long eventId);

    /**
     * Получить информацию о запросах на участие в событии текущего пользователя
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @return List<RequestDto>
     */
    List<RequestDto> getAllRequestsByEventId(Long userId, Long eventId);

    /**
     * Подтвердить или отклонить чужую заявку на участие в событии созданном текущим пользователем
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @param requestId идентификатор заявки на участие другим пользователем
     * @param isApproved true - подтвердить, false - отклонить
     * @return RequestDto
     */
    RequestDto changeRequestStatus(Long userId, Long eventId, Long requestId, boolean isApproved);

    /**
     * Искать событие по идентификатору
     * если не найден, то вернуть NotFoundException
     * @param eventId идентификатор события
     * @return Event
     */
    Event findEventById(Long eventId);
}