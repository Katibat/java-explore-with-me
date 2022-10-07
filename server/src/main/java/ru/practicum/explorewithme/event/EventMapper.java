package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.category.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.location.LocationMapper;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.user.UserMapper;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    private static EventPrivateService service;

    public static Event toModel(EventCreateDto eventCreateDto, Long userId) {
        Category category = service.findCategoryById(eventCreateDto.getCategory());
        User initiator = service.findUserById(userId);
        return Event.builder()
                .title(eventCreateDto.getTitle())
                .annotation(eventCreateDto.getAnnotation())
                .description(eventCreateDto.getDescription())
                .category(category)
                .createdOn(LocalDateTime.now())
                .eventDate(eventCreateDto.getEventDate())
                .initiator(initiator)
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .requestModeration(eventCreateDto.getRequestModeration())
                .state(EventState.PENDING)
                .location(LocationMapper.toModel(eventCreateDto.getLocation()))
                .build();
    }

    public static EventFullDto toFullDto(Event event, Integer views) {
        Long confirmedRequests = service.getConfirmedRequestsCount(event);
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .location(LocationMapper.toDto(event.getLocation()))
                .confirmedRequests(confirmedRequests)
                .category(CategoryMapper.toDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto toShortDto(Event event, Integer views) {
        Long confirmedRequests = service.getConfirmedRequestsCount(event);
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static void toUpdateDto(EventUpdateDto eventUpdateDto, Event event) {
        Category category = service.findCategoryById(eventUpdateDto.getCategory());
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (eventUpdateDto.getCategory() != null) {
            event.setCategory(category);
        }
        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }
        if (eventUpdateDto.getEventId() != null) {
            event.setId(eventUpdateDto.getEventId());
        }
        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }
    }

    public static Event toEventAdminUpdate(EventAdminUpdate eventAdminUpdate, Event event) {
        Category category = service.findCategoryById(eventAdminUpdate.getCategory());
        if (eventAdminUpdate.getAnnotation() != null) {
            event.setAnnotation(eventAdminUpdate.getAnnotation());
        }
        if (eventAdminUpdate.getCategory() != null) {
            event.setCategory(category);
        }
        if (eventAdminUpdate.getDescription() != null) {
            event.setDescription(eventAdminUpdate.getDescription());
        }
        if (eventAdminUpdate.getEventDate() != null) {
            event.setEventDate(eventAdminUpdate.getEventDate());
        }
        if (eventAdminUpdate.getPaid() != null) {
            event.setPaid(eventAdminUpdate.getPaid());
        }
        if (eventAdminUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminUpdate.getParticipantLimit());
        }
        if (eventAdminUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminUpdate.getRequestModeration());
        }
        if (eventAdminUpdate.getTitle() != null) {
            event.setTitle(eventAdminUpdate.getTitle());
        }
        return event;
    }
}