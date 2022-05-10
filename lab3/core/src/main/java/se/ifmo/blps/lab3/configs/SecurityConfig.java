package se.ifmo.blps.lab3.configs;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.ifmo.blps.lab3.filters.JwtTokenAuthErrorHandlingFilter;
import se.ifmo.blps.lab3.filters.JwtTokenAuthFilter;
import se.ifmo.blps.lab3.utils.JwtUtil;

@Configuration
@Profile("!postgres")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userService;
  private final AbstractJaasAuthenticationProvider jaasAuthenticationProvider;
  private final JwtUtil jwtUtil;

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.addFilterAfter(
            new JwtTokenAuthFilter(userService, jwtUtil),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtTokenAuthErrorHandlingFilter(), JwtTokenAuthFilter.class)
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(STATELESS)
        .and()
        .headers()
        .frameOptions()
        .sameOrigin()
        .and()
        .authorizeRequests()
        .antMatchers("/h2-console/**")
        .permitAll();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(jaasAuthenticationProvider);
  }
}
