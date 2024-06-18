package com.kirkwoodwest.openwoods.trackbank;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.settings.ShowScriptConsole;
import com.kirkwoodwest.utils.StringUtil;

import java.util.stream.IntStream;

/**
 * A basic implementation of trackbank which allows you to get visible tracks by name or index.
 * Useful for getting a cursor track to pin to a specific track.
 *
 * The point of this is to keep things simple to allow other bitwig objects to align with the current view.
 * Since this item will always be locked to scroll position 0, this will always be in sync with current view.
 */
public class CursorTrackHelper implements Helper {
  private final TrackBank trackBank;
  private final ControllerHost host;
  private final Action actionShowScriptConsole;

  public CursorTrackHelper(ControllerHost host, int numTracks){
    this.host = host;
    trackBank = host.createTrackBank(numTracks, 0, 0);
    trackBank.scrollPosition().markInterested();
    IntStream.range(0, trackBank.getSizeOfBank())
            .forEach(trackIndex -> {
              trackBank.getItemAt(trackIndex).name().markInterested();
              trackBank.getItemAt(trackIndex).exists().markInterested();
            });
//    //Always force to 0
//    trackBank.scrollPosition().addValueObserver(scrollPosition -> {
//      if(scrollPosition != 0){
//        trackBank.scrollPosition().set(0);
//      }
//    });
//
//    Signal signalSetting = host.getDocumentState().getSignalSetting("Debug Cursor Tracks", "Tools", "Debug Cursor Tracks");
//    signalSetting.addSignalObserver(this::debugBank);
    actionShowScriptConsole = ShowScriptConsole.createAction(host);

  }

  public Track getTrack(int index){
    if(index < 0 || index >= trackBank.getSizeOfBank()){
      return null;
    }
    return trackBank.getItemAt(index);
  }

  public Track getTrackByName(String name){
    return IntStream.range(0, trackBank.getSizeOfBank())
            .mapToObj(trackBank::getItemAt)
            .filter(trackItem -> trackItem.name().get().equals(name))
            .findFirst().orElse(null);
  }

  public int getIndexByName(String name){
    return IntStream.range(0, trackBank.getSizeOfBank())
            .filter(trackIndex -> trackBank.getItemAt(trackIndex).name().get().equals(name))
            .findFirst().orElse(-1);
  }

  public int getSizeOfBank() {
    return this.trackBank.getSizeOfBank();
  }

  public void debugBank(){
    host.println("---- Debug Cursor Tracks");
    host.println("Bank Size: " + trackBank.getSizeOfBank());
    host.println("Scroll Position: " + trackBank.scrollPosition().get());
    host.println("-----");
    actionShowScriptConsole.invoke();
    IntStream.range(0, trackBank.getSizeOfBank())
            .forEach(trackIndex -> {
              Track track = trackBank.getItemAt(trackIndex);
              if(!track.name().get().isEmpty()) {
                host.println("" + StringUtil.padIntRight(3,trackIndex) + " : " + track.name().get());
              }
            });
    host.println("-----");
  }



}
