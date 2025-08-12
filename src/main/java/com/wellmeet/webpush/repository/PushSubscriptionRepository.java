package com.wellmeet.webpush.repository;

import com.wellmeet.webpush.domain.PushSubscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    List<PushSubscription> findByUserId(String userId);

    void deleteByUserIdAndEndpoint(String userId, String endpoint);

    boolean existsByUserIdAndEndpoint(String userId, String endpoint);
}
