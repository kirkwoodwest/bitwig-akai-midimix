package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.RemoteControl;
import com.bitwig.extension.controller.api.Track;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControlsPage;

import java.util.ArrayList;

/**
 * Container for project remotes and parameters. Allows for quick creation of project remote pages and the parameters with a specific tag in mind.
 */
public class ProjectRemotes {
  private final ArrayList<CursorRemoteControlsPage> pages = new ArrayList<>();
  private final int numPages;
  private final Track rootTrack;
  private ArrayList<OwRemoteControl> remoteControls;
  private final int parameterCount = 8;

  public ProjectRemotes(ControllerHost host, String remoteBaseTag, int numPages) {
    this.numPages = numPages;
    //get root track from project
    rootTrack = host.getProject().getRootTrackGroup();
    remoteControls = new ArrayList<>();
    for (int i = 0; i < numPages; i++) {
      String filterExpression = remoteBaseTag + (i + 1);
      boolean useValidPageIndex = false;
      if(remoteBaseTag.isEmpty()){
        filterExpression = "";
        useValidPageIndex = true;
      }
      CursorRemoteControlsPage cursorRemoteControlsPage = rootTrack.createCursorRemoteControlsPage("rootTrackRemoteControls", 8, filterExpression);
      OwRemoteControlsPage cursorRemoteControlsPageLocked = new OwRemoteControlsPage(cursorRemoteControlsPage, i, useValidPageIndex);

      int parameterCount = cursorRemoteControlsPage.getParameterCount();
      for (int j = 0; j < parameterCount; j++) {
        RemoteControl parameter = cursorRemoteControlsPage.getParameter(j);
        Color color = Color.whiteColor();
        OwRemoteControl owRemoteControl = new OwRemoteControl(parameter, cursorRemoteControlsPageLocked, j, i, useValidPageIndex);

        remoteControls.add(owRemoteControl);
      }
      pages.add(cursorRemoteControlsPage);

      cursorRemoteControlsPage.getName().markInterested();

    }
  }

  public ArrayList<OwRemoteControl> getParametersFromRemote(int pageIndex) {
    ArrayList<OwRemoteControl> parameters = new ArrayList<>();
    int startIndex = pageIndex * parameterCount;
    int endIndex = startIndex + parameterCount;
    for (int i = startIndex; i < endIndex; i++) {
      parameters.add(this.remoteControls.get(i));
    }
    return parameters;
  }

  public OwRemoteControl getRemoteControl(int pageIndex, int parameterIndex) {
    int index = pageIndex * parameterCount + parameterIndex;
    return this.remoteControls.get(index);
  }

  public OwRemoteControl getRemoteControl(int parameterIndex) {
    return this.remoteControls.get(parameterIndex);
  }

  public CursorRemoteControlsPage getCursorRemotePage(int pageIndex) {
    return pages.get(pageIndex);
  }

  //return number of remotes
  public int getRemoteCount() {
    return numPages;
  }

  public int getNumParameters() {
    return parameterCount;
  }

  public Track getRoot() {
    return rootTrack;
  }
}
