package ru.practicum.explorewithme.event.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventFilter;
import ru.practicum.explorewithme.event.EventRepo;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.common.EventPublicService;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.util.PageMaker;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventAdminService {

    private final EventPublicService eventPublicService;
    private final EventRepo eventRepo;
    private final PageMaker<Event> pageMaker;

    public List<Event> getEvents(Long[] userIds,
                                 EventState[] states,
                                 Long[] categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 int from,
                                 int size) {
        log.debug("EVENT ADMIN SERVICE - getting events by: " +
                        "\nuser ids: {}, " +
                        "\nstates: {}, " +
                        "\ncategories: {}, " +
                        "\nrangeStart: {}, " +
                        "\nrangeEnd: {}", userIds, states, categories, rangeStart, rangeEnd);
        List<Event> events = Arrays.stream(userIds)
                .map(eventRepo::findEventByInitiatorId)
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByCategoryId(event, categories))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByState(event, states))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByStartAndEnd(event, rangeStart, rangeEnd))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return pageMaker.getPage(from, size, events).getContent();
    }

    public void putEvent(Event event) {
        log.debug("EVENT ADMIN SERVICE - updating event by: {}", event);
        eventRepo.save(event);
    }

    @Transactional
    public Event changeEventState(long eventId, boolean isPublish) {
        log.debug("EVENT ADMIN SERVICE - changing Event id: {}'s status, is publish - {}", eventId, isPublish);
        Event event = getEvent(eventId);
        if (!event.getState().equals(EventState.PENDING)) {
            log.warn("EVENT ADMIN SERVICE - UpdateIsForbiddenException");
            throw new UpdateIsForbiddenException(String.format("Event id: %s is already updated", eventId));
        }
        if (isPublish) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            event.setState(EventState.CANCELED);
        }
        return eventRepo.save(event);
    }

    public Event getEvent(long eventId) {
        log.debug("EVENT ADMIN SERVICE - getting event id: {}", eventId);
        return eventPublicService.getEvent(eventId);
    }
}
