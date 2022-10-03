package ru.practicum.explorewithme.comment.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class CommentUserController {

    private final CommentUserManager commentUserManager;

    /**
     * метод получает все комменты к событию пользователя
     */

    @GetMapping("/{userId}/events/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable long userId,
                                             @PathVariable long eventId) {
        log.debug("Get event id: {}'s (owner id: {}) comments", eventId, userId);
        return commentUserManager.getEventComments(userId, eventId);
    }

    /**
     * метод постит коммент к посещенному событию, к своему событию оставлять комменты нельзя
     */

    @PostMapping("/{userId}/events/{eventId}/comments")
    public CommentDto postCommentToEvent(@Valid @RequestBody NewCommentDto commentDto,
                                         @PathVariable long userId,
                                         @PathVariable long eventId) {
        log.debug("Post new comment to event id: {} by user id: {}", eventId, userId);
        return commentUserManager.postCommentToEvent(commentDto, userId, eventId);
    }

    /**
     * метод обновляет уже созданный коммент, ограничение - коммент должен быть на модерации
     */

    @PutMapping("/{userId}/events/{eventId}/comments")
    public CommentDto updateEventComment(@Valid @RequestBody UpdateCommentDto dto,
                                         @PathVariable long userId,
                                         @PathVariable long eventId) {
        log.debug("Update comment to event id: {} by user id: {}", eventId, userId);
        return commentUserManager.updateComment(dto, userId, eventId);
    }

    /**
     * метод удаляет уже созданный коммент
     */

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public String deleteEventComment(@PathVariable long userId,
                                     @PathVariable long eventId,
                                     @PathVariable long commentId) {
        log.debug("Delete comment id: {} by user id: {} to event id {}", commentId, userId, eventId);
        commentUserManager.deleteComment(userId, eventId, commentId);
        return String.format("Comment id: %s is deleted", commentId);
    }

    /**
     * Метод ставит лайк комменту
     */

    @PatchMapping("/{userId}/events/{eventId}/comments/{commentId}/like")
    public String putLike(@PathVariable long userId,
                          @PathVariable long eventId,
                          @PathVariable long commentId) {
        log.debug("Increase useful for event id: {}'s comment id: {}", eventId, commentId);
        commentUserManager.handleLike(userId, eventId, commentId, true);
        return String.format("You add like to comment id: %s", commentId);
    }

    /**
     * Метод ставит дизлайк комменту
     */

    @PatchMapping("/{userId}/events/{eventId}/comments/{commentId}/dislike")
    public String removeUseful(@PathVariable long userId,
                               @PathVariable long eventId,
                               @PathVariable long commentId) {
        log.debug("Decrease useful for event id: {}'s comment id: {}", eventId, commentId);
        commentUserManager.handleLike(userId, eventId, commentId, false);
        return String.format("You add dislike to comment id: %s", commentId);
    }
}
