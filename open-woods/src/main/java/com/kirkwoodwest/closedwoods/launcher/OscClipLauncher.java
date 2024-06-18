package com.kirkwoodwest.closedwoods.launcher;

import com.bitwig.extension.api.opensoundcontrol.OscMethodCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.closedwoods.loader.element.ILoaderElement;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.oscthings.songbank.SongBankOscClipLauncher;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;

import java.util.stream.IntStream;

public class OscClipLauncher implements SongBankOscClipLauncher, ILoaderElement {

  public static final String ID = "ClipLauncher";

  private final OscController oscController;
  private final OscClipLauncherPaths oscPath;
  private final CursorTrackBank cursorTrackBank;

  public OscClipLauncher(ControllerHost host, CursorTrackBank cursorTrackBank, OscController oscController, String oscPath) {
    this.oscPath = new OscClipLauncherPaths(oscPath);
    this.oscController = oscController;
    this.cursorTrackBank = cursorTrackBank;
    int numScenes = cursorTrackBank.getItemAt(0).clipLauncherSlotBank().getSizeOfBank();
    oscController.addIndexedPathInfo(this.oscPath.getTrackIndex(), "Track Index 1 - " +  cursorTrackBank.getSizeOfBank());
    oscController.addIndexedPathInfo(this.oscPath.getClipIndex(), "Clip Index 1 - " +  numScenes);
    setupCursorTrackBank(cursorTrackBank, numScenes);



    //Scroll Position set all
    oscController.registerOscIntegerCallback(oscPath + "/scroll_position", "get / set scroll position of bank", this::scrollPosition);
  }

  private void scrollPosition(Integer integer) {
    IntStream.range(0,  cursorTrackBank.getSizeOfBank())
      .forEach(trackIndex->{
        ClipLauncherSlotBank clipLauncherSlotBank = cursorTrackBank.getItemAt(trackIndex).clipLauncherSlotBank();
        clipLauncherSlotBank.scrollPosition().set(integer);
      });
  }

  private void setupCursorTrackBank(CursorTrackBank cursorTrackBank, int numScenes) {
    IntStream.range(0, cursorTrackBank.getSizeOfBank())
      .forEach(trackIndex->{

        Track track = cursorTrackBank.getItemAt(trackIndex);
        oscController.addStringValue(ID, oscPath.getTrackName(trackIndex), track.name());
        oscController.addSettableColorValue(ID, oscPath.getTrackColor(trackIndex), track.color(), "Track Color");

        ClipLauncherSlotBank clipLauncherSlotBank = track.clipLauncherSlotBank();
        setupClipLauncherSlotBank(clipLauncherSlotBank, trackIndex);
        setupClipLaunchStops(trackIndex, clipLauncherSlotBank, track);

        //Clip level
        IntStream.range(0,numScenes)
          .forEach(slotIndex->{
            setupSlot(trackIndex, slotIndex, clipLauncherSlotBank);
          });
      });
  }

  private void setupSlot(int trackIndex, int slotIndex, ClipLauncherSlotBank clipLauncherSlotBank) {
    ClipLauncherSlot slot = clipLauncherSlotBank.getItemAt(slotIndex);
    setupSlotStatus(trackIndex, slotIndex, slot);
    setupSlotLaunch(trackIndex, slotIndex, slot);
    setupSlotMisc(trackIndex, slotIndex, slot);
  }

  private void setupSlotStatus(int trackIndex, int slotIndex, ClipLauncherSlot slot) {
    oscController.addStringValue(ID, oscPath.getClipName(trackIndex, slotIndex), slot.name());
    oscController.addSettableColorValue(ID, oscPath.getClipColor(trackIndex, slotIndex), slot.color(), "Clip Color");
    oscController.addBooleanValue(ID, oscPath.getClipExists(trackIndex, slotIndex), slot.exists());
    oscController.addBooleanValue(ID, oscPath.getClipHasContent(trackIndex, slotIndex), slot.hasContent());
    //oscController.addBooleanValue(ID, oscPath.getClipIsPlaying(trackIndex, slotIndex), slot.isPlaying());
    slot.isPlaying().addValueObserver(isPlaying -> {
      oscController.getOscHost().sendMessage(oscPath.getClipIsPlaying(trackIndex, slotIndex), isPlaying);
    });

    oscController.addBooleanValue(ID, oscPath.getClipIsRecording(trackIndex, slotIndex), slot.isRecording());
    oscController.addBooleanValue(ID, oscPath.getClipIsPlaybackQueued(trackIndex, slotIndex), slot.isPlaybackQueued());
    oscController.addBooleanValue(ID, oscPath.getClipIsStopQueued(trackIndex, slotIndex), slot.isStopQueued());
    oscController.addBooleanValue(ID, oscPath.getClipIsRecordingQueued(trackIndex, slotIndex), slot.isRecordingQueued());
  }

  private void setupSlotMisc(int trackIndex, int slotIndex, ClipLauncherSlot slot) {
    //Duplicate slot
    oscController.registerOscNoParameterCallback(oscPath.getClipDuplicate(trackIndex, slotIndex), "Duplicate the clip at " + trackIndex + "Slot " + slotIndex, slot::duplicateClip);

    //Create Empty Clip
    oscController.registerOscIntegerCallback(oscPath.getClipCreateEmpty(trackIndex, slotIndex), "Create Empty Clip at " + trackIndex + "Slot " + slotIndex, slot::createEmptyClip);
  }

  private void setupSlotLaunch(int trackIndex, int slotIndex, ClipLauncherSlot slot) {
    oscController.registerOscNoParameterCallback(oscPath.getClipLaunch(trackIndex, slotIndex), "Launches Clip on Track" + trackIndex + "Slot " + slotIndex, slot::launch);
    oscController.registerOscNoParameterCallback(oscPath.getClipLaunchRelease(trackIndex, slotIndex), "Launch Release Clip " + trackIndex + "Slot " + slotIndex, slot::launchRelease);
    oscController.registerOscNoParameterCallback(oscPath.getClipLaunchAlt(trackIndex, slotIndex), "Launches Alt Clip " + trackIndex + "Slot " + slotIndex, slot::launchAlt);
    oscController.registerOscNoParameterCallback(oscPath.getClipLaunchAltRelease(trackIndex, slotIndex),"Launch Alt Release Clip " + trackIndex + "Slot " + slotIndex, slot::launchReleaseAlt);

    String launchWithOptionsDescription = "Launches with options: Params: quantization – possible values are \"default\", \"none\", \"8\", \"4\", \"2\", \"1\", \"1/2\", \"1/4\", \"1/8\", \"1/16\" launchMode – possible values are: \"default\", \"from_start\", \"continue_or_from_start\", \"continue_or_synced\", \"synced\"";

    OscMethodCallback launchWithOptionsCallback = (connection, message) -> {
      String quantization = message.getString(0);
      String launchMode = message.getString(1);
      slot.launchWithOptions(quantization, launchMode);
    };

    oscController.registerOscCallback(oscPath.getClipLaunchWithOptions(trackIndex, slotIndex),",s,s", launchWithOptionsDescription, launchWithOptionsCallback );

    String launchLastWithOptionsDescription = "Launches Last with options: Params: quantization – possible values are \"default\", \"none\", \"8\", \"4\", \"2\", \"1\", \"1/2\", \"1/4\", \"1/8\", \"1/16\" launchMode – possible values are: \"default\", \"from_start\", \"continue_or_from_start\", \"continue_or_synced\", \"synced\"";

    OscMethodCallback launchLastWithOptionsCallback = (connection, message) -> {
      String quantization = message.getString(0);
      String launchMode = message.getString(1);
      slot.launchLastClipWithOptionsAction(quantization, launchMode);
    };

    oscController.registerOscCallback(oscPath.getClipLaunchLastWithOptions(trackIndex, slotIndex),",s,s",  launchLastWithOptionsDescription, launchLastWithOptionsCallback );
  }

  //setup clipLauncherSlotBank
  private void setupClipLauncherSlotBank(ClipLauncherSlotBank clipLauncherSlotBank, int trackIndex) {
    oscController.addSettableIntegerValue(ID, oscPath.getScrollPosition(trackIndex), clipLauncherSlotBank.scrollPosition(), "get / set scroll position of bank");
  }

  private void setupClipLaunchStops(int trackIndex, ClipLauncherSlotBank clipLauncherSlotBank, Track track) {
    oscController.addBooleanValue(ID, oscPath.getIsStopQueued(trackIndex), track.isQueuedForStop());
    oscController.addBooleanValue(ID, oscPath.getIsStopped(trackIndex), track.isStopped());

    //Register Stop Callback for track
    oscController.registerOscNoParameterCallback(oscPath.getTrackStop(trackIndex), "Stop Track " + trackIndex, clipLauncherSlotBank::stop);
  }

  @Override
  public void selectScene(Integer integer) {
    scrollPosition(integer);
  }

  @Override
  public void refresh() {
    oscController.forceNextFlush(ID);
  }

  @Override
  public void enable(boolean enabled) {
    oscController.setGroupEnabled(ID, enabled);
  }
}