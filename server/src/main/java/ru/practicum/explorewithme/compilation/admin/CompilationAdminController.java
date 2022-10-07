package ru.practicum.explorewithme.compilation.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminApiManager compilationAdminApiManager;

    @PostMapping
    public CompilationDto postCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.debug("Saving new compilation to DB");
        return compilationAdminApiManager.postCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public String deleteCompilation(@PathVariable long compId) {
        log.debug("Deleting compilation id: {}", compId);
        compilationAdminApiManager.deleteCompilation(compId);
        return String.format("Compilation id: %s is deleted", compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public String deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.debug("Deleting event id: {} from compilation id: {}", compId, eventId);
        compilationAdminApiManager.handleEventId(compId, eventId, true);
        return String.format("Event id: %s is deleted from compilation id: %s", eventId, compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public String addEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.debug("Adding event id: {} to compilation id: {}", compId, eventId);
        compilationAdminApiManager.handleEventId(compId, eventId, false);
        return String.format("Event id: %s is added to compilation id: %s", eventId, compId);
    }

    @DeleteMapping("/{compId}/pin")
    public String unpinCompilation(@PathVariable long compId) {
        log.debug("Unpin compilation id: {}", compId);
        compilationAdminApiManager.handleCompilationPin(compId, true);
        return String.format("Compilation id: %s is unpinned", compId);
    }

    @PatchMapping("/{compId}/pin")
    public String pinCompilation(@PathVariable long compId) {
        log.debug("Pin compilation id: {}", compId);
        compilationAdminApiManager.handleCompilationPin(compId, false);
        return String.format("Compilation id: %s is pinned", compId);
    }
}
