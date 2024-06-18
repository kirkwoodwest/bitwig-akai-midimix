// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.closedwoods.trackmaster.extensions.singletwister;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.closedwoods.trackmaster.TrackMaster;


public class SingleTwisterExtension extends ControllerExtension {
  private TrackMaster core;

  protected SingleTwisterExtension(ControllerExtensionDefinition definition, ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    core = new TrackMaster(this, new SingleTwister());

  }

  @Override
  public void exit() {
    core.exit();
  }

  @Override
  public void flush() {
    core.flush();
  }
}
