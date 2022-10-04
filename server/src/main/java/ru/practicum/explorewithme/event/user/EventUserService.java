package ru.practicum.explorewithme.event.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.common.CategoryPublicService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.common.EventPublicService;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.exception.UserNotInitiatorException;
import ru.practicum.explorewithme.user.UserAdminService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static ru.practicum.explorewithme.event.EventState.CANCELED;
import static ru.practicum.explorewithme.event.EventState.PENDING;

@Slf4j
@Service
@AllArgsConstructor
public class EventUserService {

    private final UserAdminService userAdminService;
    private final CategoryPublicService categoryService;
    private final EventPublicService eventPublicService;
    private final EventRepository eventRepository;
    private final EntityManager entityManager;

    @Transactional
    public Event save(Event event) {
        log.debug("Saving new event to DB: {}", event);
        userAdminService.checkUserId(event.getInitiatorId());
        categoryService.checkCategoryId(event.getCategoryId());
        eventRepository.save(event);
        entityManager.refresh(event);
        return event;
    }

    public List<Event> getUserEvents(long userId, int from, int size) {
        log.debug("Getting user id: {}'s events", userId);
        userAdminService.checkUserId(userId);
        Pageable pageable = PageRequest.of(from, size);
        return eventRepository.findAllByInitiatorId(userId, pageable);
    }

    public Event getEventByOwnerId(long userId, long eventId) {
        log.debug("Getting event id: {} by user id: {}", eventId, userId);
        userAdminService.checkUserId(userId);
        Event event = eventPublicService.getEvent(eventId);
        long initiatorId = event.getInitiatorId();
        if (userId != initiatorId) {
            log.error("UserNotInitiatorException");
            throw new UserNotInitiatorException(String.format("User id: %s is not initiator for event id: %s",
                    userId, eventId));
        }
        return event;
    }

    public Event getEventById(long eventId) {
        log.debug("Getting event id: {}", eventId);
        checkEventId(eventId);
        return eventRepository.findById(eventId).get();
    }

    @Transactional
    public void patchEvent(Event event, long userId) {
        log.debug("Updating event id: {} by user id: {}", event.getId(), userId);
        Event eventDb = getEventById(event.getId());
        if (eventDb.getInitiatorId() != userId) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s can't be update because user id: %s is " +
                    "not owner", event.getId(), userId));
        }
        if (!event.getState().equals(PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s can't be update because it status is: %s",
                    event.getId(), event.getState()));
        }
        categoryService.checkCategoryId(event.getCategoryId());
        eventRepository.save(event);
        entityManager.refresh(event);
    }

    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        log.debug("Cancel event id: {} by user id: {}", eventId, userId);
        Event event = getEventByOwnerId(userId, eventId);
        if (event.getState().equals(CANCELED)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s can't be canceled because it status is: %s",
                    event.getId(), event.getState()));
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
}