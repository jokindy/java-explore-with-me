package ru.practicum.explorewithme.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
