package se.ifmo.blps.lab3.aop.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.transaction.TransactionDefinition.TIMEOUT_DEFAULT;
import static org.springframework.transaction.annotation.Isolation.DEFAULT;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Inherited
public @interface ManualTransaction {

  String name() default "";

  Propagation propagation() default REQUIRED;

  Isolation isolation() default DEFAULT;

  int timeout() default TIMEOUT_DEFAULT;

  boolean readOnly() default false;

  Class<? extends Throwable>[] rollbackFor() default {};

  Class<? extends Throwable>[] noRollbackFor() default {};
}
