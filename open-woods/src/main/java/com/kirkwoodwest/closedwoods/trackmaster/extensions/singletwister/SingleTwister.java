package com.kirkwoodwest.closedwoods.trackmaster.extensions.singletwister;

import com.kirkwoodwest.closedwoods.trackmaster.extensions.TrackMasterDefinition;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;

import java.util.ArrayList;
import java.util.List;

public class SingleTwister implements TrackMasterDefinition {

  @Override
  public List<Class<?>> getControllers() {
    List<Class<?>> controllers = new ArrayList<>();
    controllers.add(HardwareMFT.class);
    return controllers;
  }

}
