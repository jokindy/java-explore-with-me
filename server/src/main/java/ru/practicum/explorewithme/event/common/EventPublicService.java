package ru.practicum.explorewithme.event.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.ModelNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class EventPublicService {

    private final EventRepository eventRepository;

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

    public Event getEvent(long eventId) {
        log.debug("Getting event id: {}", eventId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        return optionalEvent.orElseThrow(() -> {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(eventId, Event.class.getSimpleName());
        });
    }
}
