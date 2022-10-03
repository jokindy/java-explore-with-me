package ru.practicum.explorewithme.comment.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class CommentPublicController {

    private final CommentPublicManager commentPublicManager;

    /**
     * Метод возвращает только опубликованные комменты к событию с сортировкой по полезности или дате
     */
    
    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable long eventId,
                                             @RequestParam(defaultValue = "ID") CommentSort sort,
                                             @RequestParam(required = false) Boolean positive,
                                             @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.debug("Get event id: {}'s comments", eventId);
        return commentPublicManager.getComments(eventId, sort, positive, from, size);
    }

    /**
     * Метод возвращает один опубликованный коммент к событию
     */

    @GetMapping("/{eventId}/comments/{commentId}")
    public CommentDto getEventComment(@PathVariable long eventId,
                                      @PathVariable long commentId) {
        log.debug("Get event id: {}'s comment id: {}", eventId, commentId);
        return commentPublicManager.getComment(eventId, commentId);
    }
}
