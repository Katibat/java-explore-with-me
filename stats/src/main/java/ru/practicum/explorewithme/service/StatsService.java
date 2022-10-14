package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация API для работы с сервисом статистики
 */

public interface StatsService {

    /**
     * Сохранить информацию о том, что к endpointHit был запрос с основного сервиса
     * о просмотре событий в публичном доступе
     * @param endpointHit
     */
    void save(EndpointHit endpointHit);

    /**
     * Получить количество просмотров события по URI
     * @param uri URI для которого был осуществлен запрос
     * @return Integer натуральное число просмотров
     */
    Integer getViews(String uri);

    /**
     * Получить список посещений события
     * @param start дата и время начала периода
     * @param end дата и время окончания периода
     * @param uris массив URI
     * @param unique true - уникальные IP, false - не уникальные IP
     * @return List<ViewStats>
     */
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}