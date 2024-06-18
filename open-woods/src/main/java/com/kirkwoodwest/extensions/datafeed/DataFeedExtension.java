// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.datafeed;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;

import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.oscthings.OscTransport;
import com.kirkwoodwest.openwoods.superbank.SuperBank;

import com.kirkwoodwest.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import static com.kirkwoodwest.utils.LogUtil.reportExtensionStatus;

public class DataFeedExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;
  private HashMap<String, Action> selectActions = new HashMap<>();
  private SuperBank superBank;
  private SceneBank sceneBank;
  private ArrayList<SettingRemotePage> settingRemotePages = new ArrayList<>();
  private OscController oscController;

  protected DataFeedExtension(final DataFeedExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    Preferences preferences = host.getPreferences();

    OscHost oscHost = new OscHost(host, "/datafeed");
    oscController = new OscController(host, oscHost, "/datafeed");

    {
      //Transport
      Transport transport = host.createTransport();
      OscTransport oscTransport = new OscTransport(host, oscController, "/datafeed");
    }

    //Settings for Notes
    SettableStringValue settingNotePath = preferences.getStringSetting("Note Path", "Notes", 32, "/datafeed/notes");
    settingNotePath.markInterested();
    //Settings for clips
    SettableStringValue settingClipPath = preferences.getStringSetting("Clip Path", "Clips", 32, "/datafeed/clip/name");
    settingClipPath.markInterested();

    int numScenes = 128;
    int numPages = 1;
    int numPageParameters = 8;

    //settings for remote controls page
    for (int i = 0; i < numPages; i++) {
      for (int j = 0; j < numPageParameters; j++) {
        SettingRemotePage settingRemotePage = new SettingRemotePage(preferences, i+1, j+1);
        settingRemotePages.add(settingRemotePage);
      }
    }

    //Create CursorTrack
    CursorTrack cursorTrack = host.createCursorTrack("DataFeed Cursor Track", "DataFeed Cursor Track", 0, numScenes, true);

    //Create CursorDevice
    PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice("DataFeed Cursor Device", "DataFeed Cursor Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);

    //Do this so we get a cursor device so we can lock it...
    //cursorDevice.createCursorRemoteControlsPage(8);

    DocumentState documentState = host.getDocumentState();
    documentState.getSignalSetting("Lock", "Lock", "Lock").addSignalObserver(() -> {
      cursorTrack.isPinned().set(true);
      cursorDevice.isPinned().set(true);
    });

    documentState.getSignalSetting("Unlock", "Lock", "Unlock").addSignalObserver(() -> {
      cursorTrack.isPinned().set(false);
      cursorDevice.isPinned().set(false);
    });

    //Create CursorRemoteControlsPage
    for (int i = 0; i < numPages; i++) {
    //  CursorRemoteControlsPage cursorRemoteControlsPage = cursorDevice.createCursorRemoteControlsPage("DataFeed " + i, 8, "datafeed" + (i+1));
        CursorRemoteControlsPage cursorRemoteControlsPage = cursorDevice.createCursorRemoteControlsPage(8);

     // Lock.create(cursorRemoteControlsPage, i);

      //Create parameters
      for (int j = 0; j < 8; j++) {
        final int pageIndex = i +1;
        final int parameterIndex = j +1;
        Parameter parameter = cursorRemoteControlsPage.getParameter(j);
        parameter.setIndication(true);
        SettableRangedValue value = parameter.value();
        oscController.addRangedValue("DataFeed",  "/datafeed/" + pageIndex + "/" + parameterIndex, value);

        //TODO Fix this... Need a custom thing for this type of parameter.
//        ledDataFeedParameter.setPath(settingRemotePages.get(i * 8 + j).getPath());
//        ledDataFeedParameter.setType(settingRemotePages.get(i * 8 + j).getType());
//        ledDataFeedParameter.setDataType(settingRemotePages.get(i * 8 + j).getRange());
//        oscController.addLed("Remotes", ledDataFeedParameter);
//        ledDataFeedParameters.add(ledDataFeedParameter);
      }
    }

    //project remote 1
    Track rootTrackGroup = host.getProject().getRootTrackGroup();
    CursorRemoteControlsPage cursorRemoteControlsPage = rootTrackGroup.createCursorRemoteControlsPage(8);
    RemoteControl parameter = cursorRemoteControlsPage.getParameter(0);
    parameter.modulatedValue().addValueObserver((v)->{
      oscHost.addMessageToQueue("/datafeed/project/1", v);
    });


    //notes in
    {
      String path = settingNotePath.get();
      cursorTrack.playingNotes().addValueObserver((notes) -> {
        for (int i = 0; i < notes.length; i++) {
          if (notes[i] != null) {
            PlayingNote note = notes[i];
            int pitch = note.pitch();
            int velocity = note.velocity();
            oscHost.addMessageToQueue(path, pitch, velocity);
          }
        }
      });
    }
    {
      String path = settingClipPath.get();
      ClipLauncherSlotBank clipLauncherSlotBank = cursorTrack.clipLauncherSlotBank();
      int capacityOfBank = clipLauncherSlotBank.getCapacityOfBank();
      for (int i = 0; i < capacityOfBank; i++) {
        clipLauncherSlotBank.getItemAt(i).name().markInterested();
      }
      clipLauncherSlotBank.addIsPlayingObserver((slotIndex, isPlaying) -> {
        String s = clipLauncherSlotBank.getItemAt(slotIndex).name().get();
        if (isPlaying) {
          oscHost.addMessageToQueue(path, s);
        }

      });
    }



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
    oscController.flush();
  }
}
