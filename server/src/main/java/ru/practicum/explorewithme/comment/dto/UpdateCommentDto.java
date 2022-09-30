package ru.practicum.explorewithme.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UpdateCommentDto {

    private Long id;

    @NotNull
    private Boolean positive;

    @NotNull
    @Length(min = 20, max = 2000)
    private String content;
}
