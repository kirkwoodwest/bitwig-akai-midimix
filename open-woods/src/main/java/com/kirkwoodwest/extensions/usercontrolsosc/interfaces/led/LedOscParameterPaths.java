package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led;

public class LedOscParameterPaths {
    private static final String TARGET_NAME = "/name";
    private static final String TARGET_EXISTS = "/exists";
    private static final String TARGET_VALUE = "/value";
    private static final String TARGET_MODULATED_VALUE = "/modulated_value";
    private static final String TARGET_TOUCH = "/touch";
    private static final String TARGET_DISPLAYED_VALUE = "/display_value";
    public String name;
    public String exists;
    public String value;
    public String modulatedValue;
    public String touch;
    public String displayedValue;

    public LedOscParameterPaths(String baseTarget) {
      name = baseTarget + TARGET_NAME;
      exists = baseTarget + TARGET_EXISTS;
      value = baseTarget + TARGET_VALUE;
      modulatedValue = baseTarget + TARGET_MODULATED_VALUE;
      touch = baseTarget + TARGET_TOUCH;
      displayedValue = baseTarget + TARGET_DISPLAYED_VALUE;
    }
}