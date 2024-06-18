package com.kirkwoodwest.extensions.datafeed.parameter;

public class ParameterDataType {
  ParameterType type = ParameterType.VALUE;
  Class<?> dataType = int.class;
  private int[] intRange = new int[2];
  private float[] floatRange = new float[2];
  private boolean validData = false;

  public ParameterDataType(String input) {
    set(input);
  }

  public Class<?> getDataType() {
    return dataType;
  }

  public int getIntMin(){
    return intRange[0];
  }
  public int getIntMax(){
    return intRange[1];
  }
  public float getFloatMin(){
    return floatRange[0];
  }
  public float getFloatMax(){
    return floatRange[1];
  }

  public void set(String input) {
    //TODO: Do validation
    if (input.matches("-?\\d+:\\d+")) {
      dataType = int.class;
      intRange = convertToIntArray(input);
    } else if (input.matches("-?\\d+\\.\\d+:-?\\d+\\.\\d+")) {
      dataType = float.class;
      floatRange = convertToFloatArray(input);
    } else if (input.equalsIgnoreCase("bool")) {
      // or throw an exception
      dataType = boolean.class;
    }
    validData = true;
  }

  private static int[] convertToIntArray(String input) {
    String[] parts = input.split(":");
    int[] numbers = new int[2];
    numbers[0] = Integer.parseInt(parts[0]);
    numbers[1] = Integer.parseInt(parts[1]);
    return numbers;
  }

  private static float[] convertToFloatArray(String input) {
    String[] parts = input.split(":");
    float[] numbers = new float[2];
    numbers[0] = Float.parseFloat(parts[0]);
    numbers[1] = Float.parseFloat(parts[1]);
    return numbers;
  }

}


