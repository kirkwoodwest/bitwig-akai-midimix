package com.kirkwoodwest.openwoods.trackbank;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.values.BooleanValueImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class OscTrackBank {
  public static final String ID = "TrackBank";
  public static final String ADVANCED_ID = "TrackBankAdvanced";
  private final OscTrackBankPaths path;
  private final CursorTrackBank cursorTrackBank;
  private final OscController oscController;
  private final List<BooleanValueImpl> isSelected = new ArrayList<>();


  //Cursor Track style selection variables
  private CursorTrack cursorTrack = null;
  private int selectedTrackIndex = -1;
  private final ArrayList<BooleanValue> equalsValues = new ArrayList<>();

  // ... fields declaration ...

  public OscTrackBank(CursorTrackBank cursorTrackBank, String oscPath, OscController oscController) {
    this.path = new OscTrackBankPaths(oscPath);
    this.cursorTrackBank = cursorTrackBank;
    this.oscController = oscController;
    setupTrackBank(); // method call after extraction
  }

  /**
   * uses direct selectiom method for track selection.
   * @param cursorTrackBank
   * @param oscPath
   * @param oscController
   * @param cursorTrack
   */
  public OscTrackBank(CursorTrackBank cursorTrackBank, String oscPath, OscController oscController, CursorTrack cursorTrack) {
    this.path = new OscTrackBankPaths(oscPath);
    this.cursorTrackBank = cursorTrackBank;
    this.oscController = oscController;
    this.cursorTrack = cursorTrack;
    setupTrackBank(); // method call after extraction
  }

  private void setupTrackBank() {
    IntStream.range(0, cursorTrackBank.getSizeOfBank())
            .forEach(this::setupTrack);
  }

  private void setupTrack(final int trackIndex) {
    int oscIndex = trackIndex + 1;
    Track track = cursorTrackBank.getItemAt(trackIndex);
    oscController.addStringValue(ID, path.getName(oscIndex), track.name());

    if(cursorTrack!=null){
      cursorTrack.position().markInterested();
      BooleanValue equalsValue = cursorTrack.createEqualsValue(track);
      equalsValue.markInterested();
      equalsValues.add(equalsValue);
      oscController.addBooleanValue(ID, path.getSelected(oscIndex), equalsValues.get(trackIndex));

      //Select
      oscController.registerOscCallback(path.getSelect(oscIndex), "Select Track " + (oscIndex), (oscConnection, oscMessage) -> {
        cursorTrack.selectChannel(track);
        oscController.requestFlush();
      });
    } else {
      //Selection
      BooleanValueImpl booleanValue = new BooleanValueImpl(false);
      this.isSelected.add(booleanValue);

      track.addIsSelectedInEditorObserver((isSelected) -> {
        booleanValue.set(isSelected);
      });

      oscController.addBooleanValue(ID, path.getSelected(oscIndex), booleanValue);

      //Select
      oscController.registerOscCallback(path.getSelect(oscIndex), "Select Track " + (oscIndex), (oscConnection, oscMessage) -> {
        track.selectInEditor();
        oscController.requestFlush();
      });
    }
    //Exists
    //TODO Add custom exists here... probably need a way to make custom wrappers for boolean values.
    // For example this one needs to get updates from track name and also exists. a handy wrapper for updates
    // to update the boolean value would be slick.
    /**
    track.exists().markInterested();
    Supplier<Boolean> existsSupplier = () -> {
      boolean isNotDummy = !track.name().get().contains("---");
      boolean b = track.exists().get();
      return b; //&& isNotDummy;
    };
    **/
    oscController.addBooleanValue(ID, path.getTrackExists(oscIndex), track.exists());

    //Track Type ("Group", "Instrument", "Audio", "Hybrid", "Effect" or "Master")
    {
      String target = path.getTrackType(oscIndex);
      oscController.addStringValue(ID, target, track.trackType());
      // TODO: track type!!
//      track.trackType().markInterested();
//      Supplier<String> supplier = () -> {
//        return track.trackType().get().toUpperCase();
//      };

    }

    //Track Color
    oscController.addColorValue(ID, path.getTrackColor(oscIndex), track.color(), "Track Color ");
    //Standard Boolean Values
    oscController.addSettableBooleanValue(ID, path.getMute(oscIndex), track.mute(), "Mute");
    oscController.addSettableBooleanValue(ID, path.getSolo(oscIndex), track.solo(), "Solo");
    oscController.addSettableBooleanValue(ID, path.getArm(oscIndex), track.arm(), "Arm");
    oscController.addSettableBooleanValue(ID, path.getActivated(oscIndex), track.isActivated(), "Activated");

    //Parameters
    oscController.addParameter(ADVANCED_ID, path.getVolume(oscIndex), track.volume(), "Volume Level");
    oscController.addParameter(ADVANCED_ID,  path.getPan(oscIndex), track.pan(), "Pan");

    //Sends
    setupSends(oscIndex, track);
  }

  private void setupSends(int oscIndex, Track track) {
    //Sends
    SendBank sendBank = track.sendBank();

    IntStream.range(0, sendBank.getCapacityOfBank()).forEach(s -> {
      int oscSendIndex = s + 1;
      Send parameter = sendBank.getItemAt(s);

      oscController.addParameter(ADVANCED_ID, path.getSend(oscIndex, oscSendIndex), parameter, "Send " + oscSendIndex + "Volume");

      oscController.addSettableColorValue(ADVANCED_ID, path.getSendColor(oscIndex, oscSendIndex), parameter.sendChannelColor(), "Send " + oscSendIndex + "Color");
    });
  }
}
