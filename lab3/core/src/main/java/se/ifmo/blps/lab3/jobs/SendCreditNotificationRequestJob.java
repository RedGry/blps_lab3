package se.ifmo.blps.lab3.jobs;

import static se.ifmo.blps.lab3.domains.CreditStatus.CREATED;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import se.ifmo.blps.lab3.dtos.CreditMetadataDto;
import se.ifmo.blps.lab3.dtos.CreditNotificationDto;
import se.ifmo.blps.lab3.repositories.CreditRepository;
import se.ifmo.blps.lab3.repositories.CreditSubscriptionRepository;
import se.ifmo.blps.lab3.utils.ArtemisWebSocketStompClient;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SendCreditNotificationRequestJob extends QuartzJobBean {

  private final CreditRepository creditRepository;
  private final CreditSubscriptionRepository creditSubscriptionRepository;
  private final ArtemisWebSocketStompClient stompClient;
  private final TransactionTemplate transactionTemplate;

  @Override
  @SneakyThrows
  protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
    log.info("Started execution of SendCreditNotificationRequestJob with context = {}", context);

    final var result =
        transactionTemplate.execute(
            action -> {
              final var destination = (String) context.getMergedJobDataMap().get("destination");
              try {
                final var subscriptions = creditSubscriptionRepository.findAll();
                final var credits =
                    creditRepository.findAllByStatus(CREATED).stream()
                        .map(
                            credit ->
                                CreditMetadataDto.builder()
                                    .id(credit.getId())
                                    .price(credit.getPrice())
                                    .applicantEmail(credit.getApplicant().getEmail())
                                    .status(credit.getStatus())
                                    .build())
                        .collect(Collectors.toList());
                for (final var subscription : subscriptions) {
                  stompClient.sendMessage(
                      destination,
                      new CreditNotificationDto(subscription.getSubscriber().getEmail(), credits));
                }
              } catch (InterruptedException e) {
                log.error(
                    "Caught InterruptedException during SendCreditNotificationRequestJob with message = {}",
                    e.getMessage());
                action.setRollbackOnly();
                return e;
              } catch (URISyntaxException | ExecutionException e) {
                log.error(
                    "Caught exception during SendCreditNotificationRequestJob with message = {}",
                    e.getMessage());
                action.setRollbackOnly();
                return e;
              }
              return null;
            });
    if (result instanceof InterruptedException) {
      throw result;
    }
    if (result != null) {
      throw new JobExecutionException(result);
    }

    log.info("Finished execution of SendCreditNotificationRequestJob");
  }
}
