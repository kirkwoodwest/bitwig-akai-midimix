package com.kirkwoodwest.extensions.datafeed;

import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableEnumValue;
import com.bitwig.extension.controller.api.SettableStringValue;

public class SettingRemotePage {

  private final SettableStringValue path;
  private final SettableEnumValue type;
  private final SettableStringValue range;

  public SettingRemotePage(Preferences preferences, int pageIndex, int parameterIndex){
    path = preferences.getStringSetting("Path", "Remote " + pageIndex + "/" + parameterIndex, 32, "/datafeed/" + pageIndex + "/" + parameterIndex);
    path.markInterested();

    type = preferences.getEnumSetting("Type", "Remote " + pageIndex + "/" + parameterIndex, new String[]{"Value","Modulated Value", "Displayed Value"}, "Value");
    type.markInterested();

    range = preferences.getStringSetting("Range", "Remote " + pageIndex + "/" + parameterIndex, 32, "0.0:1.0");
    range.markInterested();
  }

  public String getPath(){
    return path.get();
  }

  public String getType(){
    return type.get();
  }

  public String getRange(){
    return range.get();
  }

  public Class<?> getType(String input){
    if (input.matches("-?\\d+:\\d+")) {
      return int.class;
    } else if (input.matches("-?\\d+\\.\\d+:-?\\d+\\.\\d+")) {
      return float.class;
    } else if (input.equalsIgnoreCase("bool")) {
      return boolean.class;
    } else {
      return null;
    }
  }
}
