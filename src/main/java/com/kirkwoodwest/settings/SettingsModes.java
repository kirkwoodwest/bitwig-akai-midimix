package com.kirkwoodwest.settings;

import java.util.*;

public class SettingsModes {

  // Knob modes

  public static final String KNOB_DISABLED = "Disabled";
  public static final String KNOB_VOLUME = "Track Volume";
  public static final String KNOB_PAN = "Track Pan";
  public static final String KNOB_SEND1 = "Track Send 1";
  public static final String KNOB_SEND2 = "Track Send 2";
  public static final String KNOB_SEND3 = "Track Send 3";
  public static final String TRACK_REMOTE_1 = "Track Remote 1";
  public static final String TRACK_REMOTE_2 = "Track Remote 2";
  public static final String TRACK_REMOTE_3 = "Track Remote 3";
  public static final String TRACK_REMOTE_4 = "Track Remote 4";
  public static final String TRACK_REMOTE_5 = "Track Remote 5";
  public static final String TRACK_REMOTE_6 = "Track Remote 6";
  public static final String TRACK_REMOTE_7 = "Track Remote 7";
  public static final String TRACK_REMOTE_8 = "Track Remote 8";
  public static final String PROJECT_REMOTE_1 = "Project Remote 1/1";
  public static final String PROJECT_REMOTE_2 = "Project Remote 1/2";
  public static final String PROJECT_REMOTE_3 = "Project Remote 1/3";
  public static final String PROJECT_REMOTE_4 = "Project Remote 1/4";
  public static final String PROJECT_REMOTE_5 = "Project Remote 1/5";
  public static final String PROJECT_REMOTE_6 = "Project Remote 1/6";
  public static final String PROJECT_REMOTE_7 = "Project Remote 1/7";
  public static final String PROJECT_REMOTE_8 = "Project Remote 1/8";
  public static final String PROJECT_REMOTE_9 = "Project Remote 2/1";
  public static final String PROJECT_REMOTE_10 = "Project Remote 2/2";
  public static final String PROJECT_REMOTE_11 = "Project Remote 2/3";
  public static final String PROJECT_REMOTE_12 = "Project Remote 2/4";
  public static final String PROJECT_REMOTE_13 = "Project Remote 2/5";
  public static final String PROJECT_REMOTE_14 = "Project Remote 2/6";
  public static final String PROJECT_REMOTE_15 = "Project Remote 2/7";
  public static final String PROJECT_REMOTE_16 = "Project Remote 2/8";
  public static final String PROJECT_REMOTE_17 = "Project Remote 3/1";
  public static final String PROJECT_REMOTE_18 = "Project Remote 3/2";
  public static final String PROJECT_REMOTE_19 = "Project Remote 3/3";
  public static final String PROJECT_REMOTE_20 = "Project Remote 3/4";
  public static final String PROJECT_REMOTE_21 = "Project Remote 3/5";
  public static final String PROJECT_REMOTE_22 = "Project Remote 3/6";
  public static final String PROJECT_REMOTE_23 = "Project Remote 3/7";
  public static final String PROJECT_REMOTE_24 = "Project Remote 3/8";
  public static final String PROJECT_REMOTE_25 = "Project Remote 4/1";
  public static final String PROJECT_REMOTE_26 = "Project Remote 4/2";
  public static final String PROJECT_REMOTE_27 = "Project Remote 4/3";
  public static final String PROJECT_REMOTE_28 = "Project Remote 4/4";
  public static final String PROJECT_REMOTE_29 = "Project Remote 4/5";
  public static final String PROJECT_REMOTE_30 = "Project Remote 4/6";
  public static final String PROJECT_REMOTE_31 = "Project Remote 4/7";
  public static final String PROJECT_REMOTE_32 = "Project Remote 4/8";

  public static final String GLOBAL_CUSTOM = "Custom Per Control";
  public static final String PROJECT_REMOTES_PAGE_1 = "Project Remote Page 1";
  public static final String PROJECT_REMOTES_PAGE_2 = "Project Remote Page 2";
  public static final String PROJECT_REMOTES_PAGE_3 = "Project Remote Page 3";
  public static final String PROJECT_REMOTES_PAGE_4 = "Project Remote Page 4";


  public static final String[] KNOB = { KNOB_DISABLED, KNOB_VOLUME, KNOB_PAN, KNOB_SEND1, KNOB_SEND2, KNOB_SEND3, TRACK_REMOTE_1, TRACK_REMOTE_2, TRACK_REMOTE_3, TRACK_REMOTE_4, TRACK_REMOTE_5, TRACK_REMOTE_6, TRACK_REMOTE_7, TRACK_REMOTE_8, PROJECT_REMOTE_1, PROJECT_REMOTE_2, PROJECT_REMOTE_3, PROJECT_REMOTE_4, PROJECT_REMOTE_5, PROJECT_REMOTE_6, PROJECT_REMOTE_7, PROJECT_REMOTE_8, PROJECT_REMOTE_9, PROJECT_REMOTE_10, PROJECT_REMOTE_11, PROJECT_REMOTE_12, PROJECT_REMOTE_13, PROJECT_REMOTE_14, PROJECT_REMOTE_15, PROJECT_REMOTE_16, PROJECT_REMOTE_17, PROJECT_REMOTE_18, PROJECT_REMOTE_19, PROJECT_REMOTE_20, PROJECT_REMOTE_21, PROJECT_REMOTE_22, PROJECT_REMOTE_23, PROJECT_REMOTE_24, PROJECT_REMOTE_25, PROJECT_REMOTE_26, PROJECT_REMOTE_27, PROJECT_REMOTE_28, PROJECT_REMOTE_29, PROJECT_REMOTE_30, PROJECT_REMOTE_31, PROJECT_REMOTE_32};
  public static final String[] KNOB_GLOBAL = { KNOB_DISABLED, GLOBAL_CUSTOM, KNOB_VOLUME, KNOB_PAN, KNOB_SEND1, KNOB_SEND2, KNOB_SEND3, TRACK_REMOTE_1, TRACK_REMOTE_2, TRACK_REMOTE_3, TRACK_REMOTE_4, TRACK_REMOTE_5, TRACK_REMOTE_6, TRACK_REMOTE_7, TRACK_REMOTE_8, PROJECT_REMOTES_PAGE_1, PROJECT_REMOTES_PAGE_2, PROJECT_REMOTES_PAGE_3, PROJECT_REMOTES_PAGE_4};

  public static final String[] PROJECT_REMOTES_PAGES_1 = { PROJECT_REMOTE_1, PROJECT_REMOTE_2, PROJECT_REMOTE_3, PROJECT_REMOTE_4, PROJECT_REMOTE_5, PROJECT_REMOTE_6, PROJECT_REMOTE_7, PROJECT_REMOTE_8};
  public static final String[] PROJECT_REMOTES_PAGES_2 = { PROJECT_REMOTE_9, PROJECT_REMOTE_10, PROJECT_REMOTE_11, PROJECT_REMOTE_12, PROJECT_REMOTE_13, PROJECT_REMOTE_14, PROJECT_REMOTE_15, PROJECT_REMOTE_16};
  public static final String[] PROJECT_REMOTES_PAGES_3 = { PROJECT_REMOTE_17, PROJECT_REMOTE_18, PROJECT_REMOTE_19, PROJECT_REMOTE_20, PROJECT_REMOTE_21, PROJECT_REMOTE_22, PROJECT_REMOTE_23, PROJECT_REMOTE_24};
  public static final String[] PROJECT_REMOTES_PAGES_4 = { PROJECT_REMOTE_25, PROJECT_REMOTE_26, PROJECT_REMOTE_27, PROJECT_REMOTE_28, PROJECT_REMOTE_29, PROJECT_REMOTE_30, PROJECT_REMOTE_31, PROJECT_REMOTE_32};

  // MidiLight modes
  public static final String MIDI_LIGHT_OFF = "Off";
  public static final String MIDI_LIGHT_SOLO = "Solo";
  public static final String MIDI_LIGHT_MUTE = "Mute";
  public static final String MIDI_LIGHT_REC_ARM = "Rec";
  public static final String[] MIDI_LIGHT_MODES = { MIDI_LIGHT_OFF, MIDI_LIGHT_SOLO, MIDI_LIGHT_REC_ARM, MIDI_LIGHT_MUTE };

  // MuteButton modes, SoloButton modes, RecButton modes
  public static final String BUTTON_MUTE = "Mute";
  public static final String BUTTON_REC = "Rec";
  public static final String BUTTON_SOLO = "Solo";
  public static final String BUTTON_DISABLED = "Disabled";
  public static final String BUTTON_SELECTED_PLUGIN_WINDOW = "Selected Plugin Window";

  public static final String[] BUTTON = {BUTTON_DISABLED, BUTTON_MUTE, BUTTON_SOLO, BUTTON_REC, BUTTON_SELECTED_PLUGIN_WINDOW, TRACK_REMOTE_1, TRACK_REMOTE_2, TRACK_REMOTE_3, TRACK_REMOTE_4, TRACK_REMOTE_5, TRACK_REMOTE_6, TRACK_REMOTE_7, TRACK_REMOTE_8, PROJECT_REMOTE_2, PROJECT_REMOTE_3, PROJECT_REMOTE_4, PROJECT_REMOTE_5, PROJECT_REMOTE_6, PROJECT_REMOTE_7, PROJECT_REMOTE_8, PROJECT_REMOTE_9, PROJECT_REMOTE_10, PROJECT_REMOTE_11, PROJECT_REMOTE_12, PROJECT_REMOTE_13, PROJECT_REMOTE_14, PROJECT_REMOTE_15, PROJECT_REMOTE_16, PROJECT_REMOTE_17, PROJECT_REMOTE_18, PROJECT_REMOTE_19, PROJECT_REMOTE_20, PROJECT_REMOTE_21, PROJECT_REMOTE_22, PROJECT_REMOTE_23, PROJECT_REMOTE_24, PROJECT_REMOTE_25, PROJECT_REMOTE_26, PROJECT_REMOTE_27, PROJECT_REMOTE_28, PROJECT_REMOTE_29, PROJECT_REMOTE_30, PROJECT_REMOTE_31, PROJECT_REMOTE_32};

  public static final String[] BUTTON_GLOBAL = {BUTTON_DISABLED, GLOBAL_CUSTOM, BUTTON_MUTE, BUTTON_SOLO, BUTTON_REC, BUTTON_SELECTED_PLUGIN_WINDOW, TRACK_REMOTE_1, TRACK_REMOTE_2, TRACK_REMOTE_3, TRACK_REMOTE_4, TRACK_REMOTE_5, TRACK_REMOTE_6, TRACK_REMOTE_7, TRACK_REMOTE_8, PROJECT_REMOTES_PAGE_1, PROJECT_REMOTES_PAGE_2, PROJECT_REMOTES_PAGE_3, PROJECT_REMOTES_PAGE_4};

  // Fader Length modes
  public static final String FADER_LENGTH_FULL = "Full";
  public static final String FADER_LENGTH_VOLUME_MAX = "0db";
  public static final String[] FADER_LENGTH = { FADER_LENGTH_FULL, FADER_LENGTH_VOLUME_MAX };

  public static final Map<String, Integer> trackRemotesMap = new HashMap<>();


  public static final Map<String, int[]> projectRemotesMap = new HashMap<>();


  static {
    trackRemotesMap.put(TRACK_REMOTE_1, 0);
    trackRemotesMap.put(TRACK_REMOTE_2, 1);
    trackRemotesMap.put(TRACK_REMOTE_3, 2);
    trackRemotesMap.put(TRACK_REMOTE_4, 3);
    trackRemotesMap.put(TRACK_REMOTE_5, 4);
    trackRemotesMap.put(TRACK_REMOTE_6, 5);
    trackRemotesMap.put(TRACK_REMOTE_7, 6);
    trackRemotesMap.put(TRACK_REMOTE_8, 7);

    projectRemotesMap.put(PROJECT_REMOTE_1, new int[]{0,0});
    projectRemotesMap.put(PROJECT_REMOTE_2, new int[]{0,1});
    projectRemotesMap.put(PROJECT_REMOTE_3, new int[]{0,2});
    projectRemotesMap.put(PROJECT_REMOTE_4, new int[]{0,3});
    projectRemotesMap.put(PROJECT_REMOTE_5, new int[]{0,4});
    projectRemotesMap.put(PROJECT_REMOTE_6, new int[]{0,5});
    projectRemotesMap.put(PROJECT_REMOTE_7, new int[]{0,6});
    projectRemotesMap.put(PROJECT_REMOTE_8, new int[]{0,7});
    projectRemotesMap.put(PROJECT_REMOTE_9, new int[]{1,0});
    projectRemotesMap.put(PROJECT_REMOTE_10, new int[]{1,1});
    projectRemotesMap.put(PROJECT_REMOTE_11, new int[]{1,2});
    projectRemotesMap.put(PROJECT_REMOTE_12, new int[]{1,3});
    projectRemotesMap.put(PROJECT_REMOTE_13, new int[]{1,4});
    projectRemotesMap.put(PROJECT_REMOTE_14, new int[]{1,5});
    projectRemotesMap.put(PROJECT_REMOTE_15, new int[]{1,6});
    projectRemotesMap.put(PROJECT_REMOTE_16, new int[]{1,7});
    projectRemotesMap.put(PROJECT_REMOTE_17, new int[]{2,0});
    projectRemotesMap.put(PROJECT_REMOTE_18, new int[]{2,1});
    projectRemotesMap.put(PROJECT_REMOTE_19, new int[]{2,2});
    projectRemotesMap.put(PROJECT_REMOTE_20, new int[]{2,3});
    projectRemotesMap.put(PROJECT_REMOTE_21, new int[]{2,4});
    projectRemotesMap.put(PROJECT_REMOTE_22, new int[]{2,5});
    projectRemotesMap.put(PROJECT_REMOTE_23, new int[]{2,6});
    projectRemotesMap.put(PROJECT_REMOTE_24, new int[]{2,7});
    projectRemotesMap.put(PROJECT_REMOTE_25, new int[]{3,0});
    projectRemotesMap.put(PROJECT_REMOTE_26, new int[]{3,1});
    projectRemotesMap.put(PROJECT_REMOTE_27, new int[]{3,2});
    projectRemotesMap.put(PROJECT_REMOTE_28, new int[]{3,3});
    projectRemotesMap.put(PROJECT_REMOTE_29, new int[]{3,4});
    projectRemotesMap.put(PROJECT_REMOTE_30, new int[]{3,5});
    projectRemotesMap.put(PROJECT_REMOTE_31, new int[]{3,6});
    projectRemotesMap.put(PROJECT_REMOTE_32, new int[]{3,7});
  }

  static final HashMap<String, String[]> globalProjectRemotesMap = new HashMap<>();
  static {
    globalProjectRemotesMap.put(SettingsModes.PROJECT_REMOTES_PAGE_1, SettingsModes.PROJECT_REMOTES_PAGES_1);
    globalProjectRemotesMap.put(SettingsModes.PROJECT_REMOTES_PAGE_2, SettingsModes.PROJECT_REMOTES_PAGES_2);
    globalProjectRemotesMap.put(SettingsModes.PROJECT_REMOTES_PAGE_3, SettingsModes.PROJECT_REMOTES_PAGES_3);
    globalProjectRemotesMap.put(SettingsModes.PROJECT_REMOTES_PAGE_4, SettingsModes.PROJECT_REMOTES_PAGES_4);
  }

  static final List<String> allProjectRemotes = new ArrayList<>();
  static {
    globalProjectRemotesMap.forEach((k,v)->{
      Collections.addAll(allProjectRemotes, v);
    });
  }
}
