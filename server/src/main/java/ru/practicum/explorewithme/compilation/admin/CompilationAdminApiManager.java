package ru.practicum.explorewithme.compilation.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CompilationAdminApiManager {

    private final Mapper mapper;
    private final CompilationAdminService compilationAdminService;

    public CompilationDto postCompilation(NewCompilationDto compilationDto) {
        List<Long> eventsId = compilationDto.getEvents();
        Compilation compilation = mapper.map(compilationDto, Compilation.class);
        compilationAdminService.save(compilation, eventsId);
        return mapper.map(compilation, CompilationDto.class);
    }

    public void deleteCompilation(long compId) {
        compilationAdminService.deleteCompilation(compId);
    }

    public void handleEventId(long compId, long eventId, boolean isDeleting) {
        compilationAdminService.handleEventId(compId, eventId, isDeleting);
    }

    public void handleCompilationPin(long compId, boolean isPin) {
        compilationAdminService.handleCompilationPin(compId, isPin);
    }
}
