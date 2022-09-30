package ru.practicum.explorewithme.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.comment.CommentState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewCommentDto {

    @NotNull
    @Length(min = 20, max = 2000)
    private String content;

    private LocalDateTime created = LocalDateTime.now();

    @NotNull
    private Boolean positive;

    private CommentState state = CommentState.PENDING;
}
