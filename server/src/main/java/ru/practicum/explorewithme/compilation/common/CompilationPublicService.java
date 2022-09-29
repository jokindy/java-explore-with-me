package ru.practicum.explorewithme.compilation.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.CompilationRepository;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.Mapper;
import ru.practicum.explorewithme.util.PageMaker;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationPublicService {

    private final CompilationRepository compilationRepository;
    private final Mapper mapper;
    private final PageMaker<Compilation> pageMaker;

    public List<CompilationDto> getAll(boolean pinned, int from, int size) {
        log.debug("Getting compilations");
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned);
        return mapper.mapList(pageMaker.getPage(from, size, compilations).getContent(), CompilationDto.class);
    }

    public CompilationDto getCompilationDto(long compId) {
        Compilation compilation = getCompilation(compId);
        return mapper.map(compilation, CompilationDto.class);
    }

    public Compilation getCompilation(long compId) {
        log.debug("Getting compilation id: {}", compId);
        checkCompilationId(compId);
        return compilationRepository.findById(compId).get();
    }

    public void checkCompilationId(long compId) {
        boolean isExists = compilationRepository.existsById(compId);
        if (!isExists) {
            log.error("ModelNotFoundException");
            throw new ModelNotFoundException(compId, Compilation.class.getSimpleName());
        }
    }
}