package ru.practicum.explorewithme.event.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.common.CategoryPublicService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.user.UserAdminService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.event.EventState.CANCELED;
import static ru.practicum.explorewithme.event.EventState.PENDING;

@Slf4j
@Service
@AllArgsConstructor
public class EventUserService {

    private final EventClient eventClient;
    private final UserAdminService userAdminService;
    private final CategoryPublicService categoryService;
    private final EntityManager entityManager;
    private final EventRepository eventRepository;

    @Transactional
    public EventFullDto save(NewEventDto dto, long userId) {
        log.warn("Saving new event to DB: {}", dto);
        userAdminService.checkUserId(userId);
        categoryService.checkCategoryId(dto.getCategory());
        Event event = NewEventDto.toDomain(dto, userId);
        eventRepository.save(event);
        entityManager.refresh(event);
        return EventFullDto.construct(event, 0);
    }

    public List<EventShortDto> getUserEvents(long userId, int from, int size) {
        log.warn("Getting user id: {}'s events", userId);
        userAdminService.checkUserId(userId);
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable).getContent();
        return events.stream()
                .map(e -> {
                    int views = getViews(e.getId());
                    return EventShortDto.construct(e, views);
                })
                .collect(Collectors.toList());
    }

    public Event getEventByOwnerId(long userId, long eventId) {
        log.warn("Getting event id: {} by user id: {}", eventId, userId);
        userAdminService.checkUserId(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            long initiatorId = eventOptional.get().getInitiatorId();
            if (userId != initiatorId) {
                log.error("UserNotInitiatorException");
                throw ForbiddenException.userIsNotInitiator(userId, eventId);
            }
            return eventOptional.get();
        } else {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        }
    }

    public Event getEventById(long eventId) {
        log.warn("Getting event id: {}", eventId);
        checkEventId(eventId);
        return eventRepository.findById(eventId).get();
    }

    @Transactional
    public void patchEvent(Event event, long userId) {
        log.warn("Updating event id: {} by user id: {}", event.getId(), userId);
        Event eventDb = getEventById(event.getId());
        if (eventDb.getInitiatorId() != userId) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updateEvent(event.getId(), userId);
        }
        if (!event.getState().equals(PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.updatePublishedEvent(event.getId(), event.getState());
        }
        categoryService.checkCategoryId(event.getCategoryId());
        eventRepository.save(event);
        entityManager.refresh(event);
    }

    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        log.warn("Cancel event id: {} by user id: {}", eventId, userId);
        Event event = getEventByOwnerId(userId, eventId);
        if (event.getState().equals(CANCELED)) {
            log.error("UpdateIsForbiddenException");
            throw ForbiddenException.cancelEvent(eventId, event.getState());
        }
        eventRepository.cancelEvent(CANCELED, eventId);
        event.setState(CANCELED);
        return event;
    }

    public void checkEventId(long eventId) {
        boolean isExists = eventRepository.existsById(eventId);
        if (!isExists) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        }
    }

    private int getViews(long id) {
        String uri = "/events/" + id;
        return (Integer) eventClient.getViews(uri);
    }
}