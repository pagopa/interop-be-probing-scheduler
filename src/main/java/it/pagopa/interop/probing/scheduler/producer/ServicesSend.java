package it.pagopa.interop.probing.scheduler.producer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.scheduler.dto.EserviceContent;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ServicesSend {

  @Value("${amazon.sqs.end-point.poll-queue}")
  private String sqsUrl;

  @Autowired
  private AmazonSQS amazonSQS;

  @Autowired
  private ObjectMapper objectMapper;


  public void sendMessage(EserviceContent service) throws IOException {
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl)
        .withMessageBody(objectMapper.writeValueAsString(service));
    amazonSQS.sendMessage(sendMessageRequest);
    log.info("Service with record id {} has been published in SQS.", service.getEserviceRecordId());
  }
}
