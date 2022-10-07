package ru.practicum.explorewithme.event.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventMapper;
import ru.practicum.explorewithme.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventPublicApiManager {

    private final EventMapper eventMapper;
    private final EventClient eventClient;
    private final EventPublicService eventPublicService;

    public List<EventShortDto> getEvents(String text,
                                         Long[] categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         boolean onlyAvailable,
                                         EventSortKey sort,
                                         int from,
                                         int size) {
        List<Event> events = eventPublicService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        return events.stream()
                .map(event -> eventMapper.mapToShortDto(event, getViews(event.getId())))
                .sorted((o1, o2) -> {
                    if (sort != null) {
                        switch (sort) {
                            case EVENT_DATE:
                                return o2.getEventDate().compareTo(o1.getEventDate());
                            case VIEWS:
                                return o2.getViews().compareTo(o1.getViews());
                            default:
                                return o2.getId().compareTo(o1.getId());
                        }
                    } else {
                        return o2.getId().compareTo(o1.getId());
                    }
                })
                .skip((long) from * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventById(long eventId) {
        Event event = eventPublicService.getEvent(eventId);
        return eventMapper.mapToFullDto(event, getViews(eventId));
    }

    private int getViews(long eventId) {
        String uri = "/events/" + eventId;
        return (int) eventClient.getViews(uri);
    }
}
