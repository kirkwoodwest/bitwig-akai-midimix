package com.kirkwoodwest.openwoods.trackbank;


import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.settings.SettingsHelper;
import com.kirkwoodwest.openwoods.settings.ShowScriptConsole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.kirkwoodwest.utils.StringUtil.padInt;

//Long Live the cursor track. This is my most favorite class of all time. perfect for retaining permanent focus on a track.
//This is a helper class to create multiple cursor tracks and position them by using a reference name and index.
//This index is limited to the size of the CursorTrackHelper size of Bank.
//Simulates a track bank for multiple cursor tracks, this allows you to create a collection of tracks. These can be positioned freely or together
public class CursorTrackBank {

  private final ArrayList<CursorTrack> cursorTracks = new ArrayList<>();
  private final ControllerHost host;
  private final CursorTrackHelper cursorTrackHelper;
  private final String name;
  private String settingsName = "Cursor Track Bank";


  private SettableStringValue settingTargetName;

  private Signal settingTargetSet;
  private Action actionOpenControllerConsole;

  public enum INDICATOR_STYLE {
    PAN,
    VOLUME,
    NONE,
  }

  public CursorTrackBank(ControllerHost host, ArrayList<CursorTrack> cursorTracks, CursorTrackHelper cursorTrackHelper, String name){
    this.host = host;
    this.cursorTrackHelper = cursorTrackHelper;
    this.name = name;

    IntStream.range(0, cursorTracks.size())
            .forEach(i -> {
              CursorTrack cursorTrack = cursorTracks.get(i);
              setupCursorTrack(cursorTrack, i);
            });
    generateSettingsControls(host, name);
  }

  public CursorTrackBank(ControllerHost host, CursorTrackHelper cursorTrackHelper, int trackCount, int numSends, int numScenes, String name){
    this.host = host;
    this.cursorTrackHelper = cursorTrackHelper;
    this.name = name;

    for (int i = 0; i < trackCount; i++) {
      CursorTrack cursorTrack = host.createCursorTrack(name + i, name + i, numSends, numScenes, true);
      setupCursorTrack(cursorTrack, i);
    }
    generateSettingsControls(host, name);
  }

  private void setupCursorTrack(CursorTrack cursorTrack, int index){
//    SendBank cursorTrackSendBank = cursorTrack.sendBank();
//    IntStream.range(0, cursorTrackSendBank.getSizeOfBank())
//            .forEach(sendIndex -> {
//              cursorTrackSendBank.getItemAt(sendIndex).setIndication(true);
//            });

    cursorTrack.isPinned().markInterested();
    cursorTrack.exists().markInterested();
    cursorTrack.name().markInterested();
    cursorTrack.position().markInterested();

    cursorTracks.add(cursorTrack);
  }

  public void setIndicatorStyle(INDICATOR_STYLE style){
    switch (style){
      case PAN:
        cursorTracks.forEach(cursorTrack -> {
          cursorTrack.pan().setIndication(true);
          cursorTrack.volume().setIndication(false);
        });
        break;
      case VOLUME:
        cursorTracks.forEach(cursorTrack -> {
          cursorTrack.pan().setIndication(false);
          cursorTrack.volume().setIndication(true);
        });
        break;
      case NONE:
        cursorTracks.forEach(cursorTrack -> {
          cursorTrack.pan().setIndication(false);
          cursorTrack.volume().setIndication(false);
        });
        break;
    }
  }

  private void generateSettingsControls(ControllerHost host, String settingsName){
    //TODO: make a settings builder with those two models to hide and show the values.
    // consolodate the Ctrack1-4 to a list and make that the size of the track count.
    // Build OSC Version to select tracks. It should publish a list of tracks from the track bank on query...
    // then the user can choose either index or name to select the track.
    //Build Settings
    this.settingsName = settingsName;
    DocumentState documentState = host.getDocumentState();

    actionOpenControllerConsole = ShowScriptConsole.createAction(host);
    settingTargetName = documentState.getStringSetting(settingsName + " [Cursors Selection]", "Cursor Track Bank", 24, "");
    settingTargetName.markInterested();
    settingTargetSet = documentState.getSignalSetting(settingsName + " [Set Cursors]", "Cursor Track Bank", "Set");
    settingTargetSet.addSignalObserver(()->{
      set();
    });
    Signal settingDebug = documentState.getSignalSetting((settingsName + " [Debug]"), "Cursor Track Bank", "Debug");
    settingDebug.addSignalObserver(()->{
      debugBank();
    });
    SettingsHelper.setVisible((Setting) settingTargetName, false);
    SettingsHelper.setVisible((Setting) settingTargetSet, false);
    SettingsHelper.setVisible((Setting) settingDebug, false);
  }



  public int getSizeOfBank(){
    return cursorTracks.size();
  }

  public int getNumItems(){
    //Since all items are consider valid, this is the same as size of bank.
    return cursorTracks.size();
  }

  public CursorTrack getItemAt(int index){
    return cursorTracks.get(index);
  }

  //Come up with better names you suck.
  public void positionAllByIndexes(List<Integer> indexes){
    if(indexes.size() != cursorTracks.size()){
      throw new IllegalArgumentException("Indexes must be the same size as the cursor track bank");
    }
    IntStream.range(0, cursorTracks.size())
            .forEach(index -> {
              CursorTrack cursorTrack = cursorTracks.get(index);

              positionCursorByTrackIndex(index, indexes.get(index));
            });
  }


  public void positionAllByFirstTrackIndex(int trackIndex){
    IntStream.range(0, cursorTracks.size())
            .forEach(index -> {
              CursorTrack cursorTrack = cursorTracks.get(index);
              positionCursorByTrackIndex(index, trackIndex + index);
            });
  }

  public void positionSplitByFirstTrackIndex(int trackIndex){
    int splitIndex = (cursorTracks.size() + 1) / 2;
    ArrayList<CursorTrack> firstHalf = new ArrayList<>(cursorTracks.subList(0, splitIndex)),
            secondHalf = new ArrayList<>(cursorTracks.subList(splitIndex, cursorTracks.size()));
    positionCursorTracksByTrackIndex(firstHalf, trackIndex);
    positionCursorTracksByTrackIndex(secondHalf, trackIndex);
  }

  private void positionCursorTracksByTrackIndex(ArrayList<CursorTrack> cursorTracks, int trackIndex) {
    IntStream.range(0, cursorTracks.size())
            .forEach(index -> {
              CursorTrack cursorTrack = cursorTracks.get(index);
              positionCursorByTrackIndex(index, trackIndex + index);
            });
  }

  public void positionCursorByTrackIndex(int cursorIndex, int trackIndex){
    CursorTrack cursorTrack = cursorTracks.get(cursorIndex);
    Track track = cursorTrackHelper.getTrack(trackIndex);
    if(track != null) {
      cursorTrack.isPinned().set(false);
      cursorTrack.selectChannel(track);
      cursorTrack.isPinned().set(true);
    }
  }

  public void positionAllByTrackName(String trackName){
    positionAllByFirstTrackIndex(cursorTrackHelper.getIndexByName(trackName));
  }


  public void positionSplitByTrackName(String trackName){

    int splitIndex = (cursorTracks.size() + 1) / 2;
    ArrayList<CursorTrack> firstHalf = new ArrayList<>(cursorTracks.subList(0, splitIndex)),
            secondHalf = new ArrayList<>(cursorTracks.subList(splitIndex, cursorTracks.size()));
    positionCursorTracksByTrackIndex(firstHalf, cursorTrackHelper.getIndexByName(trackName));
    positionCursorTracksByTrackIndex(secondHalf, cursorTrackHelper.getIndexByName(trackName));

  }

  public void positionCursorByTrackName(int cursorIndex, String trackName){
    CursorTrack cursorTrack = cursorTracks.get(cursorIndex);
    Track track = cursorTrackHelper.getTrackByName(trackName);
    if(track != null) {
      cursorTrack.isPinned().set(false);
      cursorTrack.selectChannel(track);
      cursorTrack.isPinned().set(true);
    }
  }


  private void positionCursorTracksByTrackName(ArrayList<CursorTrack> cursorTracks, String trackName) {
    positionCursorTracksByTrackIndex(cursorTracks, cursorTrackHelper.getIndexByName(trackName));
  }

  public ArrayList<CursorTrack> getCursors() {
    return cursorTracks;
  }

  public void debugBank(){
    host.println("---- " + name + " debug ");
    host.println("Bank Size: " + cursorTracks.size());
    host.println("-----");
    IntStream.range(0,cursorTracks.size()).forEach(i -> {
      CursorTrack cursorTrack = cursorTracks.get(i);
      String name = cursorTrack.name().get();
      if(!name.isEmpty()) {
        host.println("" + padInt(3,i) + " : " + cursorTrack.name().get());
      }

    });
    host.println("-----");
  }

  public void setTrackTargets(String target){
    settingTargetName.set(target);
    set();
  }

  public void set(){
    String s = settingTargetName.get();
    if(s.isEmpty()) return;
    try {
      List<Integer> indexes = IndexParser.parseIndexes(s, cursorTrackHelper, this.getSizeOfBank(), cursorTrackHelper.getSizeOfBank() );
      if(indexes.get(0).equals(-1)) {
        host.showPopupNotification("Invalid String Selection");
      } else if (indexes.size() != cursorTracks.size()) {
        host.showPopupNotification("missed matched selection: You provided " + indexes.size() + " tracks, but there are " + cursorTracks.size() + " tracks. Open Controller Script Console For More Info");
        //print indexes user provided

        host.println("-----------------------");
        host.println(settingsName);
        host.println("String Provided: " + s);
        host.println("Number of Cursor Tracks: " + indexes.size());
        host.println("Indexes Provided: " + indexes);
      } else {
        positionAllByIndexes(indexes);
        host.showPopupNotification("Positions Updated:" + indexes);
      }
    } catch (Exception e){
      host.showPopupNotification(e.getMessage());
      host.println("-----------------------");
      host.println("OPEN WOODS HELP");
      host.println("Your selection string was: " + s);
      host.println("Something is wrong with it. Refer to the help below.");
      host.println(IndexParser.getHelpString(settingsName));
    }
  }

}
