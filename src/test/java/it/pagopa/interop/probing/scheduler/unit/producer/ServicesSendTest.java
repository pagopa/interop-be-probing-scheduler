package it.pagopa.interop.probing.scheduler.unit.producer;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.xray.AWSXRay;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.scheduler.dto.impl.EserviceContent;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.util.EserviceTechnology;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ServicesSendTest {

  @InjectMocks
  @Autowired
  ServicesSend servicesSend;

  @Mock
  private AmazonSQSAsync amazonSQS;

  private EserviceContent eServiceDTO;

  private static final String TEST_URL = "http://queue/test-queue";

  @Mock
  ObjectMapper mapper;


  @BeforeEach
  void setup() {
    AWSXRay.beginSegment("Segment-test");
    String[] basePath = {"basePath1", "basePath2"};
    String[] audience = {"audience"};
    ReflectionTestUtils.setField(servicesSend, "sqsUrl", TEST_URL);
    eServiceDTO = EserviceContent.builder().basePath(basePath).eserviceRecordId(1L)
        .technology(EserviceTechnology.REST).audience(audience).build();

  }

  @AfterEach
  void clean() {
    AWSXRay.endSegment();
  }

  @Test
  @DisplayName("The sendMessage method of ServicesSend class is tested.")
  void testSendMessage_whenGivenValidEServiceAndUrl_thenSchedulerWriteOnQueue() throws IOException {
    Mockito.when(amazonSQS.sendMessage(Mockito.any())).thenReturn(null);
    servicesSend.sendMessage(eServiceDTO);
    SendMessageRequest expectedSendMessageRequest = new SendMessageRequest().withQueueUrl(TEST_URL)
        .withMessageBody(mapper.writeValueAsString(eServiceDTO));

    Mockito.verify(amazonSQS).sendMessage(expectedSendMessageRequest);
  }


}
