package ru.practicum.explorewithme.compilation.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.compilation.CompilationRepository;
import ru.practicum.explorewithme.compilation.common.CompilationPublicService;
import ru.practicum.explorewithme.event.Event;
import ru.practicum.explorewithme.event.user.EventUserService;
import ru.practicum.explorewithme.exception.ForbiddenException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationAdminService {

    private final CompilationPublicService compilationPublicService;
    private final CompilationRepository compilationRepository;
    private final EventUserService eventService;

    public Compilation save(Compilation compilation, List<Long> eventsId) {
        log.debug("Saving compilation to DB: {} with events: {}", compilation, eventsId);
        Set<Event> events = eventsId.stream()
                .map(eventService::getEventById)
                .collect(Collectors.toSet());
        compilation.setEvents(events);
        return compilationRepository.save(compilation);
    }

    public void deleteCompilation(long compId) {
        log.debug("Deleting compilation ID: {}", compId);
        compilationPublicService.checkCompilationId(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public void handleEventId(long compId, long eventId, boolean isDeleting) {
        log.debug("Handle event: {} in compilation id: {}, is deleting - {}",
                eventId, compId, isDeleting);
        Compilation compilation = compilationPublicService.getCompilation(compId);
        Event event = eventService.getEventById(eventId);
        if (isDeleting) {
            if (compilation.getEvents().contains(event)) {
                compilationRepository.deleteEventFromCompilation(compId, eventId);
            }
        } else {
            if (!compilation.getEvents().contains(event)) {
                compilationRepository.addEventToCompilation(compId, eventId);
            }
        }
    }

    @Transactional
    public void handleCompilationPin(long compId, boolean isPinned) {
        log.debug("Handle compilation id: {} pinned, is pinned - {}", compId, isPinned);
        Compilation compilation = compilationPublicService.getCompilation(compId);
        boolean pinned = compilation.isPinned();
        if (isPinned) {
            if (pinned) {
                compilationRepository.setCompilationPinned(false, compId);
            } else {
                log.error("UpdateIsForbiddenException");
                throw ForbiddenException.updateUnpinnedCompilation(compId);
            }
        } else {
            if (!pinned) {
                compilationRepository.setCompilationPinned(true, compId);
            } else {
                log.error("UpdateIsForbiddenException");
                throw ForbiddenException.updatePinnedCompilation(compId);
            }
        }
    }
}