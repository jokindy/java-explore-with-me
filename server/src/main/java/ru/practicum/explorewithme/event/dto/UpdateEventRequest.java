package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateEventRequest {

    @NotNull
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Long eventId;

    private boolean paid;

    @NotNull
    private Integer participantLimit;

    @Length(min = 3, max = 120)
    private String title;
}
