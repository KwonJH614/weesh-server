package com.example.weesh.data.jpa.advice;

import com.example.weesh.core.advice.application.AdviceRepository;
import com.example.weesh.core.advice.domain.Advice;
import com.example.weesh.core.foundation.enums.AdviceStatus;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.data.jpa.advice.mapper.AdviceMapper;
import com.example.weesh.data.jpa.user.JpaUserRepositoryImpl;
import com.example.weesh.data.jpa.user.UserEntity;
import com.example.weesh.data.jpa.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdviceRepositoryImpl implements AdviceRepository {
    private final JpaAdviceRepositoryImpl adviceRepository;
    private final JpaUserRepositoryImpl userRepository;
    private final UserMapper userMapper;
    private final AdviceMapper adviceMapper;

    @Override
    public Advice save(Advice advice) {
        UserEntity userEntity = null;
        if (advice.getUserId() != null) {
            userEntity = userRepository.findById(advice.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }
        AdviceEntity entity = adviceMapper.toEntity(advice, userEntity);
        AdviceEntity savedEntity = adviceRepository.save(entity);

        return adviceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Advice> findById(Long id) {
        return adviceRepository.findById(id).map(adviceMapper::toDomain);
    }

    @Override
    public List<Advice> findAll() {
        return adviceRepository.findAll().stream()
                .map(adviceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByDateAndTime(String desiredDate, String desiredTime) {
        return adviceRepository.existsByDesiredDateAndDesiredTimeAndStatusNot(desiredDate, desiredTime, AdviceStatus.DELETED);
    }
}