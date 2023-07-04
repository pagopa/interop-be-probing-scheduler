package it.pagopa.interop.probing.scheduler.producer;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.scheduler.dto.impl.EserviceContent;
import it.pagopa.interop.probing.scheduler.util.logging.Logger;


@Service
@XRayEnabled
public class ServicesSend {

  @Value("${amazon.sqs.endpoint.poll-queue}")
  private String sqsUrl;

  @Autowired
  private AmazonSQSAsync amazonSQS;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Logger logger;


  public void sendMessage(EserviceContent service) throws IOException {
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl)
        .withMessageBody(objectMapper.writeValueAsString(service));
    amazonSQS.sendMessage(sendMessageRequest);
    logger.logQueueSendSuccess(service.getEserviceRecordId());
  }
}
