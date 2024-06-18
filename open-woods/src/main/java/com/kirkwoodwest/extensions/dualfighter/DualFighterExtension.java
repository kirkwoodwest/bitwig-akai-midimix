// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.dualfighter;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;

import com.kirkwoodwest.openwoods.cursortrack.OscCursorTrack;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.oscthings.browser.OscBrowser;
import com.kirkwoodwest.openwoods.oscthings.vu.VuCursorTrackBankOsc;
import com.kirkwoodwest.openwoods.superbank.SuperBank;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackHelper;
import com.kirkwoodwest.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DualFighterExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;
  private DeviceBank deviceBank;

  //Bitwig Objects
  private CursorTrack cursorTrack;

  private OscCursorTrack oscCursorTrack;

  private VuCursorTrackBankOsc vuCursorTrackBankOsc;
  private OscHost oscHost;

  private OscBrowser oscBrowser;
  private HardwareMFT hardwareMFT;
  private SuperBank superBank;
  private boolean shutdown;
  private CursorTrackHelper cursorTrackHelper;


  protected DualFighterExtension(final DualFighterExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);


    //Setup Preference and Document Settings
    Preferences preferences = host.getPreferences();
    DocumentState documentState = host.getDocumentState();

    cursorTrackHelper = new CursorTrackHelper(host, 64);
    CursorTrackBank trackParser = new CursorTrackBank(host, cursorTrackHelper, 10, 2, 2, "Track Parser");
    trackParser.setIndicatorStyle(CursorTrackBank.INDICATOR_STYLE.VOLUME);

    CursorTrack itemAt = trackParser.getItemAt(0);

    PinnableCursorDevice cursorDevice = itemAt.createCursorDevice("Cursor Device", "Cursor Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
    cursorDevice.addDirectParameterIdObserver((id) -> {
      host.println("Direct Parameter ID: " + id);

    });


    //What this extension is...
    //Create an object that you can pass Midi Light Data too... maybe it could be any midi data light based on its data type.
    //Setup 4 Cursor Tracks 4 Cursor Devices and 4 Remote Banks
    //Each oneshould be addressable via commander and osc.
    //If you can set up the class to support osc, commander and midi.
    //Midi should be generic somehow, like how so?
    //Pass in a Hardware

    //This extension should just

//    ArrayList<CursorTrack> cursors = trackParser.getCursors();
//    int index = 0;
//    IntStream.range(0,cursors.size()).forEach(i -> {
//      CursorTrack cursorTrack1 = cursors.get(i);
//      TrackBank trackBank1 = cursorTrack1.createSiblingsTrackBank(1, 1, 16, true, true);
//      trackBank1.setShouldShowClipLauncherFeedback(true);
//      Track itemAt = trackBank1.getItemAt(0);
//      itemAt.clipLauncherSlotBank().getItemAt(0).isPlaying().markInterested();
//
//    });
//
//    ArrayList<CursorTrack> cursors = trackParser.getCursors();
//    IntStream.range(0,cursors.size()).forEach(i -> {
//      final CursorTrack cursorTrack1 = cursors.get(i);
//                //      final TrackBank trackBank = host.createTrackBank(1, 2, 4);
//                //      trackBank.followCursorTrack(cursorTrack1);
//                //      trackBank.setShouldShowClipLauncherFeedback(true);
//                //      trackBank.sceneBank().setIndication(true);
//                //      final Track itemAt = trackBank.getItemAt(0);0
//      Track itemAt = cursorTrack1;
//      itemAt.clipLauncherSlotBank().setIndication(true);
//      itemAt.clipLauncherSlotBank().getItemAt(0).name().addValueObserver(s -> {
//        host.println(s);
//      });
//    });

    //Multi Record
      //TODO: SET THIS UP VIA OSC... multiTrackDuoOsc
   //   MultiRecordDuo multiTrackDuo = new MultiRecordDuo(host, superBank, numTracks);

    LogUtil.reportExtensionStatus(this, "Initialized.");

  }

  @Override
  public void exit() {
    // TODO: Perform any cleanup once the driver exits

    LogUtil.reportExtensionStatus(this,"Exited.");
  }

  @Override
  public void flush() {

  }
  public void debug(){
    cursorTrackHelper.debugBank();
  }
}
