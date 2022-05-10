package se.ifmo.blps.lab3.configs;

import static javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;

import java.util.Map;
import javax.security.auth.login.AppConfigurationEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.ifmo.blps.lab3.repositories.UserRepository;
import se.ifmo.blps.lab3.security.UserDetailsLoginModule;
import se.ifmo.blps.lab3.security.UserRepositoryAuthorityGranter;

@Configuration
public class JaasConfig {

  @Bean
  public javax.security.auth.login.Configuration configuration(
      final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
    final var configurationEntries =
        new AppConfigurationEntry[] {
          new AppConfigurationEntry(
              UserDetailsLoginModule.class.getCanonicalName(),
              REQUIRED,
              Map.of("userDetailsService", userDetailsService, "passwordEncoder", passwordEncoder))
        };
    return new InMemoryConfiguration(Map.of("SPRINGSECURITY", configurationEntries));
  }

  @Bean
  public AbstractJaasAuthenticationProvider jaasAuthenticationProvider(
      final javax.security.auth.login.Configuration configuration,
      final UserRepository userRepository) {
    final var defaultJaasAuthenticationProvider = new DefaultJaasAuthenticationProvider();
    defaultJaasAuthenticationProvider.setConfiguration(configuration);
    defaultJaasAuthenticationProvider.setAuthorityGranters(
        new AuthorityGranter[] {new UserRepositoryAuthorityGranter(userRepository)});
    return defaultJaasAuthenticationProvider;
  }
}
