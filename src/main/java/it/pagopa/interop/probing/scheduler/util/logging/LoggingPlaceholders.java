package it.pagopa.interop.probing.scheduler.util.logging;

public class LoggingPlaceholders {
  private LoggingPlaceholders() {}

  public static final String TRACE_ID_XRAY_PLACEHOLDER = "AWS-XRAY-TRACE-ID";
  public static final String TRACE_ID_XRAY_MDC_PREFIX = "- [TRACE_ID= ";
  public static final String SUBSEGMENT_ID_XRAY_PLACEHOLDER = "AWS-XRAY-SUBSEG-ID";
  public static final String SUBSEGMENT_ID_XRAY_MDC_PREFIX = "- [SUB_SEGMENT_ID= ";
  public static final String SEARCH_SUBSEGMENT_PLACEHOLDER = "Search_Subsegment";
  public static final String UPDATE_LAST_REQ_SUBSEGMENT_PLACEHOLDER =
      "Update_last_request_Subsegment_eserviceId_";
}
