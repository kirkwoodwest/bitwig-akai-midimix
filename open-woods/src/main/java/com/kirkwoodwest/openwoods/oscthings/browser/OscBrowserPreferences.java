package com.kirkwoodwest.openwoods.oscthings.browser;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.SettableRangedValue;

import java.util.HashMap;

public class OscBrowserPreferences {
  private final SettableRangedValue smartFilter;
  private final SettableRangedValue filterCategory;
  private final SettableRangedValue filterDeviceType;
  private final SettableRangedValue filterFileType;
  private final SettableRangedValue filterLocation;
  private final SettableRangedValue filterCreator;
  private final SettableRangedValue filterDevice;
  private final SettableRangedValue filterBankTag;
  private final SettableRangedValue resultsBank;

  private final int[] smartFilterMinMax= {1, 32};
  private final int[] filterCategoryMinMax= {1, 32};
  private final int[] filterDeviceTypeMinMax= {1, 32};
  private final int[] filterFileTypeMinMax= {1, 32};
  private final int[] filterLocationMinMax= {1, 32};
  private final int[] filterCreatorMinMax= {1, 32};
  private final int[] filterDeviceMinMax= {1, 32};
  private final int[] filterBankTagMinMax= {1, 32};
  private final int[] resultsBankMinMax= {1, 64};
  private final HashMap<SettableRangedValue, int[]> map = new HashMap<>();


  public OscBrowserPreferences(ControllerHost host) {
    smartFilter = host.getPreferences().getNumberSetting("Smart Filter # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterCategory  = host.getPreferences().getNumberSetting("Category # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterDeviceType = host.getPreferences().getNumberSetting("Device Type # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterFileType = host.getPreferences().getNumberSetting("File Type # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterLocation = host.getPreferences().getNumberSetting("Location # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterCreator = host.getPreferences().getNumberSetting("Creator # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterDevice = host.getPreferences().getNumberSetting("Device # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    filterBankTag = host.getPreferences().getNumberSetting("Bank Tag # Items", "Browser", smartFilterMinMax[0], smartFilterMinMax[1], 1, "", 16);
    resultsBank = host.getPreferences().getNumberSetting("Results # Items", "Browser", resultsBankMinMax[0], resultsBankMinMax[1], 1, "", 45);
    smartFilter.markInterested();
    filterCategory.markInterested();
    filterDeviceType.markInterested();
    filterFileType.markInterested();
    filterLocation.markInterested();
    filterCreator.markInterested();
    filterDevice.markInterested();
    filterBankTag.markInterested();
    resultsBank.markInterested();
    map.put(smartFilter, smartFilterMinMax);
    map.put(filterCategory, filterCategoryMinMax);
    map.put(filterDeviceType, filterDeviceTypeMinMax);
    map.put(filterFileType, filterFileTypeMinMax);
    map.put(filterLocation, filterLocationMinMax);
    map.put(filterCreator, filterCreatorMinMax);
    map.put(filterDevice, filterDeviceMinMax);
    map.put(filterBankTag, filterBankTagMinMax);
    map.put(resultsBank, resultsBankMinMax);
  }

  public int getNumItems(SettableRangedValue settableRangedValue) {
    int[] minMax = map.get(settableRangedValue);
    double v = minMax[0] + Math.floor((settableRangedValue.get() * (minMax[1] - minMax[0])));
    return (int) Math.floor(v);
  }
  public int getSmartFilterSize() {
    return getNumItems(smartFilter);
  }
  public int getCategorySize() {
    return getNumItems(filterCategory);
  }
  public int getDeviceTypeSize() {
    return getNumItems(filterDeviceType);
  }
  public int getFileTypeSize() {
    return getNumItems(filterFileType);
  }
  public int getLocationSize() {
    return getNumItems(filterLocation);
  }
  public int getCreatorSize() {
    return getNumItems(filterCreator);
  }
  public int getDeviceSize() {
    return getNumItems(filterDevice);
  }
  public int getBankTagSize() {
    return getNumItems(filterBankTag);
  }
  public int getResultsBankSize() {
    return getNumItems(resultsBank);
  }
}
