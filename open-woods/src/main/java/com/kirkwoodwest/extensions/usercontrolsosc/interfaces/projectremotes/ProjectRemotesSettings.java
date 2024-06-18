package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.settings.NumberSetting;
import com.kirkwoodwest.openwoods.settings.SettingsHelper;

public class ProjectRemotesSettings {

  private final NumberSetting numPages;
  private final SettableBooleanValue useExpressionTag;
  private final SettableStringValue baseExpressionTag;

  //create settings for project remotes
  public ProjectRemotesSettings(Preferences preferences, String defaultRemoteBaseTag, int defaultNumRemotes) {
    numPages = new NumberSetting(preferences, "Number Remote Pages", "Project Remotes", 1, 32, 1, "", defaultNumRemotes);
    numPages.markInterested();
    useExpressionTag = preferences.getBooleanSetting("Use Expression Tag", "Project Remotes", false);
    useExpressionTag.markInterested();
    baseExpressionTag = preferences.getStringSetting("Base Expression Tag", "Project Remotes", Integer.MAX_VALUE - 1, defaultRemoteBaseTag);
    baseExpressionTag.markInterested();
    useExpressionTag.addValueObserver((visible) -> {
      SettingsHelper.setVisible(baseExpressionTag, visible);
    });

  }

  public int getNumPages() {
    return numPages.get();
  }

  public boolean getUseExpressionTag() {
    return useExpressionTag.get();
  }

  public String getBaseExpressionTag() {
    if(useExpressionTag.get()){
      return baseExpressionTag.get();
    } else {
      return "";
    }

  }

}
