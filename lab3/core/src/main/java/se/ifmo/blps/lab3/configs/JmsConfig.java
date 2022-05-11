package se.ifmo.blps.lab3.configs;

import static org.springframework.jms.support.converter.MessageType.BYTES;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.web.client.RestTemplate;
import se.ifmo.blps.lab3.dtos.AuthorizationDto;

@Configuration
@EnableJms
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class JmsConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public MessageConverter jsonMessageConverter() {
    final var converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(objectMapper);
    converter.setTargetType(BYTES);
    converter.setTypeIdPropertyName("JMSType");
    converter.setTypeIdMappings(
        Map.of(AuthorizationDto.class.getCanonicalName(), AuthorizationDto.class));
    return converter;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}

