package ru.practicum.explorewithme.event.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.common.EventPublicService;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.util.Mapper;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventAdminService {

    private final EventPublicService eventPublicService;
    private final EventRepository eventRepository;
    private final Mapper mapper;

    public List<Event> getEvents(Long[] userIds,
                                 EventState[] states,
                                 Long[] categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd) {
        log.debug("Getting events by: " +
                "\nuser ids: {}, " +
                "\nstates: {}, " +
                "\ncategories: {}, " +
                "\nrangeStart: {}, " +
                "\nrangeEnd: {}", userIds, states, categories, rangeStart, rangeEnd);
        return eventRepository.findEventsByAdminFilters(userIds, states, categories, rangeStart, rangeEnd);
    }

    public Event putEvent(AdminUpdateEventDto eventDto, long eventId) {
        log.debug("Updating event by: {}", eventDto);
        Event event = eventPublicService.getEvent(eventId);
        mapper.map(eventDto, event);
        eventRepository.save(event);
        return event;
    }

    @Transactional
    public Event changeEventState(long eventId, boolean isPublish) {
        log.debug("Changing Event id: {}'s status, is publish - {}", eventId, isPublish);
        Event event = eventPublicService.getEvent(eventId);
        if (!event.getState().equals(EventState.PENDING)) {
            log.error("UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s is already updated", eventId));
        }
        if (isPublish) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            event.setState(EventState.CANCELED);
        }
        eventRepository.save(event);
        return event;
    }
}
