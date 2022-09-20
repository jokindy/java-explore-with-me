package ru.practicum.explorewithme.event.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.common.CategoryPublicService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventRepo;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.exception.SizeIsZeroException;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.exception.UserNotInitiatorException;
import ru.practicum.explorewithme.user.UserAdminService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

import static ru.practicum.explorewithme.event.EventState.*;

@Slf4j
@Service
@AllArgsConstructor
public class EventUserService {

    private final UserAdminService userAdminService;
    private final CategoryPublicService categoryService;
    private final EntityManager entityManager;
    private final EventRepo eventRepo;

    @Transactional
    public Event save(Event event) {
        log.warn("EVENT USER SERVICE - saving new event to DB: {}", event);
        userAdminService.checkUserId(event.getInitiatorId());
        categoryService.checkCategoryId(event.getCategoryId());
        eventRepo.save(event);
        entityManager.refresh(event);
        return event;
    }

    public Page<Event> getUserEvents(long userId, int from, int size) {
        log.warn("EVENT USER SERVICE - getting user id: {}'s events", userId);
        userAdminService.checkUserId(userId);
        Pageable pageable = getPageable(from, size);
        return eventRepo.findAllByInitiatorId(userId, pageable);
    }

    public Event getEventByOwnerId(long userId, long eventId) {
        log.warn("EVENT USER SERVICE - getting event id: {} by user id: {}", eventId, userId);
        userAdminService.checkUserId(userId);
        Optional<Event> eventOptional = eventRepo.findById(eventId);
        if (eventOptional.isPresent()) {
            long initiatorId = eventOptional.get().getInitiatorId();
            if (userId != initiatorId) {
                log.warn("EVENT USER SERVICE - UserNotInitiatorException");
                throw new UserNotInitiatorException(String.format("User id: %s is not initiator for event id: %s",
                        userId, eventId));
            }
            return eventOptional.get();
        } else {
            log.warn("EVENT USER SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        }
    }

    public Event getEventById(long eventId) {
        log.warn("EVENT USER SERVICE - getting event id: {}", eventId);
        checkEventId(eventId);
        return eventRepo.findById(eventId).get();
    }

    @Transactional
    public void patchEvent(Event event, long userId) {
        log.warn("EVENT USER SERVICE - updating event id: {} by user id: {}", event.getId(), userId);
        Event eventDb = getEventById(event.getId());
        if (eventDb.getInitiatorId() != userId) {
            log.warn("EVENT USER SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s can't be update because user id: %s is " +
                            "not owner", event.getId(), userId));
        }
        if (!event.getState().equals(PENDING)) {
            log.warn("EVENT USER SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s can't be update because it status is: %s",
                    event.getId(), event.getState()));
        }
        categoryService.checkCategoryId(event.getCategoryId());
        eventRepo.save(event);
        entityManager.refresh(event);
    }

    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        log.warn("EVENT USER SERVICE - cancel event id: {} by user id: {}", eventId, userId);
        Event event = getEventByOwnerId(userId, eventId);
        if (event.getState().equals(CANCELED)) {
            log.warn("EVENT ADMIN SERVICE - ModelNotFoundException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s can't be canceled because it status is: %s",
                    event.getId(), event.getState()));
        }
        eventRepo.cancelEvent(CANCELED, eventId);
        event.setState(CANCELED);
        return event;
    }

    private Pageable getPageable(int from, int size) {
        if (size == 0) {
            throw new SizeIsZeroException("Size can't be a zero"); // custom exception
        }
        return PageRequest.of(from, size, Sort.by("id").descending());
    }

    public void checkEventId(long eventId) {
        boolean isExists = eventRepo.existsById(eventId);
        if (!isExists) {
            log.warn("EVENT USER SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        }
    }
}