package com.kirkwoodwest.openwoods.cursortrack;

public class OscCursorTrackPaths {

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
  public final String CHAIN_EXISTS;
  public final String CHAIN_COUNT;
  public final String SET_CHAIN_INDEX;

  public String REMOTE;
  public String LAYER_BANK_EXISTS;
  public String PARENT_TRACK_EXISTS;
  public String PARENT_TRACK_NAME;

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
    REMOTE = path + "/remote";
    CHAIN_EXISTS = path + "/chain_exists";
    CHAIN_COUNT = path + "/chain_count";
    LAYER_BANK_EXISTS = path + "/layer_exists";
    SET_CHAIN_INDEX = path + "/chain_index";
    PARENT_TRACK_EXISTS = path + "/parent_track_exists";
    PARENT_TRACK_NAME = path + "/parent_track_name";
  }

  public String getRemotePageName(int index){
    return this.path + "/device/remote/" + (index+1) + "/name";
  }

  public String getParameter(int pageIndex, int parameterIndex) {
    return REMOTE + "/" +getParameterIndex(pageIndex, parameterIndex);
  }
  public String getParameterIndex(int pageIndex, int parameterIndex) {
    return (pageIndex + 1) + "/" + (parameterIndex + 1);
  }

  public String getRemotePage(){
    return REMOTE + "/*";
  }

  public String getRemoteParameter(){
    return REMOTE + "/1/*";
  }

  public String getLayerBankIndex(int index) {
    return REMOTE + "/layer/" + (index + 1);
  }

  public String getLayerBankName(int index) {
    return getLayerBankIndex(index) + "/name";
  }

}
