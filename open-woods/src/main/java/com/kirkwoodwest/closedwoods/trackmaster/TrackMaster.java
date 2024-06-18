package com.kirkwoodwest.closedwoods.trackmaster;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.closedwoods.trackmaster.cursor.OscDeviceBank;
import com.kirkwoodwest.closedwoods.trackmaster.extensions.TrackMasterDefinition;
import com.kirkwoodwest.closedwoods.trackmaster.pluginwindow.CursorDevicePluginWindow;
import com.kirkwoodwest.closedwoods.trackmaster.pluginwindow.OscCursorDevicePluginWindow;
import com.kirkwoodwest.closedwoods.trackmaster.preferences.TrackMasterPreferences;
import com.kirkwoodwest.closedwoods.trackmaster.projectremotes.*;
import com.kirkwoodwest.closedwoods.trackmaster.xytool.XYTool;
import com.kirkwoodwest.openwoods.cursortrack.OscCursorDevice;
import com.kirkwoodwest.openwoods.cursortrack.OscCursorTrack;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.oscthings.launcher.OscClipLauncher;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.savelist.CursorTrackColorCollectionSaved;
import com.kirkwoodwest.openwoods.savelist.DoubleRangeCollectionSaved;
import com.kirkwoodwest.openwoods.savelist.StringValueCollectionSaved;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.oscthings.browser.OscBrowser;
import com.kirkwoodwest.openwoods.settings.ShowScriptConsole;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackHelper;
import com.kirkwoodwest.openwoods.trackbank.OscTrackBank;
import com.kirkwoodwest.utils.LogUtil;
import com.kirkwoodwest.openwoods.oscthings.OscTransport;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackMaster {
  private final List<Flushable> flushables = new ArrayList<>();
  private final ControllerExtension extension;
  private boolean shutdown;

  public TrackMaster(ControllerExtension extension, TrackMasterDefinition definition) {
    ControllerHost host = extension.getHost();
    this.extension = extension;
    LogUtil.init(host);

    //Setup Preference and Document Settings
    Preferences preferences = host.getPreferences();
    DocumentState documentState = host.getDocumentState();

    TrackMasterPreferences trackMasterPreferences = new TrackMasterPreferences(host);

    //Setup Number Of Sends
    //int MAX_SENDS = 8;
    //SettableRangedValue numberSetting = preferences.getNumberSetting("Number of Sends", "Cursor", 1, MAX_SENDS, 1, "", 5);
    //int numSends = (int) Math.floor(numberSetting.get() * MAX_SENDS) + 1;
    int numSends = 6;
    int numMixTracks = trackMasterPreferences.getCursorTrackCount();
    int numClipLauncherTracks = trackMasterPreferences.getClipLauncherTrackCount();
    int numRemotePages = trackMasterPreferences.getRemotePagesCount();
    int numProjectRemotePages = trackMasterPreferences.getProjectRemotePagesCount();
    int numDevices = 8;
    int numScenes = trackMasterPreferences.getSceneCount();

    Application app = host.createApplication();

    //Super Bank
    CursorTrackHelper cursorTrackHelper = new CursorTrackHelper(host, 64);

    //Bitwig Objects
    CursorTrack cursorTrack = host.createCursorTrack(numSends, 2);
   // TrackBank trackBank = host.createTrackBank(numMixTracks, numSends, numScenes, true);
    ProjectRemotes projectRemotes = new ProjectRemotes(host, "", numProjectRemotePages);
    DeviceBank deviceBank = cursorTrack.createDeviceBank(numDevices);
    PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice();

    //Setup
    //Auto Builder for track remotes...
    ProjectRemotesAutoBuild projectRemotesAutoBuild = new ProjectRemotesAutoBuild(host, projectRemotes, numProjectRemotePages);


    //-------------------------------------------------------------------------------------------------------------------------
    // Midifighter Twister
    //
    HardwareMFT hardwareMFT = new HardwareMFT(host, 0, null);
    flushables.add(hardwareMFT);
    HardwareMF3D hardwareMF3D = new HardwareMF3D(host, 1, null);
    flushables.add(hardwareMF3D);

    //Trackbank
    CursorTrackBank cursorTrackBank = new CursorTrackBank(host, cursorTrackHelper, numMixTracks, numSends, numScenes, "CTB");
    cursorTrackBank.setIndicatorStyle(CursorTrackBank.INDICATOR_STYLE.VOLUME);
    CursorTrackBank cursorTrackBankAlt = new CursorTrackBank(host, cursorTrackHelper, numClipLauncherTracks, numSends, numScenes, "CTB Alt");
    cursorTrackBankAlt.setIndicatorStyle(CursorTrackBank.INDICATOR_STYLE.PAN);


    //-------------------------------------------------------------------------------------------------------------------------
    // Open Sound Control
    //
    OscHost oscHost = new OscHost(host, "/trackmaster");
    flushables.add(oscHost);
    OscController oscController = new OscController(host, oscHost, "/trackmaster");
    flushables.add(oscController);

    //Implement this somehow...
    //oscController.enableLastTouchedParameter();

    OscControllerModes oscControllerModes = new OscControllerModes(oscController, "/trackmaster/mode");
    OscCursorTrack oscCursorTrack = new OscCursorTrack(cursorTrack, "/trackmaster/cursor", oscController);
    OscCursorDevice oscCursorDevice = new OscCursorDevice(cursorDevice, oscController, "/trackmaster/cursor/device", numRemotePages, "");
//    OscTrackBank oscTrackBank = new OscTrackBank(cursorTrackBank, "/trackmaster/trackbank", oscController, cursorTrack);
//    OscDeviceBank.create(deviceBank, oscCursorTrack.getCursorDevice(), "/trackmaster/cursor/devicebank", oscController);
//    OscClipLauncher oscClipLauncher = new OscClipLauncher(host, cursorTrackBankAlt, oscController, "/trackmaster");

    //Cursor Device Plugin Window
    //TODO: THis is dope!
    CursorDevicePluginWindow cursorDevicePluginWindow = new CursorDevicePluginWindow(cursorDevice);
    OscCursorDevicePluginWindow.create(oscController, "/trackmaster/cursor", cursorDevicePluginWindow);

    //Last Touched Parameter initialization
    //TODO This is dope!
    OscLastTouchedParameter instance = OscLastTouchedParameter.getInstance();
    instance.init(oscController, cursorTrack, cursorDevice);

    //Browser
    OscBrowser oscBrowser = new OscBrowser(host, oscController, "/trackmaster/browser");

    //Vu Meter
    //OscVuMeterBank oscVuMeterLeds = new OscVuMeterBank(oscController, cursorTrack, "/trackmaster/cursor", cursorTrackBank, "/trackmaster/trackbank");
    //flushables.add(oscVuMeterLeds);

    //Transport
    OscTransport oscTransport = new OscTransport(host, oscController, "/trackmaster");

    //XY Tool
    int numXYTools = 4;
    for (int i = 0; i < numXYTools; i++) {
      new XYTool(host, oscController, "/trackmaster/xytool", i + 1);
    }

    //Setup Modes
    setupOscModes(oscControllerModes);
    oscControllerModes.setMode("devices");


    //-------------------------------------------------------------------------------------------------------------------------
    // OSC Refresh, these should get moved into their respective classes. just here for convenience...
    //
    oscController.registerOscCallback("/trackmaster/refresh", "Refresh All Osc Parameters", (oscConnection, oscMessage) -> {
      oscController.forceNextFlush();
      host.requestFlush();
    });

    oscController.registerOscCallback("/trackmaster/trackbank/refresh", "Refresh TrackBank", (oscConnection, oscMessage) -> {
      oscController.forceNextFlush("TrackBank");
      host.requestFlush();
    });

    oscController.registerOscCallback("/trackmaster/cursor/device/remote/refresh", "Refresh Remote Parameters", (oscConnection, oscMessage) -> {
      oscController.forceNextFlush(OscCursorTrack.REMOTES_ID);
      host.requestFlush();
    });

    oscController.registerOscCallback("/trackmaster/cursor/refresh", "Refresh Cursor Parameters", (oscConnection, oscMessage) -> {
      oscController.forceNextFlush("CursorTrack");
      host.requestFlush();
    });

    //-------------------------------------------------------------------------------------------------------------------------
    // Signal Based OSC Refresh
    //
    {
      Signal signalSetting = documentState.getSignalSetting("Force Update Cursor Leds", "Force Update", "Update Cursor Leds");
      signalSetting.addSignalObserver(() -> {
        oscController.forceNextFlush(OscCursorTrack.REMOTES_ID);
        oscController.forceNextFlush(OscCursorTrack.ID);
      });

      Signal signalSetting3 = documentState.getSignalSetting("Force Update All Leds", "Force Update", "Update Cursor Leds");
      signalSetting3.addSignalObserver(() -> {
        oscController.forceNextFlush();
        host.requestFlush();
      });

      //-------------------------------------------------------------------------------------------------------------------------
      // Mapping System
      //
      OscMappers oscMappers = new OscMappers(oscController, "/trackmaster/map");
      {
        //Build first set of project remotes for MF3D
        ArrayList<OwRemoteControl> parametersFromRemote0 = projectRemotes.getParametersFromRemote(0);
        ArrayList<OwRemoteControl> owRemoteControls1 = projectRemotes.getParametersFromRemote(1);
        ArrayList<OwRemoteControl> mft1Remotes = new ArrayList<>();
        mft1Remotes.addAll(parametersFromRemote0);
        mft1Remotes.addAll(owRemoteControls1);


        CursorTrackColorCollectionSaved remoteColors = new CursorTrackColorCollectionSaved(host, "ProjectRemoteColors", cursorTrack, mft1Remotes.size());
        flushables.add(remoteColors);
        StringValueCollectionSaved trackNames = new StringValueCollectionSaved(host, "ProjectRemoteTrackNames", cursorTrack.name(), mft1Remotes.size());
        flushables.add(trackNames);
        StringValueCollectionSaved deviceNames = new StringValueCollectionSaved(host, "ProjectRemoteDeviceNames", cursorDevice.name(),  mft1Remotes.size());
        flushables.add(deviceNames);

        //Project Remotes
        OscMFTMapper oscMFTMapper = new OscMFTMapper(oscMappers, hardwareMFT, mft1Remotes, oscController, "/trackmaster/mf1", remoteColors, trackNames, deviceNames);
        MFTRemotes mftRemotes = new MFTRemotes(host, hardwareMFT, mft1Remotes, remoteColors, oscMFTMapper);
      }
      {
        //Build second set of project remotes for MF3D
        ArrayList<OwRemoteControl> parametersFromRemote0 = projectRemotes.getParametersFromRemote(2);
        ArrayList<OwRemoteControl> owRemoteControls1 = projectRemotes.getParametersFromRemote(3);
        ArrayList<OwRemoteControl> mf3dRemotes = new ArrayList<>();
        mf3dRemotes.addAll(parametersFromRemote0);
        mf3dRemotes.addAll(owRemoteControls1);

        CursorTrackColorCollectionSaved remoteColors = new CursorTrackColorCollectionSaved(host, "mf3dColors", cursorTrack, mf3dRemotes.size());
        flushables.add(remoteColors);
        StringValueCollectionSaved trackNames = new StringValueCollectionSaved(host, "mf3dProjectNames", cursorTrack.name(), mf3dRemotes.size());
        flushables.add(trackNames);
        StringValueCollectionSaved deviceNames = new StringValueCollectionSaved(host, "mf3dDeviceNames", cursorDevice.name(), mf3dRemotes.size());
        flushables.add(deviceNames);

        DoubleRangeCollectionSaved mf3dRanges = new DoubleRangeCollectionSaved(host, "mf3dRanges", mf3dRemotes.size());
        flushables.add(mf3dRanges);
        //Project Remotes
        OscMF3DMapper OscMF3dMapper = new OscMF3DMapper(oscMappers, hardwareMF3D, mf3dRemotes, oscController, "/trackmaster/mf2", remoteColors, trackNames, deviceNames, mf3dRanges);
        MF3DRemotes mftRemotes = new MF3DRemotes(host, hardwareMF3D, mf3dRemotes, remoteColors, mf3dRanges, OscMF3dMapper);
      }
      // MFTProjectRemotes MFTProjectRemotes2 = new MFTProjectRemotes(host, hardwareTwister,  projectRemotes, cursorTrack, trackBank, 0, 0);
      //Need to make a class map of this along with the MFT mapping. Should this be a conglomatron? or just use the mft mapping thing.

      //-------------------------------------------------------------------------------------------------------------------------
      // Misc Utilities
      //

      //Show Console Button
      ShowScriptConsole.create(host, documentState);

      //Multi Record
      //TODO: SET THIS UP VIA OSC... multiTrackDuoOsc
      //   MultiRecordDuo multiTrackDuo = new MultiRecordDuo(host, superBank, numTracks);

      LogUtil.reportExtensionStatus(extension, "Initialized.");
    }
  }

  private void setupOscModes(OscControllerModes oscControllerModes) {
    //Create All the different modes... this should be moved out into the extension but here for building...

//    oscContrllerModes.createMode("browser");
//    oscContrllerModes.addGroup("browser", OscBrowser.ID);
//    oscContrllerModes.addGroup("browser", OscCursorTrack.ID);
//    oscContrllerModes.addGroup("browser", OscCursorDevicePluginWindow.ID);
//    oscContrllerModes.addGroup("browser", OscTrackBank.ID);

    //Always Mapped
    //oscContrllerModes.addGroup("control", OscMFTMapper.ID);

    oscControllerModes.createMode("xytool");
    oscControllerModes.addGroup("xytool", XYTool.ID);

    oscControllerModes.createMode("device_xytool");
    oscControllerModes.addGroup("device_xytool", XYTool.ID);
    oscControllerModes.addGroup("device_xytool", OscCursorTrack.ID);

    oscControllerModes.createMode("devices");
    oscControllerModes.addGroup("devices", OscCursorTrack.ID);
    oscControllerModes.addGroup("devices", OscCursorTrack.REMOTES_ID);
    oscControllerModes.addGroup("devices", OscCursorDevicePluginWindow.ID);
    oscControllerModes.addGroup("devices", OscTrackBank.ID);
    oscControllerModes.addGroup("devices", OscDeviceBank.ID);
    oscControllerModes.addGroup("devices", XYTool.ID);
    oscControllerModes.addGroup("devices", OscLastTouchedParameter.ID);

    oscControllerModes.createMode("mix");
    oscControllerModes.addGroup("mix", OscTrackBank.ID);
    oscControllerModes.addGroup("mix", OscTrackBank.ADVANCED_ID);
    oscControllerModes.addGroup("mix", OscLastTouchedParameter.ID);

    oscControllerModes.createMode("clip_launcher");
    oscControllerModes.addGroup("clip_launcher", OscClipLauncher.ID);
    oscControllerModes.addGroup("clip_launcher", OscTrackBank.ID);


    oscControllerModes.createMode("control");
    oscControllerModes.addGroup("control", OscMFTMapper.ID_ADVANCED);

    oscControllerModes.disableAllModes();
  }

  public void exit() {
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    shutdown = true;
    LogUtil.reportExtensionStatus(extension,"Exited.");
  }

  public void flush() {
    if(!shutdown) {
      for (Flushable flushable : flushables) {
        try {
          flushable.flush();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
