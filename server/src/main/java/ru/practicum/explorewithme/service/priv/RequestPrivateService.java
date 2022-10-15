package ru.practicum.explorewithme.service.priv;

import ru.practicum.explorewithme.dto.request.RequestDto;

import java.util.List;

/**
 * Реализация закрытого API для работы с запросами текущего пользователя на участие в событиях
 * (для авторизованных пользователей)
 */

public interface RequestPrivateService {

    /**
     * Получить список запросов на участие в событиях текущего пользователя
     * @param userId идентификатор текущего пользователя
     * @return List<RequestDto>
     */
    List<RequestDto> findRequests(Long userId);

    /**
     * Сохранить запрос от текущего пользователя на участие в событии
     * @param userId идентификатор текущего пользователя
     * @param eventId идентификатор события
     * @return RequestDto
     */
    RequestDto save(Long userId, Long eventId);

    /**
     * Отмена запроса на участие в событии текущим пользователем
     * @param userId идентификатор текущего пользователя
     * @param requestId идентификатор запроса на участие в событии
     * @return RequestDto
     */
    RequestDto cancelRequest(Long userId, Long requestId);
}