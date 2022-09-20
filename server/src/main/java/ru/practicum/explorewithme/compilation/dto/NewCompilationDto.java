package ru.practicum.explorewithme.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto {

    private Long id;

    @NotNull
    @Length(min = 5, max = 120)
    private String title;

    private boolean pinned;

    private List<Long> events;
}
