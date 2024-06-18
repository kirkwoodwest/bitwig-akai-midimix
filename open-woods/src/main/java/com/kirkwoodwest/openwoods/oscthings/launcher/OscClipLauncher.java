package com.kirkwoodwest.openwoods.oscthings.launcher;

import com.bitwig.extension.api.opensoundcontrol.OscMethodCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;
import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.stream.IntStream;

public class OscClipLauncher {

  public static final String ID = "ClipLauncher";

  private final OscController oscController;
    private final OscClipLauncherPaths oscPath;

  public OscClipLauncher(ControllerHost host, CursorTrackBank cursorTrackBank, OscController oscController, String oscPath) {
    this.oscPath = new OscClipLauncherPaths(oscPath);
    this.oscController = oscController;

    int numScenes = cursorTrackBank.getItemAt(0).clipLauncherSlotBank().getSizeOfBank();
    setupSceneBank(host, numScenes);
    setupCursorTrackBank(cursorTrackBank, numScenes);

    oscController.addIndexedPathInfo(this.oscPath.getTrackIndex(), "Track Index 1 - " +  cursorTrackBank.getSizeOfBank());
    oscController.addIndexedPathInfo(this.oscPath.getClipIndex(), "Clip Index 1 - " +  numScenes);
  }

  private void setupSceneBank(ControllerHost host, int numScenes) {
    SceneBank sceneBank = host.createSceneBank(numScenes);

    IntStream.range(0,sceneBank.getSizeOfBank())
            .forEach(i-> {
              Scene scene = sceneBank.getScene(i);
              oscController.addStringValue(ID, oscPath.getSceneName(i), scene.name());
              oscController.addSettableColorValue(ID, oscPath.getSceneColor(i), scene.color(), "Sets scolor for Scene");
              oscController.registerOscNoParameterCallback(oscPath.getSceneLaunch(i), "Launches Scene " + i, scene::launch);
              oscController.registerOscNoParameterCallback(oscPath.getSceneLaunchRelease(i), "Launches Scene " + i, scene::launchRelease);
              oscController.registerOscNoParameterCallback(oscPath.getSceneLaunchAlt(i), "Launches Scene " + i, scene::launchAlt);
              oscController.registerOscNoParameterCallback(oscPath.getSceneLaunchAltRelease(i), "Launches Scene " + i, scene::launchReleaseAlt);
            });
  }

  private void setupCursorTrackBank(CursorTrackBank cursorTrackBank, int numScenes) {
    IntStream.range(0, cursorTrackBank.getSizeOfBank())
            .forEach(trackIndex->{
              Track track = cursorTrackBank.getItemAt(trackIndex);

              ClipLauncherSlotBank clipLauncherSlotBank = track.clipLauncherSlotBank();
              //Setup Stops
              setupClipLaunchStops(trackIndex, clipLauncherSlotBank, track);

              //Clip level
              IntStream.range(0,numScenes)
                      .forEach(slotIndex->{
                        setupSlot(trackIndex, slotIndex, clipLauncherSlotBank);
                      });
            });
  }

  private void setupClipLaunchStops(int trackIndex, ClipLauncherSlotBank clipLauncherSlotBank, Track track) {

    oscController.addBooleanValue(ID, oscPath.getIsStopQueued(trackIndex), track.isQueuedForStop());
    oscController.addBooleanValue(ID, oscPath.getIsStopped(trackIndex), track.isStopped());

    //Register Stop Callback for track
    oscController.registerOscNoParameterCallback(oscPath.getTrackStop(trackIndex), "Stop Track " + trackIndex, clipLauncherSlotBank::stop);
  }

  private void setupSlot(int trackIndex, int slotIndex, ClipLauncherSlotBank clipLauncherSlotBank) {
    ClipLauncherSlot slot = clipLauncherSlotBank.getItemAt(slotIndex);
    setupSlotStatus(trackIndex, slotIndex, slot);
    setupSlotLaunch(trackIndex, slotIndex, slot);
    setupSlotMisc(trackIndex, slotIndex, slot);
  }

  private void setupSlotStatus(int trackIndex, int slotIndex, ClipLauncherSlot slot) {
    int oscSlotIndex = slotIndex + 1;
    int oscTrackIndex = trackIndex + 1;
    oscController.addStringValue(ID, oscPath.getClipName(oscTrackIndex, oscSlotIndex), slot.name());
    oscController.addSettableColorValue(ID, oscPath.getClipColor(oscTrackIndex, oscSlotIndex), slot.color(), "Clip Color");
    oscController.addBooleanValue(ID, oscPath.getClipExists(oscTrackIndex, oscSlotIndex), slot.exists());
    oscController.addBooleanValue(ID, oscPath.getClipHasContent(oscTrackIndex, oscSlotIndex), slot.hasContent());
    oscController.addBooleanValue(ID, oscPath.getClipIsPlaying(oscTrackIndex, oscSlotIndex), slot.isPlaying());
    oscController.addBooleanValue(ID, oscPath.getClipIsRecording(oscTrackIndex, oscSlotIndex), slot.isRecording());
    oscController.addBooleanValue(ID, oscPath.getClipIsPlaybackQueued(oscTrackIndex, oscSlotIndex), slot.isPlaybackQueued());
    oscController.addBooleanValue(ID, oscPath.getClipIsStopQueued(oscTrackIndex, oscSlotIndex), slot.isStopQueued());
    oscController.addBooleanValue(ID, oscPath.getClipIsRecordingQueued(oscTrackIndex, oscSlotIndex), slot.isRecordingQueued());
  }

  private void setupSlotMisc(int trackIndex, int slotIndex, ClipLauncherSlot slot) {
    int oscSlotIndex = slotIndex + 1;
    int oscTrackIndex = trackIndex + 1;
    //Duplicate slot
    oscController.registerOscNoParameterCallback(oscPath.getClipDuplicate(trackIndex, slotIndex), "Duplicate the clip at " + trackIndex + "Slot " + slotIndex, slot::duplicateClip);

    //Create Empty Clip
    oscController.registerOscIntegerCallback(oscPath.getClipCreateEmpty(trackIndex, slotIndex), "Create Empty Clip at " + trackIndex + "Slot " + slotIndex, slot::createEmptyClip);
  }

  private void setupSlotLaunch(int trackIndex, int slotIndex, ClipLauncherSlot slot) {
    int oscSlotIndex = slotIndex + 1;
    int oscTrackIndex = trackIndex + 1;
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

    oscController.registerOscCallback(oscPath.getClipLaunchWithOptions(trackIndex, slotIndex), launchWithOptionsDescription, launchWithOptionsCallback );


    String launchLastWithOptionsDescription = "Launches Last with options: Params: quantization – possible values are \"default\", \"none\", \"8\", \"4\", \"2\", \"1\", \"1/2\", \"1/4\", \"1/8\", \"1/16\" launchMode – possible values are: \"default\", \"from_start\", \"continue_or_from_start\", \"continue_or_synced\", \"synced\"";

    OscMethodCallback launchLastWithOptionsCallback = (connection, message) -> {
      String quantization = message.getString(0);
      String launchMode = message.getString(1);
      slot.launchLastClipWithOptionsAction(quantization, launchMode);
    };

    oscController.registerOscCallback(oscPath.getClipLaunchLastWithOptions(trackIndex, slotIndex), launchLastWithOptionsDescription, launchLastWithOptionsCallback );
  }
}