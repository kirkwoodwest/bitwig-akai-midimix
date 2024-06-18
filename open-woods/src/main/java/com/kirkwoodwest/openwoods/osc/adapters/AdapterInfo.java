package com.kirkwoodwest.openwoods.osc.adapters;

import java.util.ArrayList;
import java.util.List;

public class AdapterInfo {
  List<AdapterInfoData> data = new ArrayList<>();

  public AdapterInfo() {
  }

  public void add(String path, String description, String typeTag) {
    data.add(new AdapterInfoData(path, description, typeTag));
  }

  public List<AdapterInfoData> get() {
    return data;
  }
}
