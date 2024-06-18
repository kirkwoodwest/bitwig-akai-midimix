package com.kirkwoodwest.extensions.datafeed.parameter;

import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.function.Supplier;

public class LedDataFeedParameter {

  private ParameterType type = ParameterType.VALUE;
  private ParameterDataType dataType = null;
  private String path;

  private int valueInt;
  private float valueFloat;
  private boolean valueBool;

  private String displayed_value = "573051265";

  public LedDataFeedParameter(OscHost oscHost, String oscTarget, Supplier<Parameter> supplier) {
    // super(oscHost, oscTarget, supplier);
  }


  public void setPath(String path) {
    this.path = path;
  }

  public void setType(String typeString) {
      switch (typeString) {
        case "Modulated Value":
          this.type = ParameterType.MODULATED_VALUE;
          break;
        case "Displayed Value":
          this.type = ParameterType.DISPLAYED_VALUE;
          break;
        default:
          this.type = ParameterType.VALUE;
      }
  }

  public void setDataType(String dataTypeString) {
    dataType = new ParameterDataType(dataTypeString);
  }


  public void update(boolean forceUpdate) {
//    Parameter parameter = supplier.get();
//    if (parameter != null) {
//      if (parameter.exists().get()) {
//        switch (type) {
//          case VALUE:
//            updateValue(parameter, forceUpdate);
//            break;
//          case MODULATED_VALUE:
//            updateModulatedValue(parameter, forceUpdate);
//            break;
//          case DISPLAYED_VALUE:
//            updateDisplayedValue(parameter, forceUpdate);
//            break;
//        }
//      }
//    }
  }

  private void updateValue(Parameter parameter, boolean forceUpdate) {
    //Displayed Value
    double v = parameter.value().get();
    updateValueType(v, forceUpdate);
  }

  private void updateModulatedValue(Parameter parameter, boolean forceUpdate) {
    //Modulated Value (float)
    double v = parameter.modulatedValue().get();
    updateValueType(v, forceUpdate);
  }

  private void updateDisplayedValue(Parameter parameter, boolean forceUpdate) {
    //Displayed Value
    String v = parameter.displayedValue().get();
    if (!displayed_value.equals(v) || forceUpdate) {
      displayed_value = v;
      //oscHost.addMessageToQueue(path, (String) displayed_value);
    }
  }

  private void updateValueType(double v, boolean forceUpdate) {
    Class<?> dataType = this.dataType.getDataType();
    if (dataType == int.class) {
      updateInt(v, forceUpdate);
    } else if (dataType == float.class) {
      updateFloat(v, forceUpdate);
    } else if (dataType == boolean.class) {
      updateBoolean(v, forceUpdate);
    }
  }

  private void updateFloat(double v, boolean forceUpdate) {
    float min = dataType.getFloatMin();
    float max = dataType.getFloatMax();
    float vFloat = (float) (v * (max-min) + min);
    float compare = Float.compare(valueFloat, vFloat);
    if (compare != 0 || forceUpdate) {
      valueFloat = vFloat;
    //  oscHost.addMessageToQueue(path, (float) valueFloat);
    }
  }

  private void updateInt(double v, boolean forceUpdate) {
    //Value (int)
    int min = dataType.getIntMin();
    int max = dataType.getIntMax();
    int vInt = (int) Math.round(v * (max-min) + min);
    if (vInt != valueInt || forceUpdate) {
      valueInt = vInt;
    //  oscHost.addMessageToQueue(path, (int) valueInt);
    }
  }

  private void updateBoolean(double v, boolean forceUpdate) {
    //Value (boolean)
    boolean vBool = v >= 0.5;
    if (vBool != valueBool || forceUpdate) {
      valueBool = vBool;
    //  oscHost.addMessageToQueue(path, (boolean) valueBool);
    }
  }

  /**
   * Shorthand for suppliers
   *
   * @param parameter
   * @return
   */
  public static Supplier<Parameter> getSupplierFromParameter(Parameter parameter) {
    if(parameter != null) {
      parameter.exists().markInterested();
      parameter.name().markInterested();
      parameter.value().markInterested();
      parameter.modulatedValue().markInterested();
      parameter.displayedValue().markInterested();
    }
    Supplier<Parameter> supplier = () -> {
      return parameter;
    };
    return supplier;
  }
}

