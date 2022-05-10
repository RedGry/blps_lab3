package se.ifmo.blps.lab3.aop.aspects;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import se.ifmo.blps.lab3.aop.annotations.ManualTransaction;
import se.ifmo.blps.lab3.exceptions.ManualTransactionException;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ManualTransactionAspect {

  private final PlatformTransactionManager platformTransactionManager;

  @Around("execution(public * *(..)) && " + "@annotation(manualTransaction)")
  public Object wrapWithCustomTransaction(
      final ProceedingJoinPoint joinPoint, final ManualTransaction manualTransaction) {
    final var transactionTemplate = new TransactionTemplate(platformTransactionManager);
    transactionTemplate.setName(manualTransaction.name());
    transactionTemplate.setPropagationBehavior(manualTransaction.propagation().value());
    transactionTemplate.setIsolationLevel(manualTransaction.isolation().value());
    transactionTemplate.setTimeout(manualTransaction.isolation().value());
    transactionTemplate.setReadOnly(manualTransaction.readOnly());

    final var wasInterrupted = new AtomicBoolean(false);
    final Object result =
        transactionTemplate.execute(
            action -> {
              try {
                final var proceed = joinPoint.proceed();
                action.flush();
                return proceed;
              } catch (final RuntimeException e) {
                wasInterrupted.set(true);
                log.error(
                    "Caught RuntimeException during manual transaction with message = {}",
                    e.getMessage());
                throw e;
              } catch (final Throwable e) {
                wasInterrupted.set(true);
                if (Arrays.stream(manualTransaction.rollbackFor())
                    .anyMatch(rollbackException -> rollbackException.equals(e.getClass()))) {
                  log.error("Caught RollbackException with message = {}", e.getMessage());
                  action.setRollbackOnly();
                  return null;
                }
              }
              return null;
            });
    if (wasInterrupted.get()) {
      throw new ManualTransactionException("Exception occurred during manual transaction!");
    }
    return result;
  }
}
