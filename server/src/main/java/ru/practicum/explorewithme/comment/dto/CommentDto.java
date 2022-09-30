package ru.practicum.explorewithme.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.comment.CommentState;
import ru.practicum.explorewithme.event.dto.EventCommentDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private int useful;
    private boolean positive;
    private UserShortDto author;
    private EventCommentDto event;
    private CommentState state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
