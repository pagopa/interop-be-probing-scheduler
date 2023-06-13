package it.pagopa.interop.probing.scheduler.unit.scheduler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.io.IOException;
import java.util.Arrays;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import it.pagopa.interop.probing.scheduler.client.EserviceClient;
import it.pagopa.interop.probing.scheduler.dto.impl.EserviceContent;
import it.pagopa.interop.probing.scheduler.dto.impl.PollingEserviceResponse;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.scheduler.ScheduledTasks;
import it.pagopa.interop.probing.scheduler.service.impl.EserviceServiceImpl;
import it.pagopa.interop.probing.scheduler.util.EserviceTechnology;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ScheduledTasksTest {

  @InjectMocks
  @Autowired
  ScheduledTasks scheduledTasks;

  @Mock
  ServicesSend servicesSend;

  @InjectMocks
  @Autowired
  EserviceServiceImpl eserviceServiceImpl;

  @Mock
  EserviceClient eserviceClient;

  private ResponseEntity<PollingEserviceResponse> response;

  @BeforeEach
  void setup() {
    String[] basePath = {"basePath1", "basePath2"};
    String[] audience = {"audience"};
    ReflectionTestUtils.setField(scheduledTasks, "limit", 10);
    EserviceContent eServiceDTO = EserviceContent.builder().basePath(basePath).eserviceRecordId(1L)
        .technology(EserviceTechnology.REST).audience(audience).build();
    response = ResponseEntity.ok(PollingEserviceResponse.builder().totalElements(1)
        .content(Arrays.asList(eServiceDTO)).build());


  }

  @Test
  @DisplayName("The scheduleFixedDelayTask method of ScheduledTasks class is tested.")
  void testScheduledTask_thenDoesNotThrowException() throws IOException {
    Mockito.when(eserviceClient.getEservicesReadyForPolling(Mockito.any(), Mockito.any()))
        .thenReturn(response);
    Mockito.when(eserviceClient.updateLastRequest(Mockito.any(), Mockito.any()))
        .thenReturn(ResponseEntity.noContent().build());
    Mockito.doNothing().when(servicesSend).sendMessage(response.getBody().getContent().get(0));
    assertDoesNotThrow(() -> scheduledTasks.scheduleFixedDelayTask());
  }


}
