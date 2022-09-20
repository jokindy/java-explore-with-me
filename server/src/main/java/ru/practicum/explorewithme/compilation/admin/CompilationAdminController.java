package ru.practicum.explorewithme.compilation.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.util.Mapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;
    private final Mapper mapper;

    @PostMapping()
    public CompilationDto postCompilationByAdmin(@RequestBody NewCompilationDto compilationDto) {
        log.debug("COMPILATION ADMIN CONTROLLER - saving new compilation to DB");
        List<Long> eventsId = compilationDto.getEvents();
        Compilation compilation = mapper.map(compilationDto, Compilation.class);
        compilationAdminService.save(compilation, eventsId);
        return mapper.map(compilation, CompilationDto.class);
    }

    @DeleteMapping("/{compId}")
    public String deleteCompilation(@PathVariable long compId) {
        log.debug("COMPILATION ADMIN CONTROLLER - deleting compilation id: {}", compId);
        compilationAdminService.deleteCompilation(compId);
        return String.format("Compilation id: %s is deleted", compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public String deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.debug("COMPILATION ADMIN CONTROLLER - deleting event id: {} from compilation id: {}", compId, eventId);
        compilationAdminService.handleEventId(compId, eventId, true);
        return String.format("Event id: %s is deleted from compilation id: %s", eventId, compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public String addEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.debug("COMPILATION ADMIN CONTROLLER - adding event id: {} to compilation id: {}", compId, eventId);
        compilationAdminService.handleEventId(compId, eventId, false);
        return String.format("Event id: %s is added to compilation id: %s", eventId, compId);
    }

    @DeleteMapping("/{compId}/pin")
    public String unpinCompilation(@PathVariable long compId) {
        log.debug("COMPILATION ADMIN CONTROLLER - unpin compilation id: {}", compId);
        compilationAdminService.handleCompilationPin(compId, true);
        return String.format("Compilation id: %s is unpinned", compId);
    }

    @PatchMapping("/{compId}/pin")
    public String pinCompilation(@PathVariable long compId) {
        log.debug("COMPILATION ADMIN CONTROLLER - pin compilation id: {}", compId);
        compilationAdminService.handleCompilationPin(compId, false);
        return String.format("Compilation id: %s is pinned", compId);
    }
}
