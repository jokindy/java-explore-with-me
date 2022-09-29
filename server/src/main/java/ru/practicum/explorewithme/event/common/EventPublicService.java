package ru.practicum.explorewithme.event.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventPublicService {

    private final EventClient eventClient;
    private final EventRepository eventRepository;
    private final PageMaker<Event> pageMaker;

    public List<EventShortDto> getSortedEvents(List<Event> list, int from, int size, EventSort sort) {
        List<Event> events = pageMaker.getPage(from, size, list).getContent();
        return events.stream()
                .map(event -> {
                    String uri = "/events/" + event.getId();
                    int views = (Integer) eventClient.getViews(uri);
                    return EventShortDto.construct(event, views);
                })
                .sorted((o1, o2) -> {
                    if (sort != null) {
                        switch (sort) {
                            case EVENT_DATE:
                                return o1.getEventDate().compareTo(o2.getEventDate());
                            case VIEWS:
                                return o1.getViews().compareTo(o2.getViews());
                            default:
                                return o1.getId().compareTo(o2.getId());
                        }
                    } else {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Event> getEvents(String text,
                                 Long[] categories,
                                 Boolean paid,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 boolean onlyAvailable) {
        log.debug("Getting events by: " +
                "\ntext: {}, " +
                "\ncategories: {}, " +
                "\npaid: {}, " +
                "\nrangeStart: {}, " +
                "\nrangeEnd: {}, " +
                "\nonlyAvailable: {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        return eventRepository.findEventsByPublicFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
    }

    public EventFullDto getEventFullDto(long eventId) {
        Event event = getEvent(eventId);
        String uri = "/events/" + event.getId();
        int views = (Integer) eventClient.getViews(uri);
        return EventFullDto.construct(event, views);
    }

    public Event getEvent(long eventId) {
        log.debug("Getting event id: {}", eventId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        }
        return optionalEvent.get();
    }
}
