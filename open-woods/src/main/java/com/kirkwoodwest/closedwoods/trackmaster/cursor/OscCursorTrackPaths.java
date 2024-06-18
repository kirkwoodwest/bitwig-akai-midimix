package com.kirkwoodwest.closedwoods.trackmaster.cursor;

public class OscCursorTrackPaths {

  public final String PARENT_TRACK_EXISTS;
  public final String PARENT_TRACK_NAME;
  public final String DEVICE;
  private final String path;
  public final String NAME;
  public final String VOLUME;
  public final String PAN;
  public final String MUTE;
  public final String SOLO;
  public final String ARM;
  public final String ACTIVATED;
  public final String TYPE;
  public final String NOTE_PLAYED;
  public final String DEVICE_NAME;
  public final String PLAYING_NOTES_ENABLED;
  public final String PIN_DEVICE;
  public final String PIN_CURSOR;


  public OscCursorTrackPaths(String path) {
    this.path = path;
    NAME = path + "/name";
    VOLUME = path + "/volume";
    PAN = path + "/pan";
    MUTE = path + "/mute";
    SOLO = path + "/solo";
    ARM = path + "/arm";
    ACTIVATED = path + "/activated";
    TYPE = path + "/type";
    NOTE_PLAYED = path + "/note_played";
    DEVICE = path + "/device";
    DEVICE_NAME = path + "/device/name";
    PLAYING_NOTES_ENABLED = path + "/playing_notes_enabled";
    PIN_DEVICE = path + "/pin_device";
    PIN_CURSOR = path + "/pin_cursor";
    PARENT_TRACK_EXISTS = path + "/parent_track_exists";
    PARENT_TRACK_NAME = path + "/parent_track_name";
  }

  public String getRemotePageName(int index){
    return this.path + "/device/remote/" + (index+1) + "/name";
  }
}
