package com.kirkwoodwest.openwoods.remotecontrol;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.Device;
import com.bitwig.extension.controller.api.Track;

//Static Class used to build remote controls for a track, device, or global
public class OwRemoteBuilder {
  //Build Device Remotes
  //pass in a device and a list of parameters to build remotes for
  public static OwRemoteControlsPage buildDeviceRemotes(Device device, String name, int numControls, String filterExpression, int pageIndex, boolean isLocked ) {
    CursorRemoteControlsPage cursorRemoteControlsPage = device.createCursorRemoteControlsPage(name, numControls, filterExpression);
    return new OwRemoteControlsPage(cursorRemoteControlsPage, pageIndex, isLocked);
  }

  //Build Global Remotes
  public static OwRemoteControlsPage buildGlobalRemotes(String name, ControllerHost host, int numControls, String filterExpression, int pageIndex, boolean isLocked) {
    Track rootTrackGroup = host.getProject().getRootTrackGroup();
    CursorRemoteControlsPage cursorRemoteControlsPage = rootTrackGroup.createCursorRemoteControlsPage(name, numControls, filterExpression);
    return new OwRemoteControlsPage(cursorRemoteControlsPage, pageIndex, isLocked);
  }

  //Build Track Remotes
  public static OwRemoteControlsPage buildTrackRemotes(Track track, String name, int numControls, String filterExpression, int pageIndex, boolean isLocked) {
    CursorRemoteControlsPage cursorRemoteControlsPage = track.createCursorRemoteControlsPage(name, numControls, filterExpression);
    return new OwRemoteControlsPage(cursorRemoteControlsPage, pageIndex, isLocked);
  }
}
