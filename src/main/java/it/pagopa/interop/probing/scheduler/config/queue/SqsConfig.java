package it.pagopa.interop.probing.scheduler.config.queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;
import com.amazonaws.xray.strategy.IgnoreErrorContextMissingStrategy;

@Configuration
public class SqsConfig {


  @Bean
  public AmazonSQSAsync amazonSQSAsync() {
    AWSXRay.getGlobalRecorder().setContextMissingStrategy(new IgnoreErrorContextMissingStrategy());
    return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder())).build();
  }

}
