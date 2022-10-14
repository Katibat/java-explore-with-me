package ru.practicum.explorewithme.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.category.Category;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.dto.event.EventCreateDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.event.Location;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final RequestRepository requestRepository;
    private final StatsClient client;

    public Event toModel(EventCreateDto eventCreateDto, Long userId) {
        return Event.builder()
                .id(null)
                .title(eventCreateDto.getTitle())
                .annotation(eventCreateDto.getAnnotation())
                .description(eventCreateDto.getDescription())
                .category(new Category(eventCreateDto.getCategory(), null))
                .createdOn(LocalDateTime.now())
                .eventDate(eventCreateDto.getEventDate())
                .initiator(new User(userId, null, null))
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(eventCreateDto.getRequestModeration())
                .state(EventState.PENDING)
                .location(new Location(eventCreateDto.getLocation().getLat(), eventCreateDto.getLocation().getLon()))
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .location(LocationMapper.toDto(event.getLocation()))
                .confirmedRequests(requestRepository.getConfirmedRequests(event.getId()))
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(requestRepository.getConfirmedRequests(event.getId()))
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public Event toModelFromShortDto(EventShortDto eventShortDto) {
        return Event.builder()
                .id(eventShortDto.getId())
                .title(eventShortDto.getTitle())
                .annotation(eventShortDto.getAnnotation())
                .eventDate(eventShortDto.getEventDate())
                .paid(eventShortDto.getPaid())
                .build();
    }

    public EventShortDto toShortDtoFromFullDto(EventFullDto eventFullDto) {
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

    private Integer getViews(Long eventId) {
        String uri = "/events/" + eventId;
        return (Integer) client.getViews(uri);
    }
}