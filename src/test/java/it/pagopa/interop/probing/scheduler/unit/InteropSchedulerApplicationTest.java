package it.pagopa.interop.probing.scheduler.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import it.pagopa.interop.probing.scheduler.InteropSchedulerApplication;

@SpringBootTest
public class InteropSchedulerApplicationTest {
  @Test
  public void main() {
    InteropSchedulerApplication.main(new String[] {});
  }
}
