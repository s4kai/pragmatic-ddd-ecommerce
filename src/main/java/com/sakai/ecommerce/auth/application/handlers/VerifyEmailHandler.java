package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.VerifyEmailCommand;
import com.sakai.ecommerce.auth.domain.EmailVerification;
import com.sakai.ecommerce.auth.domain.EmailVerificationRepository;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.InvalidVerifyEmailToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyEmailHandler {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void handle(VerifyEmailCommand command) {
        EmailVerification verification = emailVerificationRepository.findByToken(command.token())
            .orElseThrow(InvalidVerifyEmailToken::new);

        verification.verify(command.token());
        emailVerificationRepository.save(verification);

        var user = userRepository.findById(verification.getUserId())
            .orElseThrow(InvalidVerifyEmailToken::new);

        user.activateAccount();
        userRepository.save(user);
    }
}
