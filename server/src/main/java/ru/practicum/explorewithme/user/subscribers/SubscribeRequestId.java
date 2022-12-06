package ru.practicum.explorewithme.user.subscribers;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubscribeRequestId implements Serializable {

    private Long userId;
    private Long subscriberId;
}
