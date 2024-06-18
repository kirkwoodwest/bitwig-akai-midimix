package com.kirkwoodwest.openwoods.cursortrack;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.closedwoods.loader.element.ILoaderElement;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.remotecontrol.*;
import com.kirkwoodwest.openwoods.osc.OscHost;

import com.kirkwoodwest.openwoods.trackbank.OscTrackBankCursorTrack;

import java.util.ArrayList;

public class OscCursorTrack implements OscTrackBankCursorTrack, ILoaderElement {
  public static final String ID = "CursorTrack";
  public static final String REMOTES_ID = "CursorTrackRemotes";
  private final int VU_METER_RESOLUTION = 1024;

  public static final String LED_GROUP_ID = "CursorTrack";
  private final OscHost oscHost;

  private OscController oscController;
  private final CursorTrack cursorTrack;
  private final ArrayList<OwRemoteControl> deviceParameters = new ArrayList<>();
  private final ArrayList<String> deviceParameterPaths = new ArrayList<>();
  private final OscCursorTrackPaths path;
  private boolean playingNotesEnabled = true;

  private final String oscPath;
  
  public OscCursorTrack(CursorTrack cursorTrack, String oscPath, OscController oscController) {
    this.path = new OscCursorTrackPaths(oscPath);
    this.cursorTrack = cursorTrack;
    this.oscController = oscController;
    oscHost = oscController.getOscHost();
    this.oscPath = oscPath;

    Track parentTrack = cursorTrack.createParentTrack(0, 0);

    //Parent track exists() and name() to the osc controller
    oscController.addBooleanValue(ID, path.PARENT_TRACK_EXISTS, parentTrack.exists());
    oscController.addStringValue(ID, path.PARENT_TRACK_NAME, parentTrack.name());

    //Track Name
    oscController.addStringValue(ID, path.NAME, cursorTrack.name());

    //Volume
    oscController.addParameter(ID, path.VOLUME, cursorTrack.volume(), "Selected Volume Level");
    cursorTrack.volume().setIndication(true);

    //Pan
    oscController.addParameter(ID, path.PAN, cursorTrack.pan(), "Selected Pan");
    cursorTrack.pan().setIndication(true);

    //Mute
    oscController.addSettableBooleanValue(ID, path.MUTE, cursorTrack.mute(), "Selected Mute");

    //Solo
    oscController.addSettableBooleanValue(ID, path.SOLO, cursorTrack.solo(), "Selected Solo");

    //Arm
    oscController.addSettableBooleanValue(ID, path.ARM, cursorTrack.arm(), "Selected Arm");

    //Activated
    oscController.addSettableBooleanValue(ID, path.ACTIVATED, cursorTrack.isActivated(), "Selected Activated");

    //Track Type
    //"Group", "Instrument", "Audio", "Hybrid", "Effect" or "Master"
    oscController.addStringValue(ID, path.TYPE, cursorTrack.trackType());
    
    {
      //Sends
      SendBank sendBank = cursorTrack.sendBank();
      int send_size = sendBank.getCapacityOfBank();

      for (int s = 0; s < send_size; s++) {
        Send parameter = sendBank.getItemAt(s);
        parameter.setIndication(true);

        String sendPath = this.oscPath + "/send/" + (s + 1);

        //Send Level
        oscController.addParameter(ID, sendPath, parameter, "Send " + (s + 1) + "Level");

        {
          //Send Color
          String target = sendPath + "/color";
          final SettableColorValue sendColorValue = parameter.sendChannelColor();
          oscController.addSettableColorValue(ID, target, sendColorValue, "Send Color");
        }
      }
    }

    {
      //Track Color
      String target = this.oscPath + "/color";
      final SettableColorValue settableColorValue = cursorTrack.color();
      oscController.addSettableColorValue(ID, target, settableColorValue, "Track Color");
    }
  }

  //TODO: this belongs on the cursor Track object
  @Override
  public void selectTrack(Track track) {
    cursorTrack.selectChannel(track);
  }

  public void refresh() {
    oscController.forceNextFlush(ID);
  }

  public void enable(boolean enabled) {
    oscController.setGroupEnabled(ID, enabled);
  }
}
