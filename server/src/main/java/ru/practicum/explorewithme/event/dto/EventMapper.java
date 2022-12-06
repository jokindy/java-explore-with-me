package ru.practicum.explorewithme.event.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.EventState;

import java.time.LocalDateTime;

@Component
public class EventMapper extends ModelMapper {

    public EventMapper() {
        setUp();
    }

    public Event map(NewEventDto eventDto, long userId) {
        Event event = super.map(eventDto, Event.class);
        event.setCategoryId(eventDto.getCategory());
        event.setInitiatorId(userId);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return event;
    }


    public EventFullDto mapToFullDto(Event event, int views) {
        EventFullDto eventDto = super.map(event, EventFullDto.class);
        long confirmedRequests = event.getConfirmedRequests();
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setViews(views);
        return eventDto;
    }

    public EventShortDto mapToShortDto(Event event, int views) {
        EventShortDto eventDto = super.map(event, EventShortDto.class);
        long confirmedRequests = event.getConfirmedRequests();
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setViews(views);
        return eventDto;
    }

    public void map(UpdateEventRequest eventRequest, Event event) {
        super.map(eventRequest, event);
        event.setId(eventRequest.getEventId());
        event.setCategoryId(eventRequest.getCategory());
    }

    private void setUp() {
        this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
}