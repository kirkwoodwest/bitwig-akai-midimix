package com.kirkwoodwest.closedwoods.trackmaster.multirecord;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.settings.NumberSetting;
import com.kirkwoodwest.openwoods.superbank.SuperBank;


public class MultiRecordDuo {

  private final ControllerHost host;
  private final CursorTrack cursorTrack;
  private final MultiRecordSlot midiTracks;
  private final MultiRecordSlot audioTracks;
  private final SettableStringValue sourceTrackRange;
  private final SettableStringValue targetTrackRange;


  private SuperBank superBank;
  public MultiRecordDuo(ControllerHost host, SuperBank superBank, int numTracks) {
    this.host = host;

    DocumentState documentState = host.getDocumentState();

    //Create num Tracks Settings
    int MAX_TRACKS = 32;
    int MIN_TRACKS = 1;
    SettableIntegerValue numTracksSetting = new NumberSetting(documentState,   "Number of Tracks", "Source Config", 1, 32, 1, "", 16);
    SettableIntegerValue midiTrackIndex = new NumberSetting(documentState,   "Midi Start Index", "Source Config", 1, 64, 1, "", 1);

    sourceTrackRange = documentState.getStringSetting("Source Tracks", "Source Config", 32, "");
    sourceTrackRange.markInterested();

    targetTrackRange = documentState.getStringSetting("Target Tracks", "Source Config", 32, "");
    targetTrackRange.markInterested();

    SettableIntegerValue audioTrackIndex = new NumberSetting(documentState,   "Audio Start Index", "Source Config", 1, 64, 1, "", 1);

    numTracks  = numTracksSetting.get();
    cursorTrack = host.createCursorTrack("name", "name", 0, 0, true);
    superBank.createEqualsValues(cursorTrack);

    midiTracks = new MultiRecordSlot(superBank, "midi", numTracks, 0);
    audioTracks = new MultiRecordSlot(superBank, "audio", numTracks, 8);

    midiTrackIndex.addValueObserver((value) -> {
      midiTracks.setFirstTrackIndex(value - 1);
      updateInfo();
    });

    audioTrackIndex.addValueObserver((value) -> {
      audioTracks.setFirstTrackIndex(value - 1);
      updateInfo();
    });

    numTracksSetting.markInterested();
    numTracksSetting.addValueObserver((value) -> {
      midiTracks.setTrackCount(value);
      audioTracks.setTrackCount(value);
      updateInfo();
    });


    documentState.getSignalSetting("Set Midi Position", "Source Config", "Set Midi Position").addSignalObserver(() -> {
      int cursorIndex = superBank.getCursorIndex(cursorTrack);
      midiTracks.setFirstTrackIndex(cursorIndex);
      updateInfo();
    });
    documentState.getSignalSetting("Set Audio Position", "Source Config", "Set Audio Position").addSignalObserver(() -> {
      int cursorIndex = superBank.getCursorIndex(cursorTrack);
      audioTracks.setFirstTrackIndex(cursorIndex);
      updateInfo();
    });

    Signal signalSetting = documentState.getSignalSetting("Copy Source Names", "Target", "Copy Source Names");
    signalSetting.addSignalObserver(() -> {
      Track[] tracks = midiTracks.getTracks();
      String[] names = midiTracks.getNames(tracks);
      Color[] colors = midiTracks.getColors(tracks);
      audioTracks.setNames(names, true);
      audioTracks.setColors(colors);
      String firstTrackName = midiTracks.getFirstTrackName();
      String lastTrackName = midiTracks.getLastTrackName();
      targetTrackRange.set("[ " + firstTrackName + " ]-[ " + lastTrackName + " ]");
    });

    documentState.getSignalSetting("Activate Source", "Source", "Activate Source").addSignalObserver(() -> {
      boolean b = midiTracks.getActivated();
      midiTracks.setActivated(!b);
    });


    documentState.getSignalSetting("Arm Source", "Source", "Arm Source").addSignalObserver(() -> {
      boolean b = midiTracks.getArm();
      midiTracks.setArm(!b);
    });

    documentState.getSignalSetting("Arm Target", "Source", "Arm Target").addSignalObserver(() -> {
      boolean b = audioTracks.getArm();
      audioTracks.setArm(!b);
    });

//    cursorTrack.isPinned().set(true);
//    cursorTrack.name().addValueObserver((name) -> {
//      trackBank.s
//    });
//    this.track_count = numTracks;
//    this.activate_with_arm = activate_with_arm;
//
//    String cursor_track_name = "muli_record_" + name
//    this.superBank.createEqualsValues(cursor_track);
//
//
  }

  private void updateInfo(){


    String firstTrackName = midiTracks.getFirstTrackName();
    String lastTrackName = midiTracks.getLastTrackName();
    sourceTrackRange.set("[ " + firstTrackName + " ]-[ " + lastTrackName + " ]");

    firstTrackName = audioTracks.getFirstTrackName();
    lastTrackName = audioTracks.getLastTrackName();
    targetTrackRange.set("[ " + firstTrackName + " ]-[ " + lastTrackName + " ]");
  }

}
