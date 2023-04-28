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
import org.springframework.test.util.ReflectionTestUtils;
import it.pagopa.interop.probing.scheduler.dto.EserviceContent;
import it.pagopa.interop.probing.scheduler.dto.PollingEserviceResponse;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.scheduler.ScheduledTasks;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import it.pagopa.interop.probing.scheduler.util.EserviceTechnology;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ScheduledTasksTest {

  @InjectMocks
  @Autowired
  ScheduledTasks scheduledTasks;

  @Mock
  ServicesSend servicesSend;

  @Mock
  EserviceService eserviceService;


  private PollingEserviceResponse response;

  @BeforeEach
  void setup() {
    String[] basePath = {"basePath1", "basePath2"};
    ReflectionTestUtils.setField(scheduledTasks, "limit", 10);
    EserviceContent eServiceDTO = EserviceContent.builder().basePath(basePath).eserviceRecordId(1L)
        .technology(EserviceTechnology.REST).build();
    response = PollingEserviceResponse.builder().totalElements(12)
        .content(Arrays.asList(eServiceDTO)).build();


  }

  @Test
  @DisplayName("The scheduleFixedDelayTask method of ScheduledTasks class is tested.")
  void testScheduledTask_thenDoesNotThrowException() throws IOException {
    Mockito.when(eserviceService.getEservicesReadyForPolling(Mockito.any(), Mockito.any()))
        .thenReturn(response);
    Mockito.doNothing().when(servicesSend).sendMessage(response.getContent().get(0));
    scheduledTasks.scheduleFixedDelayTask();
    assertDoesNotThrow(() -> scheduledTasks.scheduleFixedDelayTask());
  }


}
