package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.RegisterUserCommand;
import com.sakai.ecommerce.auth.application.dto.UserResponse;
import com.sakai.ecommerce.auth.application.services.PasswordEncoder;
import com.sakai.ecommerce.auth.domain.EmailVerification;
import com.sakai.ecommerce.auth.domain.EmailVerificationRepository;
import com.sakai.ecommerce.auth.domain.User;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.UserExists;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    @Transactional
    public UserResponse handle(RegisterUserCommand command) {
        var email = command.email();

        userRepository.findByEmail(email)
                .ifPresent(u -> { throw new UserExists(); });

        String hashedPassword = passwordEncoder.encode(command.password());
        User user = User.createLocalUser(email, hashedPassword);
        userRepository.save(user);

        EmailVerification verification = EmailVerification.create(user.getId());
        emailVerificationRepository.save(verification);

        eventPublisher.publish(user);

        return new UserResponse(user.getId(), user.getEmail());
    }
}
