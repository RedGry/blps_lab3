package se.ifmo.blps.lab3.runners;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static se.ifmo.blps.lab3.domains.Role.ROLE_ADMIN;
import static se.ifmo.blps.lab3.domains.Role.ROLE_MANAGER;
import static se.ifmo.blps.lab3.domains.Role.ROLE_USER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.blps.lab3.domains.User;
import se.ifmo.blps.lab3.repositories.UserRepository;

@Component
@Order(HIGHEST_PRECEDENCE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserDataGenerator implements ApplicationRunner {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    for (final var user : getUsers()) {
      if (!userRepository.existsByEmail(user.getEmail())) {
        log.info("Saving user={}", user);
        userRepository.save(user);
      }
    }
  }

  private List<User> getUsers() {
    return List.of(
        User.builder()
            .email("user1@email.com")
            .password(passwordEncoder.encode("user1pass"))
            .phoneNumber("+79211679562")
            .role(ROLE_USER)
            .build(),
        User.builder()
            .email("user2@email.com")
            .password(passwordEncoder.encode("user2pass"))
            .phoneNumber("+79057586108")
            .role(ROLE_USER)
            .build(),
        User.builder()
            .email("user3@email.com")
            .password(passwordEncoder.encode("user3pass"))
            .phoneNumber("+79211679665")
            .role(ROLE_USER)
            .build(),
        User.builder()
            .email("admin@email.com")
            .password(passwordEncoder.encode("admin"))
            .phoneNumber("+79211679665")
            .role(ROLE_ADMIN)
            .build(),
        User.builder()
            .email("manager1@email.com")
            .password(passwordEncoder.encode("manager1pass"))
            .phoneNumber("+79211679665")
            .role(ROLE_MANAGER)
            .build(),
        User.builder()
            .email("manager2@email.com")
            .password(passwordEncoder.encode("manager2pass"))
            .phoneNumber("+79211679665")
            .role(ROLE_MANAGER)
            .build(),
        User.builder()
            .email("manager3@email.com")
            .password(passwordEncoder.encode("manager3pass"))
            .phoneNumber("+79211679665")
            .role(ROLE_MANAGER)
            .build());
  }
}
