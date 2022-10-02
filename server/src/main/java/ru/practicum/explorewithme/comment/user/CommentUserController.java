package ru.practicum.explorewithme.comment.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.Comment;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.util.Mapper;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class CommentUserController {

    private final CommentUserService commentUserService;
    private final Mapper mapper;

    /**
     * метод получает все комменты к событию пользователя
     */

    @GetMapping("/{userId}/events/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable long userId,
                                             @PathVariable long eventId) {
        log.debug("Get event id: {}'s (owner id: {}) comments", eventId, userId);
        List<Comment> comments = commentUserService.getEventComments(userId, eventId);
        return mapper.mapList(comments, CommentDto.class);
    }

    /**
     * метод постит коммент к посещенному событию, к своему событию оставлять комменты нельзя
     */

    @PostMapping("/{userId}/events/{eventId}/comments")
    public CommentDto postCommentToEvent(@Valid @RequestBody NewCommentDto commentDto,
                                         @PathVariable long userId,
                                         @PathVariable long eventId) {
        log.debug("Post new comment to event id: {} by user id: {}", eventId, userId);
        Comment comment = Comment.toEntity(commentDto, userId, eventId);
        commentUserService.save(comment, userId, eventId);
        return mapper.map(comment, CommentDto.class);
    }

    /**
     * метод обновляет уже созданный коммент, ограничение - коммент должен быть на модерации
     */

    @PutMapping("/{userId}/events/{eventId}/comments")
    public CommentDto updateEventComment(@Valid @RequestBody UpdateCommentDto dto,
                                         @PathVariable long userId,
                                         @PathVariable long eventId) {
        log.debug("Update comment to event id: {} by user id: {}", eventId, userId);
        Comment updatedComment = commentUserService.update(dto, userId, eventId);
        return mapper.map(updatedComment, CommentDto.class);
    }

    /**
     * метод удаляет уже созданный коммент
     */

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public String deleteEventComment(@PathVariable long userId,
                                     @PathVariable long eventId,
                                     @PathVariable long commentId) {
        log.debug("Delete comment id: {} by user id: {} to event id {}", commentId, userId, eventId);
        commentUserService.deleteComment(userId, eventId, commentId);
        return String.format("Comment id: %s is deleted", commentId);
    }
}
