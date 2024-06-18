package com.kirkwoodwest.openwoods.osc.adapters;

public class RemoteControlPaths {
  private static final String TARGET_NAME = "/name";
  private static final String TARGET_EXISTS = "/exists";
  private static final String TARGET_VALUE = "/value";
  private static final String TARGET_MODULATED_VALUE = "/modulated_value";
  private static final String TARGET_TOUCH = "/touch";
  private static final String TARGET_DISPLAYED_VALUE = "/display_value";
  private static final String TARGET_IS_BEING_MAPPED = "/is_being_mapped";
  private static final String TARGET_CLEAR_MAPPING = "/clear_mapping";
  public String name;
  public String exists;
  public String value;
  public String modulatedValue;
  public String touch;
  public String displayedValue;
  public String isBeingMapped;
  public String clearMapping;

  public RemoteControlPaths(String baseTarget) {
    name = baseTarget + TARGET_NAME;
    exists = baseTarget + TARGET_EXISTS;
    value = baseTarget + TARGET_VALUE;
    modulatedValue = baseTarget + TARGET_MODULATED_VALUE;
    touch = baseTarget + TARGET_TOUCH;
    displayedValue = baseTarget + TARGET_DISPLAYED_VALUE;
    isBeingMapped = baseTarget + TARGET_IS_BEING_MAPPED;
    clearMapping = baseTarget + TARGET_CLEAR_MAPPING;
  }
}
