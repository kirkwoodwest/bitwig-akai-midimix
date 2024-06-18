package com.kirkwoodwest.closedwoods.trackmaster.extensions.twisterandmf3d;

import com.kirkwoodwest.closedwoods.trackmaster.extensions.TrackMasterDefinition;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;

import java.util.ArrayList;
import java.util.List;

public class TwisterMF3D implements TrackMasterDefinition {

  @Override
  public List<Class<?>> getControllers() {
    List<Class<?>> controllers = new ArrayList<>();
    controllers.add(HardwareMFT.class);
    controllers.add(HardwareMF3D.class);
    return controllers;
  }

}
