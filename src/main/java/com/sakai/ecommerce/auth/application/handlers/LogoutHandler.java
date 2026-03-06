package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.LogoutCommand;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.InvalidCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutHandler {
    private final UserRepository userRepository;

    public void handler(LogoutCommand command){
        var user = userRepository.findById(command.userId())
                .orElseThrow(InvalidCredentials::new);

        user.logout();
        userRepository.save(user);
    }
}
