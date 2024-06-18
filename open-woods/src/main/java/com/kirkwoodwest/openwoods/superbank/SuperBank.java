package com.kirkwoodwest.openwoods.superbank;

import com.bitwig.extension.controller.api.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuperBank {
  private final HashMap<CursorTrack, ArrayList<BooleanValue>> cursorTrackEqualsValues;
  private final ArrayList<SuperBankDevices> track_devices;
  private final HashMap<Track, SuperBankClipLauncherSlots> superBankClipLauncherSlots = new HashMap<>();
  TrackBank trackBank;


  private ControllerHost host;

  public SuperBank(ControllerHost host, int numTracks, int numSends, int numScenes) {
    this.host = host;
    track_devices = new ArrayList<SuperBankDevices>();

    trackBank = host.createTrackBank(numTracks, numSends, numScenes, true);
    markBankInterested();


    if (numScenes > 0) {
      int sizeOfBank = trackBank.getSizeOfBank();
      for (int i = 0; i < sizeOfBank; i++) {
        final Track track = trackBank.getItemAt(i);
        final SuperBankClipLauncherSlots clipLauncherSlots = new SuperBankClipLauncherSlots(track, numScenes);
        this.superBankClipLauncherSlots.put(track, clipLauncherSlots);
      }
    }

    cursorTrackEqualsValues = new HashMap<CursorTrack, ArrayList<BooleanValue>>();
  }

  /**
   * Enables foldable settings in the document
   */
  public void settingsFoldable(){
    Signal fold = host.getDocumentState().getSignalSetting("Collapse", "Super Bank", "Collapse Super Bank");
    fold.addSignalObserver(() -> expandTracks(false));
    Signal unfold = host.getDocumentState().getSignalSetting("Expand", "Super Bank", "Expand Super Bank");
    unfold.addSignalObserver(() -> expandTracks(true));
  }

  /**
   * Marks all values in the bank as interested.
   */
  public void markBankInterested(){
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track track = trackBank.getItemAt(i);
      track.arm().markInterested();
      track.name().markInterested();
      track.color().markInterested();
      track.isActivated().markInterested();
      track.isGroup().markInterested();
      track.trackType().markInterested();
      track.isGroupExpanded().markInterested();
    }
  }

  /**
   * Marks all values in the bank as interested.
   */
  public void markBankInterested(boolean arm, boolean name, boolean color, boolean isActivated, boolean isGroup, boolean trackType, boolean isGroupExpanded){
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track track = trackBank.getItemAt(i);
      if (arm) track.arm().markInterested();
      if (name) track.name().markInterested();
      if (color) track.color().markInterested();
      if (isActivated) track.isActivated().markInterested();
      if (isGroup) track.isGroup().markInterested();
      if (trackType) track.trackType().markInterested();
      if (isGroupExpanded) track.isGroupExpanded().markInterested();
    }
  }

  /**
   * Creates a list of boolean values that are true when the track is the same as the cursor track.
   * */
  public void createEqualsValues(CursorTrack cursor_track) {

    if(cursorTrackEqualsValues.containsKey(cursor_track)) return;

    ArrayList<BooleanValue> equalsValues = new ArrayList<>();
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track track = trackBank.getItemAt(i);

      BooleanValue equalsValue = track.createEqualsValue(cursor_track);
      equalsValue.markInterested();
      equalsValues.add(equalsValue);
    }

    cursorTrackEqualsValues.put(cursor_track, equalsValues);
  }

  public TrackBank get() {
    return trackBank;
  }

  public List<String> getIndexedTrackNames() {
    List<String> trackNames = new ArrayList<>();
    boolean masterHit = false;
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track  track = trackBank.getItemAt(i);
      String name  = track.name().get();
      if(name == "Master") {
        masterHit = true;
      }
      if(masterHit && name.isEmpty()) {
        break;
      }
      trackNames.add(name);
    }
    return trackNames;
  };

  /**
   * Gets the index of the cursor track in the bank using the equals values.
   * @param cursorTrack
   * @return
   */
  public int getCursorIndex(CursorTrack cursorTrack) {
    ArrayList<BooleanValue> equalsValues = cursorTrackEqualsValues.get(cursorTrack);

    if(equalsValues==null) return -1;
    int size = equalsValues.size();
    for (int i = 0; i < size; i++) {
      boolean is_equals = equalsValues.get(i).get();
      if (is_equals) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Gets the track at the given index.
   * */
  public Track getItemAt(int i) {
    return trackBank.getItemAt(i);
  }

  /**
   * Sets expansion of all groups in bank until they are all set to target.
   * */
  public void expandTracks(boolean expanded) {
    List<Track> tracks = new ArrayList<>();
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track track = trackBank.getItemAt(i);
      if (trackBank.getItemAt(i).isGroup().get()) {
        if(  track.isGroupExpanded().get() !=  expanded) {
          track.isGroupExpanded().set(expanded);
          host.scheduleTask(()->expandTracks(expanded), 50);
          return;
        }
      }
    }
  }

  public Track findTrackByName(String name){
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track track = trackBank.getItemAt(i);
      if (track.name().get().equals(name)) {
        return track;
      }
    }
    return null;
  }

  public Track setCursorTrackByName(CursorTrack cursorTrack, String name){
    Track track = findTrackByName(name);
    if(track != null) {
      cursorTrack.isPinned().set(false);
      cursorTrack.selectChannel(track);
      cursorTrack.isPinned().set(true);
    }
    return track;
  }

  public void setClipByName(CursorTrack cursorTrack, String cursorTrackName, PinnableCursorClip pinnableCursorClip, String clipName){
    if (superBankClipLauncherSlots.size() == 0) return;
    Track track =  setCursorTrackByName(cursorTrack, cursorTrackName);
    if(track != null){
      SuperBankClipLauncherSlots clipLauncherSlots = superBankClipLauncherSlots.get(track);
      if(clipLauncherSlots != null){

        ClipLauncherSlot clipLauncherSlot = clipLauncherSlots.findByClipName(clipName);
        if(clipLauncherSlot != null) {
          //delay
          cursorTrack.selectInEditor();
          pinnableCursorClip.isPinned().set(false);
          host.scheduleTask(()->{

            clipLauncherSlot.select();
            pinnableCursorClip.isPinned().set(true);
          }, 100);
        }

      }
    }
  }
}
