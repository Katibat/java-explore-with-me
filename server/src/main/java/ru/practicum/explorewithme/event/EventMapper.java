package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.CategoryDto;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.event.location.Location;
import ru.practicum.explorewithme.event.location.LocationMapper;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserShortDto;

import java.time.LocalDateTime;

public class EventMapper {
    private static RequestRepository requestRepository;
    private static StatsClient client;

    public static Event toModel(EventCreateDto eventCreateDto, Long userId) {
        return Event.builder()
                .title(eventCreateDto.getTitle())
                .annotation(eventCreateDto.getAnnotation())
                .description(eventCreateDto.getDescription())
                .category(new Category(eventCreateDto.getCategory(), null))
                .createdOn(LocalDateTime.now())
                .eventDate(eventCreateDto.getEventDate())
                .initiator(new User(userId, null, null))
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .requestModeration(eventCreateDto.getRequestModeration())
                .state(EventState.PENDING)
                .location(new Location(eventCreateDto.getLocation().getLat(), eventCreateDto.getLocation().getLon()))
                .build();
    }

    public static EventFullDto toFullDto(Event event) {
        Integer confirmedRequests = requestRepository.countByEventAndStatus(event.getId(), RequestStatus.CONFIRMED);
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .location(LocationMapper.toDto(event.getLocation()))
                .confirmedRequests(confirmedRequests)
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        Integer confirmedRequests = requestRepository.countByEventAndStatus(event.getId(), RequestStatus.CONFIRMED);
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public static Event toModelFromShortDto(EventShortDto eventShortDto) {
        return Event.builder()
                .id(eventShortDto.getId())
                .title(eventShortDto.getTitle())
                .annotation(eventShortDto.getAnnotation())
                .eventDate(eventShortDto.getEventDate())
                .paid(eventShortDto.getPaid())
                .build();
    }

    public static EventShortDto toShortDtoFromFullDto(EventFullDto eventFullDto) {
        return EventShortDto.builder()
                .id(eventFullDto.getId())
                .annotation(eventFullDto.getAnnotation())
                .category(eventFullDto.getCategory())
                .confirmedRequests(eventFullDto.getConfirmedRequests())
                .eventDate(eventFullDto.getEventDate())
                .initiator(eventFullDto.getInitiator())
                .paid(eventFullDto.getPaid())
                .title(eventFullDto.getTitle())
                .views(eventFullDto.getViews())
                .build();
    }

    private static Integer getViews(Long eventId) {
        String uri = "/events/" + eventId;
        return (Integer) client.getViews(uri);
    }
}