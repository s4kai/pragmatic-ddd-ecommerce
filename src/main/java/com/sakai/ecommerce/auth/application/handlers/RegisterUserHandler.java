package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.RegisterUserCommand;
import com.sakai.ecommerce.auth.application.dto.UserResponse;
import com.sakai.ecommerce.auth.application.services.PasswordEncoder;
import com.sakai.ecommerce.auth.domain.User;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.UserExists;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Transactional
    public UserResponse handle(RegisterUserCommand command) {
        var email = command.email();

        userRepository.findByEmail(email)
                .ifPresent(u -> { throw new UserExists(); });

        String hashedPassword = passwordEncoder.encode(command.password());

        User user = User.createLocalUser(
            email,
            hashedPassword
        );

        userRepository.save(user);
        eventPublisher.publish(user);

        return new UserResponse(user.getId(), user.getEmail());
    }
}
