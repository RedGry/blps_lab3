package se.ifmo.blps.lab3.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Configuration
public class StompSessionConfig {

  @Bean
  public WebSocketStompClient webSocketStompClient(final ObjectMapper objectMapper) {
    final var webSocketClient = new JettyWebSocketClient();

    final var stompClient = new WebSocketStompClient(webSocketClient);
    stompClient.setTaskScheduler(stompSessionTaskScheduler());
    stompClient.setReceiptTimeLimit(5000L);

    final var converter = new MappingJackson2MessageConverter();
    converter.setObjectMapper(objectMapper);

    stompClient.setMessageConverter(converter);

    return stompClient;
  }

  @Bean
  public ThreadPoolTaskScheduler stompSessionTaskScheduler() {
    final var taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setRemoveOnCancelPolicy(true);
    taskScheduler.setPoolSize(5);
    return taskScheduler;
  }
}
