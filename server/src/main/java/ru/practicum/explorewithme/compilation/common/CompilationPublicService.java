package ru.practicum.explorewithme.compilation.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.CompilationRepository;
import ru.practicum.explorewithme.exception.ModelNotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationPublicService {

    private final CompilationRepository compilationRepository;

    public List<Compilation> getAll(boolean pinned, int from, int size) {
        log.debug("Getting compilations");
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        return compilationRepository.findAllByPinned(pinned, pageable);
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