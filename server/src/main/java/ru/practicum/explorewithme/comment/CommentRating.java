package ru.practicum.explorewithme.comment;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Embeddable
@NoArgsConstructor
public class CommentRating {

    @Column(name = "user_id", table = "comment_rating")
    private Long userId;

    @Column(name = "is_like", table = "comment_rating")
    private boolean isLike;
}
