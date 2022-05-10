package se.ifmo.blps.lab3.services;

import static java.lang.String.format;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQBytesMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.blps.lab3.domains.CreditSubscription;
import se.ifmo.blps.lab3.dtos.CreditNotificationDto;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.repositories.CreditSubscriptionRepository;
import se.ifmo.blps.lab3.repositories.UserRepository;

@Service("creditSubscriptionService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CreditSubscriptionService {

  private static final String QUEUE_DESTINATION = "credits";

  private final CreditSubscriptionRepository creditSubscriptionRepository;
  private final UserRepository userRepository;
  private final JavaMailSender mailSender;

  @Transactional
  public void createSubscription(final Long creditManagerId)
      throws ResourceNotFoundException, ResourceAlreadyExistsException {
    final var creditManager =
        userRepository
            .findById(creditManagerId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        format("Credit manager with id %d was not found", creditManagerId)));
    final var optionalSubscription =
        creditSubscriptionRepository.findBySubscriberId(creditManagerId);
    if (optionalSubscription.isPresent()) {
      throw new ResourceAlreadyExistsException(
          format("User with id %d already subscribed to notifications", creditManagerId));
    }
    final var subscription = new CreditSubscription();
    subscription.setSubscriber(creditManager);
    creditSubscriptionRepository.save(subscription);
  }

  @Transactional
  public void removeSubscription(final Long creditManagerId) throws ResourceNotFoundException {
    userRepository
        .findById(creditManagerId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    format("User with id %d was not found", creditManagerId)));
    final var subscription =
        creditSubscriptionRepository
            .findBySubscriberId(creditManagerId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        format(
                            "Subscription with subscriber id %d was not found", creditManagerId)));
    creditSubscriptionRepository.delete(subscription);
  }

  @JmsListener(destination = QUEUE_DESTINATION)
  public void sendNotifications(
      final ActiveMQBytesMessage message, final @Payload CreditNotificationDto notificationDto) {
    log.debug("Received message = {} with payload = {}", message, notificationDto);

    final var mailMessage = new MimeMailMessage(mailSender.createMimeMessage());
    mailMessage.setFrom("Lab3");
    mailMessage.setSubject("Credits notification");
    mailMessage.setTo(notificationDto.getEmail());

    final var stringBuilder = new StringBuilder("Current not reviewed credits:\n");
    if (notificationDto.getCredits().isEmpty()) {
      stringBuilder.append("There are no any unreviewed credits so far");
    } else {
      notificationDto
          .getCredits()
          .forEach(
              credit ->
                  stringBuilder
                      .append("Id: ")
                      .append(credit.getId())
                      .append(", applicant email: ")
                      .append(credit.getApplicantEmail())
                      .append(", price: ")
                      .append(credit.getPrice())
                      .append(", status: ")
                      .append(credit.getStatus())
                      .append("\n"));
    }

    mailMessage.setText(stringBuilder.toString());
    mailSender.send(mailMessage.getMimeMessage());
  }
}
