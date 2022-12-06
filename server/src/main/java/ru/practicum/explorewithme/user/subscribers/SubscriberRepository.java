package ru.practicum.explorewithme.user.subscribers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<SubscribeRequest, SubscribeRequestId> {
}
