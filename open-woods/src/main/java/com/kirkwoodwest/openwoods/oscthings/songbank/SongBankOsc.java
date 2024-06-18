package com.kirkwoodwest.openwoods.oscthings.songbank;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.bitwig.extension.controller.api.ClipLauncherSlotBank;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.openwoods.flush.Flushables;
import com.kirkwoodwest.openwoods.flush.ForceFlushQueue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.oscthings.adapters.BooleanCollectionSavedOscAdapter;
import com.kirkwoodwest.openwoods.savelist.BooleanCollectionSaved;

/**
 * Song Bank
 *
 * Tool to manage the song bank as scenes. Uses a cursor track to target the scene bank which reports the slots.
 * Selecting these slots will move the assigned clip launcher to that scene.
 * If a scene is playing, it will be marked as played.
 *
 */
public class SongBankOsc {
  public final String ID;
  private final OscController oscController;
  private final SongBankOscClipLauncher oscClipLauncher;
  private final BooleanCollectionSaved songItemPlayed;
  private String trackName;
  private ControllerHost host;

  public SongBankOsc(ControllerHost host, OscController oscController, SongBankOscClipLauncher oscClipLauncher, CursorTrack cursorTrack, String oscPath) {
    this.host = host;
    this.oscController = oscController;
    this.ID = "SONGBANK" + oscPath;
    this.oscClipLauncher = oscClipLauncher;

    host.createApplication().projectName().addValueObserver(projectName -> {
      //Load new data
      trackName = projectName;
    });

    //Get Clip Launcher Bank
    ClipLauncherSlotBank clipLauncherSlotBank = cursorTrack.clipLauncherSlotBank();
    int sizeOfBank = clipLauncherSlotBank.getSizeOfBank();

    //Song Item played Data
    songItemPlayed = new BooleanCollectionSaved(host,oscPath, sizeOfBank);
    Flushables.getInstance().add(songItemPlayed);

    //Osc Adapter for Item Played Data
    BooleanCollectionSavedOscAdapter songItemPlayedAdapter = new BooleanCollectionSavedOscAdapter(oscController, oscPath + "/played", songItemPlayed);
    oscController.addAdapter(ID, songItemPlayedAdapter);

    for (int i = 0; i < sizeOfBank; i++) {
      ClipLauncherSlot clipLauncherSlot = clipLauncherSlotBank.getItemAt(i);
      final int index = i + 1;
      SceneSongOscAdapter sceneSongOscAdapter = new SceneSongOscAdapter(clipLauncherSlot, index, oscController, oscPath, "Song Bank " + index);
      oscController.addAdapter(ID, sceneSongOscAdapter);
      clipLauncherSlot.isPlaying().addValueObserver(isPlaying -> {
        if (isPlaying) {
          songItemPlayed.setValue(index, true);
        }
      });
    }

    //Select Song
    oscController.registerOscIntegerCallback(oscPath + "/select", "Select Song", this::selectSong);

    //Resets the song bank played
    oscController.registerOscCallback(oscPath + "/reset", "Reset Song Playback Status", (oscConnection, oscMessage) -> {
      this.reset();
    });


    //Request Flush
    oscController.registerOscCallback(oscPath + "/refresh", "Request Song Bank Flush", this::refresh);
  }

  private void reset() {
    int size = songItemPlayed.size();
    for (int i = 0; i < size; i++) {
      songItemPlayed.setValue(i, false);
    }
    host.requestFlush();
  }

  private void refresh(OscConnection oscConnection, OscMessage oscMessage) {
    oscController.forceNextFlush(ID);
  }

  private void selectSong(Integer integer) {
    oscClipLauncher.selectScene(integer - 1);
  }
}
