package ru.practicum.explorewithme.dto;

/**
 * Реализация получения данных статистики
 */

public interface ViewStats {

    /**
     * название сервиса
     */
    String getApp();

    /**
     * URI сервиса
     */
    String getUri();

    /**
     * Количество просмотров
     */
    Integer getHits();
}