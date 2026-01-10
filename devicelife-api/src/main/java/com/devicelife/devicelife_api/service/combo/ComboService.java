package com.devicelife.devicelife_api.service.combo;

import com.devicelife.devicelife_api.domain.combo.Combo;
import com.devicelife.devicelife_api.domain.combo.dto.request.ComboCreateRequestDto;
import com.devicelife.devicelife_api.domain.combo.dto.response.ComboCreateResponseDto;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.combo.ComboRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComboService {

    private final ComboRepository comboRepository;
    private final EntityManager em;

    @Transactional
    public ComboCreateResponseDto createCombo(ComboCreateRequestDto request) {
        Long userId = request.getUserId();

        User user = em.find(User.class, userId);
        if (user == null) {
            throw new EntityNotFoundException("존재하지 않는 userId: " + userId);
        }

        Combo combo = Combo.builder()
                .user(user)
                .comboName(request.getComboName())
                .build();

        Combo saved = comboRepository.save(combo);

        return ComboCreateResponseDto.builder()
                .comboId(saved.getComboId())
                .comboName(saved.getComboName())
                .build();
    }
}
