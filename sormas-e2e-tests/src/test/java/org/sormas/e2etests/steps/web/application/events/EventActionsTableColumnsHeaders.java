package org.sormas.e2etests.steps.web.application.events;

public enum EventActionsTableColumnsHeaders {
  EVENT_ID("EVENT ID"),
  EVENT_TITLE("EVENT TITLE"),
  DATE_OF_EVENT("DATE_OF_EVENT"),
  EVOLUTION_DATE_OF_EVENT("EVOLUTION DATE OF EVENT"),
  EVENT_STATUS("EVENT STATUS"),
  EVENT_RISK_LEVEL("EVENT RISK LEVEL"),
  EVENT_INVESTIGATION_STATUS("EVENT_INVESTIGATION_STATUS"),
  EVENT_MANAGEMENT_STATUS("EVENT MANAGEMENT STATUS"),
  EVENT_REPORTING_USER("EVENT REPORTING USER"),
  EVENT_RESPONSIBLE_USER("EVENT RESPONSIBLE USER"),
  ACTION_TITLE("ACTION TITLE"),
  ACTION_CREATION_DATE("ACTION CREATION DATE"),
  ACTION_CHANGE_DATE("ACTION CHANGE DATE"),
  ACTION_DATE("ACTION DATE"),
  ACTION_STATUS("ACTION STATUS"),
  ACTION_PRIORITY("ACTION PRIORITY"),
  ACTION_LAST_MODIFIED_BY("ACTION LAST MODIFIED BY");

  private final String columnHeader;

  EventActionsTableColumnsHeaders(String columnHeader) {
    this.columnHeader = columnHeader;
  }

  @Override
  public String toString() {
    return this.columnHeader;
  }
}
