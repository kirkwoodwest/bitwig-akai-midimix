// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.usercontrolsosc;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.OscUserControls;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.OscVUMeterBank;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.TrackBankNavigator;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes.OscProjectRemotes;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes.ProjectRemotes;
import com.kirkwoodwest.openwoods.settings.NumberSetting;
import com.kirkwoodwest.utils.LogUtil;
import com.kirkwoodwest.openwoods.osc.*;

import static com.kirkwoodwest.utils.LogUtil.reportExtensionStatus;

public class UserControlsOscExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;

  private OscHost oscHost;
  private OscUserControls oscUserControls;
  private SettableRangedValue settingNumberOfUserControls;
  private SettableStringValue settings;
  private String osc_target;
  private SettableBooleanValue settingSendValuesOnReceived;
  private SettableBooleanValue setting_zero_pad;
  private boolean zero_pad;
  private SettableBooleanValue settingVuMeterEnabled;
  private SettableBooleanValue settingVUMeterPeak;
  private SettableBooleanValue settingVUMeterRms;
  private OscController oscController;
  private OscVUMeterBank oscVuMeterBank;
  private int userControlsCount;
  private SettableRangedValue settingVumeterRange;

  protected UserControlsOscExtension(final UserControlsOscExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    Preferences preferences = host.getPreferences();

//    MidiIn input_port = host.getMidiInPort(0);
//    input_port.createNoteInput("RemoteOscInput","??????");

    {
      settings = host.getPreferences().getStringSetting("Osc Base target", "User Controls", 20, "/user");
      osc_target = settings.get();
    }
    
    //oscHost = new OscHost(host, osc_target);
    oscController = new OscController(host, osc_target );
    
    {
      settingSendValuesOnReceived = host.getPreferences().getBooleanSetting("Send Values After Received", "Parameter Settings", false);
      settingSendValuesOnReceived.addValueObserver(this::settingSendValuesOnReceived);
    }
    
    SettableBooleanValue valuesOnlyMode = host.getPreferences().getBooleanSetting("Values Only Mode", "Parameter Settings", false);
    valuesOnlyMode.markInterested();

    {
      NumberSetting setting = new NumberSetting(preferences,  "Number Of User Controls", "User Controls", 1, 1024, 1, "", 16);
      userControlsCount = setting.get();
    }



    {
      setting_zero_pad = host.getPreferences().getBooleanSetting("Index Zero Padding (i.e. user/0001, user/0002)", "User Controls", true);
      zero_pad = setting_zero_pad.get();
    }


    {
      //VU Meter
      settingVuMeterEnabled = host.getPreferences().getBooleanSetting("VU Meter Enabled", "VU Meter", false);
      NumberSetting settingVuMeterRange = new NumberSetting(preferences, "VU Meter Range", "VU Meter", 1, 1024, 1, "", 256);

      settingVUMeterPeak = host.getPreferences().getBooleanSetting("VU Meter Peak Enabled", "VU Meter", false);
      settingVUMeterPeak.addValueObserver(this::settingVuMeterPeakOutput);

      settingVUMeterRms = host.getPreferences().getBooleanSetting("VU Meter RMS Enabled", "VU Meter", false);
      settingVUMeterRms.addValueObserver(this::settingVuMeterRmsOutput);

      boolean vu_meter_enabled = settingVuMeterEnabled.getAsBoolean();
      boolean vu_peak_enabled = settingVUMeterPeak.getAsBoolean();
      boolean vu_rms_enabled = settingVUMeterRms.getAsBoolean();

      CursorTrack cursorTrack = host.createCursorTrack(0, 0);

      int vuMeter = 1;
      {
        NumberSetting setting = new NumberSetting(preferences, "Number of Channels", "VU Meter", 1, 32, 1, "", 8);
        vuMeter = setting.get();
      }

      oscVuMeterBank = new OscVUMeterBank(host, oscHost, null, vuMeter, vu_meter_enabled, settingVuMeterRange.get(), vu_peak_enabled, vu_rms_enabled);
      TrackBank trackBank = oscVuMeterBank.getTrackBank();
      TrackBankNavigator.createTrackBankCursor(host, "VU Meter", "VU Meter", "Set VU Meter", cursorTrack, trackBank);

    }
    SettableStringValue settingProjectRemotesTag = host.getPreferences().getStringSetting("Project Remotes Base Tag", "Project Remotes", 24, "usercontrols");
    settingProjectRemotesTag.markInterested();
    String projectRemotesTag = settingProjectRemotesTag.get();

    NumberSetting settingProjectRemotesCount = new NumberSetting(preferences, "Project Remotes Pages", "Project Remotes", 1, 64, 1, "", 8);


    oscUserControls = new OscUserControls(host, oscController, userControlsCount, osc_target, zero_pad, valuesOnlyMode.get());

    ProjectRemotes projectRemotes = new ProjectRemotes(host, projectRemotesTag, settingProjectRemotesCount.get());
    OscProjectRemotes oscProjectRemotes = new OscProjectRemotes(projectRemotes, oscController, "/project/remote", valuesOnlyMode.get());

    //Reset
    Signal signalSetting = host.getDocumentState().getSignalSetting("Refresh", "Refresh", "Refresh Osc");
    signalSetting.addSignalObserver(()->{
      oscController.forceNextFlush(OscUserControls.LED_GROUP_ID);
    });

    oscHost.registerOscCallback("/refresh", "Refresh", (oscConnection, oscMessage) -> {
      oscController.forceNextFlush();
      host.requestFlush();
    });


    //If your reading this... I hope you say hello to a loved one today. <3
    reportExtensionStatus(this, "Initialized");
  }

  private void settingVuMeterRmsOutput(boolean b) {
    oscVuMeterBank.setRmsOutput(b);
  }

  private void settingVuMeterPeakOutput(boolean b) {
    oscVuMeterBank.setPeakOutput(b);
  }

  private void settingSendValuesOnReceived(boolean b) {
    oscUserControls.setSendValuesAfterReceived(b);
  }

  @Override
  public void exit() {
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    reportExtensionStatus(this, "Exited");
  }

  @Override
  public void flush() {
   // userParameterHandler.refresh();
    oscController.flush();
  }
}
