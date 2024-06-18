package com.kirkwoodwest.openwoods.hardware;
import com.bitwig.extension.controller.api.HardwareActionBindable;

import java.util.*;

public class HardwareActionCollection {
  private final HashMap<Integer, List<HardwareActionBindable>> hardwareActions = new HashMap<>();

  public void add(int index, HardwareActionBindable action) {
    hardwareActions.computeIfAbsent(index, k -> new ArrayList<>()).add(action);
  }

  public List<HardwareActionBindable> get(int index) {
    return hardwareActions.getOrDefault(index, new ArrayList<>());
  }

  public Map<Integer, List<HardwareActionBindable>> getAll() {
    return hardwareActions;
  }
}

