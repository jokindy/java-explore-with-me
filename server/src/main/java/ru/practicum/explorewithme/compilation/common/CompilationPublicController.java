package ru.practicum.explorewithme.compilation.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {

    private final CompilationPublicManager compilationPublicManager;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.debug("Getting compilations by pin: {}", pinned);
        return compilationPublicManager.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.debug("Getting compilation id: {}", compId);
        return compilationPublicManager.getCompilationById(compId);
    }
}