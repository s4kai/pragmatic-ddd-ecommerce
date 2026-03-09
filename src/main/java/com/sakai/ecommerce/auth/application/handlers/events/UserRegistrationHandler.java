package com.sakai.ecommerce.auth.application.handlers.events;

import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.events.UserRegisteredEvent;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegistrationHandler {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationHandler.class);

    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistration(UserRegisteredEvent event) {
        var email = event.getEmail();

        var user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BusinessError("User not found"));

        user.generateEmailVerificationToken();
        userRepository.save(user);

        //TODO: send to email queue service

        log.info("Sending validate email to {}", email);

        log.debug("Send user verify token: {}, to email: {}",
            user.getEmailVerificationToken(),
            email
        );
    }
}
