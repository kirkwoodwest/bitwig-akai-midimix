package com.kirkwoodwest.closedwoods.trackmaster.extensions;

import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;

import java.util.ArrayList;
import java.util.List;

public class QuadBox implements TrackMasterDefinition {

  @Override
  public List<Class<?>> getControllers() {
    List<Class<?>> controllers = new ArrayList<>();
    controllers.add(HardwareMFT.class);
    controllers.add(HardwareMFT.class);
    controllers.add(HardwareMF3D.class);
    controllers.add(HardwareMF3D.class);
    return controllers;
  }

}
