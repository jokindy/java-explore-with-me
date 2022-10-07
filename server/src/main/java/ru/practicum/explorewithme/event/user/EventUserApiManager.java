package ru.practicum.explorewithme.event.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventClient;
import ru.practicum.explorewithme.event.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventUserApiManager {

    private final EventClient eventClient;
    private final EventMapper eventMapper;
    private final EventUserService eventUserService;

    public EventFullDto postEvent(NewEventDto eventDto, long userId) {
        Event event = eventMapper.map(eventDto, userId);
        eventUserService.save(event);
        return eventMapper.mapToFullDto(event, getViews(event.getId()));
    }

    public List<EventShortDto> getEvents(long userId, int from, int size) {
        List<Event> events = eventUserService.getUserEvents(userId, from, size);
        return events.stream()
                .map(event -> eventMapper.mapToShortDto(event, getViews(event.getId())))
                .collect(Collectors.toList());
    }

    public EventFullDto patchEventsByUser(UpdateEventRequest eventDto, long userId) {
        Event event = eventUserService.getEventById(eventDto.getEventId());
        eventMapper.map(eventDto, event);
        eventUserService.patchEvent(event, userId);
        return eventMapper.mapToFullDto(event, getViews(event.getId()));
    }

    public EventFullDto getEventByEventId(long userId, long eventId) {
        Event event = eventUserService.getEventByOwnerId(userId, eventId);
        return eventMapper.mapToFullDto(event, getViews(eventId));
    }

    public EventFullDto cancelEventByEventId(long userId, long eventId) {
        Event event = eventUserService.cancelEvent(userId, eventId);
        return eventMapper.mapToFullDto(event, getViews(eventId));
    }

    private int getViews(long eventId) {
        String uri = "/events/" + eventId;
        return (int) eventClient.getViews(uri);
    }
}
