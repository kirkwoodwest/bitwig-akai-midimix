package com.kirkwoodwest.closedwoods.trackmaster.snapshots;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.Track;

public class SnapShotProjectRemotes {

  public SnapShotProjectRemotes(ControllerHost host, int numRemotePages, String remoteTagExpression){
    Track rootTrackGroup = host.getProject().getRootTrackGroup();
    for (int i = 0; i < numRemotePages; i++) {
      createRemotePage(rootTrackGroup, i, remoteTagExpression);
    }
  }

  private void createRemotePage(Track rootTrackGroup, int index, String remoteTagExpression) {
    CursorRemoteControlsPage cursorRemoteControlsPage = rootTrackGroup.createCursorRemoteControlsPage("SnapShotRemotes" + index, 8, remoteTagExpression);
    //complete me

  }
}
