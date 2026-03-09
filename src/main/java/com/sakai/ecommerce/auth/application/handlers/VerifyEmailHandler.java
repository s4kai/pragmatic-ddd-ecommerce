package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.VerifyEmailCommand;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.InvalidVerifyEmailToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailHandler {
    private final UserRepository userRepository;

    public void handle(VerifyEmailCommand command) {
        var user = userRepository.findByEmailToken(command.token())
            .orElseThrow(InvalidVerifyEmailToken::new);

        user.verifyEmail(command.token());
        userRepository.save(user);
    }
}
