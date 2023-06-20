package com.pocketeasy.pocketeasy.repos;

import com.pocketeasy.pocketeasy.domain.Subscription;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
