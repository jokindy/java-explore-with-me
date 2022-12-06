package ru.practicum.explorewithme.user.subscribers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscribers")
@IdClass(SubscribeRequestId.class)
public class SubscribeRequest {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "subscriber_id")
    private Long subscriberId;

    @Enumerated(EnumType.STRING)
    private SubscriberStatus status;
}
