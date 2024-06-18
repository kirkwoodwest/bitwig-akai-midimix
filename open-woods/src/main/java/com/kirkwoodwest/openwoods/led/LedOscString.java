package com.kirkwoodwest.openwoods.led;

import com.bitwig.extension.controller.api.SettableStringValue;
import com.bitwig.extension.controller.api.StringValue;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.function.Supplier;

public class LedOscString extends Led<String> {
  public LedOscString(OscHost oscHost, String oscTarget, Supplier<String> supplier){
    super(oscHost, oscTarget, supplier);
  }

  @Override
  public boolean compare(String s){
    if(s.equals(super.localValue)){
      return true;
    }
    return false;
  }

  public static Supplier<String> createSupplier(SettableStringValue settableStringValue) {
    settableStringValue.markInterested();
    Supplier<String> supplier = ()->{  return settableStringValue.get();
    };
    return supplier;
  }

  public static Supplier<String> createSupplier(StringValue stringValue) {
    stringValue.markInterested();
    Supplier<String> supplier = ()->{  return stringValue.get();
    };
    return supplier;
  }
}
