package se.ifmo.blps.lab3.jobs;

import static org.quartz.Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY;

import java.util.Map;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class SendCreditNotificationRequestJobConfig {

  @Bean
  public JobDetailFactoryBean sendCreditNotificationRequestJobDetail() {
    final var sendCreditNotificationRequestJobDetail = new JobDetailFactoryBean();
    sendCreditNotificationRequestJobDetail.setJobClass(SendCreditNotificationRequestJob.class);
    sendCreditNotificationRequestJobDetail.setDurability(true);
    sendCreditNotificationRequestJobDetail.setJobDataAsMap(Map.of("destination", "credits"));
    return sendCreditNotificationRequestJobDetail;
  }

  @Bean
  public CronTriggerFactoryBean sendCreditNotificationRequestTrigger(
      final JobDetail sendCreditNotificationRequestJobDetail) {
    final var sendCreditNotificationRequestTrigger = new CronTriggerFactoryBean();
    sendCreditNotificationRequestTrigger.setJobDetail(sendCreditNotificationRequestJobDetail);
    sendCreditNotificationRequestTrigger.setCronExpression("0 * * ? * * *");
    sendCreditNotificationRequestTrigger.setMisfireInstruction(
        MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
    return sendCreditNotificationRequestTrigger;
  }
}
