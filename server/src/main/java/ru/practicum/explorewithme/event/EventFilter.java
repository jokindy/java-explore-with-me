package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.request.Request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class EventFilter {

    public static Event filterByCategoryId(Event event, Long[] categories) {
        for (Long catId : categories) {
            if (event.getCategoryId() == catId) {
                return event;
            }
        }
        return null;
    }

    public static Event filterByAvailable(Event event, boolean isAvailable) {
        if (isAvailable) {
            List<Request> confirmedRequests = event.getRequests();
            if (confirmedRequests.size() < event.getParticipantLimit()) {
                return event;
            } else {
                return null;
            }
        } else {
            return event;
        }
    }

    public static Event filterByStartAndEnd(Event event, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        LocalDateTime eventDate = event.getEventDate();
        if (rangeStart != null && rangeEnd != null) {
            return (eventDate.isAfter(rangeStart) && eventDate.isBefore(rangeEnd)) ? event : null;
        } else if (rangeStart != null) {
            return eventDate.isAfter(rangeStart) ? event : null;
        } else if (rangeEnd != null) {
            return eventDate.isBefore(rangeEnd) ? event : null;
        } else {
            return event;
        }
    }


    public static Event filterByState(Event event, EventState[] eventStates) {
        for (EventState state : eventStates) {
            if (event.getState().equals(state)) {
                return event;
            }
        }
        return null;
    }

    public static Event filterByText(Event event, String text) {
        String annotation = event.getAnnotation().toLowerCase(Locale.ROOT);
        String description = event.getDescription().toLowerCase(Locale.ROOT);
        if (text != null) {
            text = text.toLowerCase(Locale.ROOT);
            if (annotation.contains(text) || description.contains(text)) {
                return event;
            } else {
                return null;
            }
        } else {
            return event;
        }
    }
}
