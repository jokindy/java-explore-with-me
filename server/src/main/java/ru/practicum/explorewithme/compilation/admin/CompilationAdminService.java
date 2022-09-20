package ru.practicum.explorewithme.compilation.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.CompilationRepo;
import ru.practicum.explorewithme.compilation.common.CompilationPublicService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.UpdateIsForbiddenException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationAdminService {

    private final CompilationPublicService compilationPublicService;
    private final EventUserService eventService;
    private final CompilationRepo compilationRepo;


    public Compilation save(Compilation compilation, List<Long> eventsId) {
        log.debug("COMPILATION ADMIN SERVICE - saving compilation to DB: {} with events: {}", compilation, eventsId);
        Set<Event> events = eventsId.stream()
                .map(eventService::getEventById)
                .collect(Collectors.toSet());
        compilation.setEvents(events);
        return compilationRepo.save(compilation);
    }

    public void deleteCompilation(long compId) {
        log.debug("COMPILATION ADMIN SERVICE - deleting compilation ID: {}", compId);
        compilationPublicService.checkCompilationId(compId);
        compilationRepo.deleteById(compId);
    }

    @Transactional
    public void handleEventId(long compId, long eventId, boolean isDeleting) {
        log.debug("COMPILATION ADMIN SERVICE - handle event: {} in compilation id: {}, is deleting - {}",
                eventId, compId, isDeleting);
        Compilation compilation = compilationPublicService.getCompilation(compId);
        Event event = eventService.getEventById(eventId);
        if (isDeleting) {
            if (compilation.getEvents().contains(event)) {
                compilationRepo.deleteEventFromCompilation(compId, eventId);
            }
        } else {
            if (!compilation.getEvents().contains(event)) {
                compilationRepo.addEventToCompilation(compId, eventId);
            }
        }
    }

    @Transactional
    public void handleCompilationPin(long compId, boolean isDeleting) {
        log.debug("COMPILATION ADMIN SERVICE - handle compilation id: {} pinned, is pinned - {}", compId, isDeleting);
        Compilation compilation = compilationPublicService.getCompilation(compId);
        boolean pinned = compilation.isPinned();
        if (isDeleting) {
            if (pinned) {
                compilationRepo.setCompilationPinned(false, compId);
            } else {
                log.warn("COMPILATION ADMIN SERVICE - UpdateIsForbiddenException");
                throw new UpdateIsForbiddenException(String.format("Compilation id: %s is already unpinned", compId));
            }
        } else {
            if (!pinned) {
                compilationRepo.setCompilationPinned(true, compId);
            } else {
                log.warn("COMPILATION ADMIN SERVICE - UpdateIsForbiddenException");
                throw new UpdateIsForbiddenException(String.format("Compilation id: %s is already pinned", compId));
            }
        }
    }
}