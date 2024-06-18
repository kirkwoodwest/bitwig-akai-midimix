// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.oscprojectfader;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.extensions.oscprojectfader.fader.FaderAB;
import com.kirkwoodwest.extensions.oscprojectfader.osc.OscFaderAB;
import com.kirkwoodwest.extensions.oscprojectfader.osc.OscRandomizer;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes.OscProjectRemotes;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes.ProjectRemotes;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes.ProjectRemotesSettings;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.utils.LogUtil;

import static com.kirkwoodwest.utils.LogUtil.reportExtensionStatus;

public class OscFaderCursorExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;
  private OscHost oscHost;
  private OscController oscController;
  private OscFaderAB oscFader;
  private FaderAB faderAB;
  private Randomizer randomizer;
  private OscRandomizer oscRandomizer;
  private OscProjectRemotes oscProjectRemotes;

  protected OscFaderCursorExtension(final OscFaderCursorExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    //Convert these to preferences.
    String basePath = "/fadercursor";
    int numPages = 3;

    Preferences preferences = host.getPreferences();

    oscHost = new OscHost(host, basePath);
    oscController = new OscController(host, oscHost, basePath );

    //Settings
    ProjectRemotesSettings projectRemotesSettings = new ProjectRemotesSettings(preferences, "fader", 4);

    //--------------------------------------------------------------------------------------------------
    //Bitwig Objects
    //--

    //Project Remotes
    ProjectRemotes projectRemotes = new ProjectRemotes(host,
            projectRemotesSettings.getBaseExpressionTag(),
            projectRemotesSettings.getNumPages());

    //Randomizer
    randomizer = new Randomizer(host, projectRemotes.getRemoteControls(), projectRemotes.getCursorRemotePages());

    //Fader
    faderAB = new FaderAB(host, randomizer);

    //--------------------------------------------------------------------------------------------------
    // OSC
    //--
  //  oscProjectRemotes = new OscProjectRemotes(projectRemotes, oscController, basePath, false);
//    oscRandomizer = new OscRandomizer(oscController, randomizer, "/fader", true);
//    oscFader = new OscFaderAB(oscController, faderAB, "/fader");
//
//    //Reset
//    Signal signalSetting = host.getDocumentState().getSignalSetting("Refresh", "Refresh", "Refresh Osc");
//    signalSetting.addSignalObserver(()->{
//      oscController.forceNextFlush();
//    });
//
//    oscHost.registerOscCallback("/refresh", "Refresh", (oscConnection, oscMessage) -> {
//      oscController.forceNextFlush();
//      host.requestFlush();
//    });
//
//    //Save
//    Signal saveSetting = host.getDocumentState().getSignalSetting("Save", "Save", "Save");
//    saveSetting.addSignalObserver(()->{
//      randomizer.saveState();
//    });
//
//    //Load
//    Signal restoreSetting = host.getDocumentState().getSignalSetting("Restore", "Save", "Restore");
//    restoreSetting.addSignalObserver(()->{
//      randomizer.restoreState();
//      oscController.forceNextFlush();
//      host.requestFlush();
//    });


    //If your reading this... I hope you say hello to a loved one today. <3
    reportExtensionStatus(this, "Initialized");
  }

  @Override
  public void exit() {
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    reportExtensionStatus(this, "Exited");
  }

  @Override
  public void flush() {
    randomizer.flush();;
    faderAB.flush();
    oscController.flush();
  }
}
