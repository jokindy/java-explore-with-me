package ru.practicum.explorewithme.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.dto.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
