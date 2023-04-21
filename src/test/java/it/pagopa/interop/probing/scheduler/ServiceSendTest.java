package it.pagopa.interop.probing.scheduler;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.scheduler.dto.EserviceContent;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.util.EserviceTechnology;

@SpringBootTest
class ServiceSendTest {

  @InjectMocks
  ServicesSend servicesSend;

  @Mock
  private AmazonSQSAsync amazonSQS;

  private EserviceContent eServiceDTO;

  @Mock
  ObjectMapper mapper;

  @BeforeAll
  void setup() {
    String[] basePath = {"basePath1", "basePath2"};

    eServiceDTO = EserviceContent.builder().basePath(basePath).id("1")
        .technology(EserviceTechnology.REST).build();

  }

  @Test
  @DisplayName("The sendMessage method of ServicesSend class is tested.")
  void testSendMessage_whenGivenValidEServiceAndUrl_thenSchedulerWriteOnQueue() throws IOException {
    String url = "http://queue/test-queue";
    Mockito.when(amazonSQS.sendMessage(Mockito.any())).thenReturn(null);
    servicesSend.sendMessage(eServiceDTO);
    SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(url)
        .withMessageBody(mapper.writeValueAsString(eServiceDTO));
    Mockito.verify(amazonSQS).sendMessage(sendMessageRequest);
  }


}
