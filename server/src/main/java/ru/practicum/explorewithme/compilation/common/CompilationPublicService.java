package ru.practicum.explorewithme.compilation.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.CompilationRepo;
import ru.practicum.explorewithme.exception.ModelNotFoundException;
import ru.practicum.explorewithme.util.PageMaker;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationPublicService {

    private final CompilationRepo compilationRepo;
    private final PageMaker<Compilation> pageMaker;

    public Page<Compilation> getAll(boolean pinned, int from, int size) {
        log.debug("COMPILATION PUBLIC SERVICE - getting compilations");
        List<Compilation> compilations = compilationRepo.findAllByPinned(pinned);
        return pageMaker.getPage(from, size, compilations);
    }

    public Compilation getCompilation(long compId) {
        log.debug("COMPILATION PUBLIC SERVICE - getting compilation ID: {}", compId);
        checkCompilationId(compId);
        return compilationRepo.findById(compId).get();
    }

    public void checkCompilationId(long compId) {
        boolean isExists = compilationRepo.existsById(compId);
        if (!isExists) {
            log.warn("COMPILATION PUBLIC SERVICE - ModelNotFoundException");
            throw new ModelNotFoundException(compId, Compilation.class.getSimpleName());
        }
    }
}