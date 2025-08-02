package com.example.weesh.core.user.application;

import com.example.weesh.core.shared.identifier.UserId;
import com.example.weesh.core.user.domain.User;

public interface UserRepository {
    User save(User user);
    User findById(UserId id);
}