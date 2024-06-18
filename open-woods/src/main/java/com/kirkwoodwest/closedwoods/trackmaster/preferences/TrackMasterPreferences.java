package com.kirkwoodwest.closedwoods.trackmaster.preferences;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.openwoods.settings.NumberSetting;

public class TrackMasterPreferences {

  private final NumberSetting cursorTracksCount;
  private final NumberSetting clipLauncherTracksCount;
  private final NumberSetting remotePagesCount;
  private final NumberSetting projectRemotePagesCount;
  private final NumberSetting clipLauncherSceneCount;

  public TrackMasterPreferences(ControllerHost host){
    com.bitwig.extension.controller.api.Preferences preferences = host.getPreferences();
    cursorTracksCount = new NumberSetting(preferences, "# Tracks", "Mix Bank", 1, 32, 1, " Tracks", 21);
    remotePagesCount = new NumberSetting(preferences, "# Remote Pages", "Mix Bank", 1, 32, 1, " Pages", 16);

    clipLauncherTracksCount = new NumberSetting(preferences, "# Tracks", "Clip Launcher", 1, 32, 1, "Tracks", 16);
    clipLauncherSceneCount = new NumberSetting(preferences, "# Scenes", "Clip Launcher", 1, 32, 1, "Scenes", 8);

    projectRemotePagesCount = new NumberSetting(preferences, "# Pages", "Project Remotes", 1, 32, 1, "Pages", 4);
  }

  public int getCursorTrackCount(){
    return cursorTracksCount.get();
  }

  public int getClipLauncherTrackCount(){
    return clipLauncherTracksCount.get();
  }

  public int getRemotePagesCount(){
    return remotePagesCount.get();
  }

  public int getProjectRemotePagesCount(){
    return projectRemotePagesCount.get();
  }

  public int getSceneCount() {
    return clipLauncherSceneCount.get();
  }
}
