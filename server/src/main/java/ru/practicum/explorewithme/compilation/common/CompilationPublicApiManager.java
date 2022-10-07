package ru.practicum.explorewithme.compilation.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CompilationPublicApiManager {

    private final Mapper mapper;
    private final CompilationPublicService compilationPublicService;

    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationPublicService.getAll(pinned, from, size);
        return mapper.mapList(compilations, CompilationDto.class);
    }

    public CompilationDto getCompilationById(long compId) {
        Compilation compilation = compilationPublicService.getCompilation(compId);
        return mapper.map(compilation, CompilationDto.class);
    }

}
