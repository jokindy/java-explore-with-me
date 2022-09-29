package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;

    public static EventShortDto construct(Event event, int views) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                CategoryDto.construct(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserShortDto.construct(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                views);
    }
}
