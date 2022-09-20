package ru.practicum.explorewithme.compilation.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {

    private final CompilationPublicService compilationPublicService;
    private final Mapper mapper;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.debug("COMPILATION PUBLIC CONTROLLER - getting compilations by pin: {}", pinned);
        List<Compilation> list = compilationPublicService.getAll(pinned, from, size).getContent();
        return mapper.mapList(list, CompilationDto.class);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.debug("COMPILATION PUBLIC CONTROLLER - getting compilation id: {}", compId);
        Compilation compilation = compilationPublicService.getCompilation(compId);
        return mapper.map(compilation, CompilationDto.class);
    }


}
