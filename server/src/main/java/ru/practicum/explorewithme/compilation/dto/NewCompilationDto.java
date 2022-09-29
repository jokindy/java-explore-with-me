package ru.practicum.explorewithme.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.compilation.Compilation;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto {

    @NotNull(message = "Name can't be null")
    @Length(min = 5, max = 120, message = "Name must be 5 characters long min and 120 max")
    private String title;

    private boolean pinned;

    private List<Long> events;

    public static Compilation toDomain(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.isPinned());
        return compilation;
    }
}
