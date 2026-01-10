package com.devicelife.devicelife_api.service.onboarding;

import com.devicelife.devicelife_api.domain.onboarding.dto.request.OnboardingCompleteRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final EntityManager em;

    @Transactional
    public void complete(OnboardingCompleteRequestDto request) {
        Long userId = request.getUserId();

        int updated = em.createQuery(
                        "update User u set u.onboardingCompleted = true where u.userId = :userId"
                )
                .setParameter("userId", userId)
                .executeUpdate();

        if (updated == 0) {
            throw new EntityNotFoundException("존재하지 않는 userId: " + userId);
        }
    }
}