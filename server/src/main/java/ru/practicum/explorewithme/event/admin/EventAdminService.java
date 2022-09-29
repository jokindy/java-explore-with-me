package ru.practicum.explorewithme.event.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.common.EventPublicService;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;
import ru.practicum.explorewithme.util.Mapper;
import ru.practicum.explorewithme.util.PageMaker;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventAdminService {

    private final EventPublicService eventPublicService;
    private final EventRepository eventRepository;
    private final EventClient eventClient;
    private final Mapper mapper;
    private final PageMaker<Event> pageMaker;

    public List<EventFullDto> getEvents(Long[] userIds,
                                        EventState[] states,
                                        Long[] categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size) {
        log.debug("Getting events by: " +
                "\nuser ids: {}, " +
                "\nstates: {}, " +
                "\ncategories: {}, " +
                "\nrangeStart: {}, " +
                "\nrangeEnd: {}", userIds, states, categories, rangeStart, rangeEnd);
        List<Event> events = eventRepository.findEventsByAdminFilters(userIds, states, categories, rangeStart, rangeEnd);
        List<Event> list = pageMaker.getPage(from, size, events).getContent();
        return list.stream()
                .map(event -> {
                    int views = getViews(event.getId());
                    return EventFullDto.construct(event, views);
                })
                .collect(Collectors.toList());
    }

    public EventFullDto putEvent(AdminUpdateEventDto eventDto, long eventId) {
        log.debug("Updating event by: {}", eventDto);
        Event event = getEvent(eventId);
        mapper.map(eventDto, event);
        eventRepository.save(event);
        return EventFullDto.construct(event, getViews(eventId));
    }

    @Transactional
    public EventFullDto changeEventState(long eventId, boolean isPublish) {
        log.debug("Changing Event id: {}'s status, is publish - {}", eventId, isPublish);
        Event event = getEvent(eventId);
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
        return EventFullDto.construct(event, getViews(eventId));
    }


    public Event getEvent(long eventId) {
        log.debug("Getting event id: {}", eventId);
        return eventPublicService.getEvent(eventId);
    }

    private int getViews(long id) {
        String uri = "/events/" + id;
        return (Integer) eventClient.getViews(uri);
    }
}
