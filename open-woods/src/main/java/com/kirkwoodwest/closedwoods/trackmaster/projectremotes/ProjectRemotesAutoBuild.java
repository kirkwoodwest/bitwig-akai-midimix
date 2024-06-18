package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.HardwareControlType;
import com.bitwig.extension.controller.api.Track;

//Using this class it will auto build project remotes.
public class ProjectRemotesAutoBuild {
  private final ProjectRemotes projectRemotes;
  private final int numProjectRemotePages;

  public ProjectRemotesAutoBuild(ControllerHost host, ProjectRemotes projectRemotes, int numProjectRemotePages) {
    this.projectRemotes = projectRemotes;
    this.numProjectRemotePages = numProjectRemotePages;
    CursorRemoteControlsPage cursorRemotePage = projectRemotes.getCursorRemotePage(0);
    cursorRemotePage.pageCount().addValueObserver((count)->{
      if(count < numProjectRemotePages) {
        host.scheduleTask(this::maybeCreatePage, 1000);
      }
    });
  }
  private void maybeCreatePage() {
    if(projectRemotes.getCursorRemotePage(0).pageCount().get() < numProjectRemotePages) {
      projectRemotes.getCursorRemotePage(0).createPresetPage();
    }
  }
}
