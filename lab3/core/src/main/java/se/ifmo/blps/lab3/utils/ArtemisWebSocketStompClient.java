package se.ifmo.blps.lab3.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import se.ifmo.blps.lab3.dtos.CreditNotificationDto;

@Component
@Slf4j
public class ArtemisWebSocketStompClient {

  private final WebSocketStompClient stompClient;
  private final StompHeaders connectHeaders;
  private final String connectUrl;
  private final AtomicReference<StompSession> stompSession;
  private final SessionHandler sessionHandler;

  @Autowired
  public ArtemisWebSocketStompClient(
      final WebSocketStompClient stompClient, final ArtemisProperties artemisProperties) {
    this.stompClient = stompClient;
    this.connectHeaders = new StompHeaders();
    connectHeaders.setLogin(artemisProperties.getUser());
    connectHeaders.setPasscode(artemisProperties.getPassword());
    this.connectUrl = artemisProperties.getBrokerUrl().replace("tcp://", "ws://");
    this.stompSession = new AtomicReference<>();
    this.sessionHandler = new SessionHandler();
  }

  @Transactional(propagation = SUPPORTS)
  public void sendMessage(
      final String destination, final CreditNotificationDto creditNotificationDto)
      throws URISyntaxException, ExecutionException, InterruptedException {
    var session = stompSession.get();
    if (session == null || !session.isConnected()) {
      stompSession.set(
          stompClient.connect(new URI(connectUrl), null, connectHeaders, sessionHandler).get());
    }

    session = stompSession.get();
    session.setAutoReceipt(true);

    final var headers = new StompHeaders();
    headers.setDestination(destination);
    headers.add("JMSType", creditNotificationDto.getClass().getCanonicalName());
    headers.add("destination-type", "ANYCAST");
    final var receiptable = session.send(headers, creditNotificationDto);

    log.debug("STOMP message sent");
    receiptable.addReceiptLostTask(() -> log.warn("STOMP receipt lost with"));
    receiptable.addReceiptTask(() -> log.debug("STOMP receipt received"));
  }

  private static class SessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void handleTransportError(final StompSession session, final Throwable exception) {
      log.error(
          "STOMP transport error with StompSession = {} and message = {}",
          session,
          exception.getMessage());
    }

    @Override
    public void handleFrame(final StompHeaders headers, final @Nullable Object payload) {
      log.debug("STOMP frame with StompHeaders = {} and payload = {}", headers, payload);
    }

    @Override
    public void handleException(
        final StompSession session,
        final @Nullable StompCommand command,
        final StompHeaders headers,
        final byte[] payload,
        final Throwable exception) {
      log.error(
          "STOMP exception occurred with StompSession = {} "
              + "with StompCommand = {} "
              + "and StompHeaders = {}. "
              + "Payload = {}",
          session,
          command,
          headers,
          new String(payload, UTF_8));
      log.error("STOMP caught exception with message = {}", exception.getMessage());
    }

    @Override
    public void afterConnected(final StompSession session, final StompHeaders connectedHeaders) {
      log.debug(
          "STOMP StompSession = {} connected with StompHeaders = {}", session, connectedHeaders);
    }
  }
}
