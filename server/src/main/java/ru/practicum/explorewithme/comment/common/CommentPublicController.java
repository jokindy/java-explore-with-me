package ru.practicum.explorewithme.comment.common;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.util.Mapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
public class CommentPublicController {

    private final Mapper mapper;
    private final CommentPublicService commentPublicService;

    /**
     * Метод возвращает только опубликованные комменты к событию с сортировкой по полезности или дате
     */
    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable long eventId,
                                             @RequestParam(defaultValue = "ID") CommentSort sort,
                                             @RequestParam(required = false) Boolean positive,
                                             @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        List<Comment> comments = commentPublicService.getComments(eventId, from, size, sort, positive);
        return mapper.mapList(comments, CommentDto.class);
    }

    /**
     * Метод возвращает один опубликованный коммент к событию
     */

    @GetMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto getEventComment(@PathVariable long eventId,
                                      @PathVariable long commentId) {
        Comment comment = commentPublicService.getCommentByEventId(eventId, commentId);
        return mapper.map(comment, CommentDto.class);
    }

    /**
     * Метод ставит лайк комменту
     */

    @PatchMapping("/events/{eventId}/comments/{commentId}/like")
    public String addUseful(@PathVariable long eventId,
                            @PathVariable long commentId) {
        commentPublicService.handleUseful(eventId, commentId, true);
        return String.format("You add like to comment id: %s", commentId);
    }

    /**
     * Метод ставит дизлайк комменту
     */

    @PatchMapping("/events/{eventId}/comments/{commentId}/dislike")
    public String removeUseful(@PathVariable long eventId,
                               @PathVariable long commentId) {
        commentPublicService.handleUseful(eventId, commentId, false);
        return String.format("You add dislike to comment id: %s", commentId);
    }
}
