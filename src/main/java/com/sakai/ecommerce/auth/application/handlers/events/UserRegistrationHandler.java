package com.sakai.ecommerce.auth.application.handlers.events;

import com.sakai.ecommerce.auth.domain.EmailVerificationRepository;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.events.UserRegisteredEvent;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class UserRegistrationHandler {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationHandler.class);

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistration(UserRegisteredEvent event) {
        var email = event.getEmail();
        var emailVerification = emailVerificationRepository.findByUserId(event.getUserId())
                .orElseThrow(() -> new BusinessError("User not found"));


        //TODO: send to email queue service
        log.info("Sending validate email to {}", email);
        log.debug("Send user verify token: {}, to email: {}",
            emailVerification.getToken(),
            email
        );
    }
}
