package ru.practicum.explorewithme.event.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventFilter;
import ru.practicum.explorewithme.event.EventRepo;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventPublicService {

    private final EventRepo eventRepo;
    private final PageMaker<Event> pageMaker;

    public List<Event> getEvents(String text,
                                 Long[] categories,
                                 Boolean paid,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 boolean onlyAvailable,
                                 int from,
                                 int size) {
        log.debug("EVENT PUBLIC SERVICE - getting events by: " +
                "\ntext: {}, " +
                "\ncategories: {}, " +
                "\npaid: {}, " +
                "\nrangeStart: {}, " +
                "\nrangeEnd: {}, " +
                "\nonlyAvailable: {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        List<Event> events;
        if (paid != null) {
            events = eventRepo.findAllByStateAndPaid(EventState.PUBLISHED, paid);
        } else {
            events = eventRepo.findAllByState(EventState.PUBLISHED);
        }
        List<Event> list = events.stream()
                .map(event -> EventFilter.filterByStartAndEnd(event, rangeStart, rangeEnd))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByCategoryId(event, categories))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByAvailable(event, onlyAvailable))
                .filter(Objects::nonNull)
                .map(event -> EventFilter.filterByText(event, text))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return pageMaker.getPage(from, size, list).getContent();
    }

    public Event getEvent(long eventId) {
        log.debug("EVENT PUBLIC SERVICE - getting event id: {}", eventId);
        Optional<Event> optionalEvent = eventRepo.findById(eventId);
        if (optionalEvent.isEmpty()) {
            log.warn("EVENT PUBLIC SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        }
        return optionalEvent.get();
    }
}
