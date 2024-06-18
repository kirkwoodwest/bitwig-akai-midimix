// Written by Kirkwood West - kirkwoodwest.com
// (c) 2023
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.hardware.HardwareMidiMix;
import com.kirkwoodwest.openwoods.led.ILed;
import com.kirkwoodwest.openwoods.led.LedNoteOnOff;
import com.kirkwoodwest.openwoods.locks.Lock;
import com.kirkwoodwest.openwoods.settings.SettingsHelper;
import com.kirkwoodwest.settings.ControlSettings;
import com.kirkwoodwest.settings.PreferencesJson;
import com.kirkwoodwest.settings.SettingsModes;
import com.kirkwoodwest.utils.LogUtil;

import java.util.ArrayList;
import java.util.function.Supplier;

public class AkaiMaxiMix extends ControllerExtension {
  //Class Variables
  private ControllerHost host;

  private static final int PORT_MIDIMIX = 0;
  private final ArrayList<ILed<?>> leds = new ArrayList<>();
  private HardwareMidiMix hardwareMidiMix;
  private TrackBank trackBank;
  private final ArrayList<HardwareActionBindable> cursorActions = new ArrayList<>();
  private HardwareActionBindable[] actionsMute;
  private CursorDevice cursorDevice;
  private SettableBooleanValue settingParameterPopup;

  protected AkaiMaxiMix(final AkaiMaxiMixExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    //Setup Preference and Document
    //Preference Settings
    PreferencesJson preferencesJson = new PreferencesJson(host, this.getExtensionDefinition());

    //Restart Extension Button
    preferencesJson.getSignalSetting("Restart", "Restart After any Changes", "Restart", ()-> {
      host.println("Restarting Extension...");
      host.restart();
    });

    SettableEnumValue cursorTrackOrBank = preferencesJson.getEnumSetting("Mode", "Mode", new String[]{"Cursor", "Track Bank"}, "Track Bank");
    cursorTrackOrBank.markInterested();

    SettableBooleanValue useCustomRemoteTag = preferencesJson.getBooleanSetting("Use Custom Remote Tag", "Remote Page", false);
    useCustomRemoteTag.markInterested();
    SettableStringValue remoteTag = preferencesJson.getStringSetting("Remote Tag", "Remote Page", 50, "maximix");
    remoteTag.markInterested();

    useCustomRemoteTag.addValueObserver((v)-> SettingsHelper.setVisible(remoteTag, v));

    settingParameterPopup = host.getNotificationSettings().getUserNotificationsEnabled();
    settingParameterPopup.markInterested();


    //Configures midi lights
    SettableEnumValue midiLightsSetting = preferencesJson.getEnumSetting("Midi Light Override", "Lights", SettingsModes.MIDI_LIGHT_MODES, SettingsModes.MIDI_LIGHT_OFF );
    midiLightsSetting.markInterested();

    //Create Knob Settings
    ControlSettings knobs1Settings = new ControlSettings(preferencesJson, "Knobs 1", "Knob", SettingsModes.KNOB_GLOBAL, SettingsModes.KNOB, SettingsModes.KNOB_SEND1);
    ControlSettings knobs2Settings = new ControlSettings(preferencesJson, "Knobs 2", "Knob", SettingsModes.KNOB_GLOBAL,  SettingsModes.KNOB, SettingsModes.KNOB_SEND2);
    ControlSettings knobs3Settings = new ControlSettings(preferencesJson, "Knobs 3", "Knob", SettingsModes.KNOB_GLOBAL, SettingsModes.KNOB, SettingsModes.KNOB_SEND3);

    ControlSettings muteButtonSettings = new ControlSettings(preferencesJson, "Buttons Mute", "Button", SettingsModes.BUTTON_GLOBAL, SettingsModes.BUTTON, SettingsModes.BUTTON_MUTE);
    ControlSettings soloButtonSettings = new ControlSettings(preferencesJson, "Buttons Solo", "Button", SettingsModes.BUTTON_GLOBAL, SettingsModes.BUTTON, SettingsModes.BUTTON_SOLO);

    ControlSettings armButtonSettings = new ControlSettings(preferencesJson, "Buttons Arm", "Button", SettingsModes.BUTTON_GLOBAL, SettingsModes.BUTTON, SettingsModes.BUTTON_REC);
    ControlSettings faderSettings = new ControlSettings(preferencesJson, "Faders", "Fader", SettingsModes.KNOB_GLOBAL, SettingsModes.KNOB,  SettingsModes.KNOB_VOLUME);

    SettableEnumValue faderLength = preferencesJson.getEnumSetting("Fader Limit", "Fader", SettingsModes.FADER_LENGTH, SettingsModes.FADER_LENGTH_FULL);
    faderLength.markInterested();

    //Determine if we should make a cursor track
    boolean settingsHavePluginWindow = false;
    ControlSettings[] controlSettingsContainer = new ControlSettings[]{muteButtonSettings, soloButtonSettings, armButtonSettings};
    for (ControlSettings controlSetting : controlSettingsContainer) {
      ArrayList<SettableEnumValue> settings = controlSetting.getSettings();
      for (SettableEnumValue setting : settings) {
        String s = setting.get();
        if (s.equals(SettingsModes.BUTTON_SELECTED_PLUGIN_WINDOW)) {
          settingsHavePluginWindow = true;
        }
      }
    }

    if(settingsHavePluginWindow){
      CursorTrack cursorTrack = host.createCursorTrack("MaxiMix CursorTrack Floating", "MaxiMix Cursor Track Floating", 0, 0, true);
      cursorDevice = cursorTrack.createCursorDevice("MaxiMix CursorDevice Custom", "MaxiMix Cursor Device Floating", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
      cursorDevice.isPlugin().markInterested();
      cursorDevice.isWindowOpen().markInterested();
    }

    hardwareMidiMix = new HardwareMidiMix(host, PORT_MIDIMIX, this::onMidiMix);

    int numTracks = 8;
    int numSends = 8;
    int numScenes = 8;

    ArrayList<Track> tracks = new ArrayList<>();

    if (cursorTrackOrBank.get().equals("Cursor")) {
      //Setup Cursor Tracks
      for (int i = 0; i < numTracks; i++) {
        final CursorTrack cursorTrack = host.createCursorTrack("MaxiMix Cursor Track " + (i + 1), "MaxiMix Cursor Track " + (i + 1), numSends, numScenes, true);
        final int index = i;
        tracks.add(cursorTrack);

        cursorTrack.isPinned().markInterested();
        cursorTrack.name().markInterested();
        HardwareActionBindable cursorAction = host.createAction(() -> {
            cursorTrack.isPinned().set(false);
            cursorTrack.isPinned().set(true);
            host.showPopupNotification("Moved Cursor Track " + index + ": " + cursorTrack.name().get());
          }
        , () -> "Toggle Cursor Track or Track Bank");
        cursorActions.add(cursorAction);
      }

      //TrackBank Nav
      HardwareButton bankLeft = hardwareMidiMix.getHardwareButtonBank(0);
      bankLeft.pressedAction().setBinding(host.createAction(() -> bindButtonsForCursorTrackMap(true), () -> "Pressed Bank Left"));
      bankLeft.releasedAction().setBinding(host.createAction(() -> bindButtonsForCursorTrackMap(false), () -> "Pressed Bank Left"));

    } else {
      //------------------------------------------------------------------------------------
      // Setup TrackBank

      trackBank = host.createTrackBank(numTracks,numSends,numScenes,true);
      trackBank.sceneBank().setIndication(true);
      trackBank.setShouldShowClipLauncherFeedback(true);
      trackBank.scrollPosition().markInterested();

      //TrackBank Nav
      HardwareButton bankLeft = hardwareMidiMix.getHardwareButtonBank(0);
      bankLeft.pressedAction().setBinding(host.createAction(() ->
              trackBank.scrollPosition().set(trackBank.scrollPosition().get() - 1), () -> "Pressed Bank Left"));
      HardwareButton bankRight = hardwareMidiMix.getHardwareButtonBank(1);
      bankRight.pressedAction().setBinding(host.createAction(() ->
        trackBank.scrollPosition().set(trackBank.scrollPosition().get() + 1), () -> "Pressed Bank Right"));

      for (int i = 0; i <numTracks; i++) {
        Track track = trackBank.getItemAt(i);
        track.name().markInterested();
        tracks.add(track);
      }

      tracks.get(0).name().addValueObserver((name) -> {
        //This needs to be delayed because the entire track bank is not updated at the same time before this observer is called.
        host.scheduleTask(() -> {
          String notification = "Track Bank: " + name + " <-> " + trackBank.getItemAt(trackBank.getSizeOfBank()-1).name().get();
          host.showPopupNotification(notification);
        }, 100);
      });
    }

    //Setup Global Cursors
    Track rootTrackGroup = host.getProject().getRootTrackGroup();
    ArrayList<CursorRemoteControlsPage> rootRemotePages = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      CursorRemoteControlsPage cursorRemoteControlsPage;
      if(useCustomRemoteTag.get()) {String tag = remoteTag.get();
       cursorRemoteControlsPage = rootTrackGroup.createCursorRemoteControlsPage("Maximix Project Remote " + (i + 1), 8, tag + (i + 1));
      } else {
        cursorRemoteControlsPage = rootTrackGroup.createCursorRemoteControlsPage("Maximix Project Remote " + (i + 1), 8, "");
        Lock.create(cursorRemoteControlsPage, i);
      }
      rootRemotePages.add(cursorRemoteControlsPage);
    }

    //------------------------------------------------------------------------------------
    // Setup Cursor Pages
    actionsMute = new HardwareActionBindable[numTracks];
    HardwareActionBindable[] actionsSolo = new HardwareActionBindable[numTracks];
    HardwareActionBindable[] actionsArm = new HardwareActionBindable[numTracks];
    for (int i = 0; i < numTracks; i++) {
      Track track = tracks.get(i);
      track.playingNotes().markInterested();

      CursorRemoteControlsPage trackRemotePage;
      if(useCustomRemoteTag.get()) {
        String tag = remoteTag.get();
        trackRemotePage = track.createCursorRemoteControlsPage("MaxiMix Track Remote " + (i + 1), 8, tag);
      } else {
        trackRemotePage = track.createCursorRemoteControlsPage("MaxiMix Track Remote " + (i + 1), 8, "");
      }

      String knob1Setting = knobs1Settings.getSettings().get(i).get();
      String knob2Setting = knobs2Settings.getSettings().get(i).get();
      String knob3Setting = knobs3Settings.getSettings().get(i).get();
      String faderSetting = faderSettings.getSettings().get(i).get();
      String muteButtonSetting = muteButtonSettings.getSettings().get(i).get();
      String soloButtonSetting = soloButtonSettings.getSettings().get(i).get();
      String recButtonSetting = armButtonSettings.getSettings().get(i).get();

      //Volume
      double faderMaxLevel = .795;
      if (faderLength.get().equals(SettingsModes.FADER_LENGTH_FULL)) {
        faderMaxLevel = 1.0;
      }
      {
        //Fader
        Parameter parameter = getParameterFromSetting(faderSetting, rootRemotePages, trackRemotePage, track);
        if(parameter !=null) {
          hardwareMidiMix.hardware_faders.get(i).addBindingWithRange(parameter, 0, faderMaxLevel);
        }
      }
      {
        //Knob1
        Parameter parameter = getParameterFromSetting(knob1Setting, rootRemotePages, trackRemotePage, track);
        if(parameter !=null) {
          hardwareMidiMix.hardware_knobs_row_1.get(i).setBinding(parameter);
        }
      }
      {
        //Knob2
        Parameter parameter = getParameterFromSetting(knob2Setting, rootRemotePages, trackRemotePage, track);
        if(parameter !=null) {
          hardwareMidiMix.hardware_knobs_row_2.get(i).setBinding(parameter);
        }
      }
      {
        //Knob3
        Parameter parameter = getParameterFromSetting(knob3Setting, rootRemotePages, trackRemotePage, track);
        if(parameter !=null) {
          hardwareMidiMix.hardware_knobs_row_3.get(i).setBinding(parameter);
        }
      }

      //check if we need to set the midi lights
      Supplier<Boolean> muteLightSupplier = null;
      Supplier<Boolean> soloLightSupplier = null;
      Supplier<Boolean> recLightSupplier = null;
      String lights = midiLightsSetting.get();
      if (lights.equals(SettingsModes.MIDI_LIGHT_MUTE)) {
        muteLightSupplier = createPlayingNotesSupplier(track);
      } else if (midiLightsSetting.get().equals(SettingsModes.MIDI_LIGHT_SOLO)) {
        soloLightSupplier = createPlayingNotesSupplier(track);
      } else if (midiLightsSetting.get().equals(SettingsModes.MIDI_LIGHT_REC_ARM)) {
        recLightSupplier = createPlayingNotesSupplier(track);
      }

      //Mute Button
      LedNoteOnOff muteLed = hardwareMidiMix.getButtonLedRow1(i, null);
      HardwareButton muteButton = hardwareMidiMix.getHardwareButtonRow1(i);
      setupButton(muteButton, actionsMute, i, muteButtonSetting, track, muteLed, muteLightSupplier,rootRemotePages, trackRemotePage);

      //Rec Button
      LedNoteOnOff recLed = hardwareMidiMix.getButtonLedRow2(i, null);
      HardwareButton recButton = hardwareMidiMix.getHardwareButtonRow2(i);
      setupButton(recButton, actionsArm, i,  recButtonSetting, track, recLed, recLightSupplier, rootRemotePages, trackRemotePage);

      //Solo Button
      LedNoteOnOff soloLed = hardwareMidiMix.getButtonLedRow3(i, null);
      HardwareButton soloButton = hardwareMidiMix.getHardwareButtonRow3(i);
      setupButton(soloButton, actionsSolo, i, soloButtonSetting, track, soloLed, soloLightSupplier, rootRemotePages, trackRemotePage);
    }

    //Put save Load settigns in last.
    preferencesJson.createSaveLoadSettings("/kirkwoodwest/midimix-maximix.json");

    reportExtensionStatus("Initialized.");
  }

  private void onMidiMix(int i, int i1, int i2) {
  }

  private void reportExtensionStatus(String status_message)
  {
    //If your reading this... Say hello to a loved one today. <3
    String name = this.getExtensionDefinition().getName();
    String version = this.getExtensionDefinition().getVersion();
    host.println(name + " " + version + " " + status_message);
  }

  @Override
  public void exit() {
    //shutdown all hardware, leds will be reset
    reportExtensionStatus("Exited.");
  }

  @Override
  public void flush() {
    leds.forEach((led)-> led.update(false));
  }

  public Supplier<Boolean> createPlayingNotesSupplier(Track track) {
    //Playing notes led
    return () -> {
      int length = track.playingNotes().get().length;
      return length > 0;
    };
  }

  public void bindButtonsForCursorTrackMap(boolean b){
    if(b) {
      for (int i = 0; i < 8; i++) {
        HardwareButton button = hardwareMidiMix.getHardwareButtonRow1(i);
        button.pressedAction().setBinding(cursorActions.get(i));
      }
    } else {
      for (int i = 0; i < 8; i++) {
        HardwareButton button = hardwareMidiMix.getHardwareButtonRow1(i);
        button.pressedAction().setBinding(actionsMute[i]);
      }
    }
  }

  /**
   * Sets up the button based on the setting information passed in.
   * @param button  The Hardware Button
   * @param actions List of actions
   * @param index Index used to select track and action
   * @param setting Setting to use
   * @param track track to set button
   * @param led Led for Light
   * @param lightSupplier Led Light Supplier for changing behavior if needed
   * @param rootRemotePages Project Remotes Page
   * @param trackRemotePage Track Remotes for the track
   */
  public void setupButton(HardwareButton button, HardwareActionBindable[] actions, int index, String setting, Track track, LedNoteOnOff led, Supplier<Boolean> lightSupplier, ArrayList<CursorRemoteControlsPage> rootRemotePages, CursorRemoteControlsPage trackRemotePage) {

    Supplier<Boolean> supplier = null;
    boolean hasProjectRemote = SettingsModes.projectRemotesMap.containsKey(setting);
    boolean hasTrackRemote = SettingsModes.trackRemotesMap.containsKey(setting);

    if(hasProjectRemote || hasTrackRemote) {
      //Track Remote Or Project Remote
      Parameter parameter = getParameterFromSetting(setting, rootRemotePages, trackRemotePage, track);
      HardwareActionBindable pressedAction = createParameterButtonAction(parameter, "Remote Parameter Toggle");
      button.pressedAction().setBinding(pressedAction);
      actions[index] = pressedAction;
      supplier = createParameterLedSupplier(parameter);
      addPopupDisplayToTrackParameter(track, parameter);
    } else {
      switch (setting) {
        case SettingsModes.BUTTON_MUTE: {
          //Mute
          SettableBooleanValue mute = track.mute();
          mute.markInterested();
          HardwareActionBindable pressedAction = mute.toggleAction();
          button.pressedAction().setBinding(pressedAction);
          actions[index] = pressedAction;
          supplier = mute::get;
          break;
        }
        case SettingsModes.BUTTON_SOLO: {
          //Solo
          SettableBooleanValue solo = track.solo();
          solo.markInterested();
          HardwareActionBindable pressedAction = solo.toggleAction();
          button.pressedAction().setBinding(pressedAction);
          actions[index] = pressedAction;
          supplier = solo::get;
          break;
        }
        case SettingsModes.BUTTON_REC: {
          //Rec Arm
          SettableBooleanValue recArm = track.arm();
          recArm.markInterested();
          HardwareActionBindable pressedAction = recArm.toggleAction();
          button.pressedAction().setBinding(pressedAction);
          actions[index] = pressedAction;
          supplier = recArm::get;
          break;
        }
        case SettingsModes.BUTTON_SELECTED_PLUGIN_WINDOW: {
          //Selected Plugin Window
          SettableBooleanValue isWindowOpen = cursorDevice.isWindowOpen();
          isWindowOpen.markInterested();
          HardwareActionBindable pressedAction = isWindowOpen.toggleAction();
          button.pressedAction().setBinding(pressedAction);
          actions[index] = pressedAction;
          supplier = isWindowOpen::get;
          break;
        }
        case SettingsModes.BUTTON_DISABLED:
          //Off
          button.pressedAction().clearBindings();
          break;
      }
    }

    if(lightSupplier != null) {
      led.setSupplier(lightSupplier);
    } else {
      led.setSupplier(supplier);
    }

    leds.add(led);
  }

  /**
   * Creates a button action for the parameter
   * @param parameter Parameter to create action on
   * @param description Description of the action
   * @return Hardware Action for binding
   */
  public HardwareActionBindable createParameterButtonAction(Parameter parameter, String description) {
    parameter.value().markInterested();
    parameter.markInterested();
    parameter.setIndication(true);
    return host.createAction(
            () -> {
              if (Double.compare(parameter.value().get(), 0.0) == 0) {
                parameter.value().setImmediately(1);
              } else {
                parameter.value().setImmediately(0);
              }
            }, () -> description);
  }

  /**
   * Creates a supplier for the led based on the parameter value
   * @param parameter Parameter to create supplier on
   * @return Supplier<Boolean> for the Led
   */
  public Supplier<Boolean> createParameterLedSupplier(Parameter parameter) {
    return () -> Double.compare(parameter.value().get(), 0.0) != 0;
  }

  /**
   * Returns the parameter based on the setting information passed in. This also sets up the parameter to display information in the popup.
   * @param setting String for the setting
   * @param rootRemotePages Project Remotes Pages
   * @param trackRemotePage Track Remotes
   * @param track Track for parameter
   * @return Parameter
   */
  public Parameter getParameterFromSetting(String setting, ArrayList<CursorRemoteControlsPage> rootRemotePages, CursorRemoteControlsPage trackRemotePage, Track track){
      Parameter parameter = null;
      if(SettingsModes.projectRemotesMap.containsKey(setting)) {
        int[] pageAndIndex = SettingsModes.projectRemotesMap.get(setting);
        int page = pageAndIndex[0];
        int remoteIndex = pageAndIndex[1];
        parameter = rootRemotePages.get(page).getParameter(remoteIndex);
        addPopupDisplayToParameter(parameter);
      } else if (SettingsModes.trackRemotesMap.containsKey(setting)) {
        int remoteIndex = SettingsModes.trackRemotesMap.get(setting);
        parameter = trackRemotePage.getParameter(remoteIndex);
        addPopupDisplayToTrackParameter(track, parameter);
      } else if (setting.equals(SettingsModes.KNOB_VOLUME)) {
        parameter = track.volume();
        addPopupDisplayToTrackParameter(track, parameter);
      } else if (setting.equals(SettingsModes.KNOB_PAN)) {
        parameter = track.pan();
        addPopupDisplayToTrackParameter(track, parameter);
      } else if (setting.equals(SettingsModes.KNOB_SEND1)) {
        parameter = track.sendBank().getItemAt(0);
        addPopupDisplayToTrackParameter(track, parameter);
      } else if (setting.equals(SettingsModes.KNOB_SEND2)) {
        parameter = track.sendBank().getItemAt(1);
        addPopupDisplayToTrackParameter(track, parameter);
      } else if (setting.equals(SettingsModes.KNOB_SEND3)) {
        parameter = track.sendBank().getItemAt(2);
        addPopupDisplayToTrackParameter(track, parameter);
      }
      return parameter;
  }

  /**
   * Adds a popup display for parameter changes
   * @param parameter Parameter to make assignment
   */
  private void addPopupDisplayToParameter(Parameter parameter) {
    parameter.name().markInterested();
    parameter.displayedValue().addValueObserver((value) -> {
      if(settingParameterPopup.get()) {
        host.showPopupNotification(parameter.name().get() + ": " + value);
      }
    });

  }

  /**
   * Adds a popup display for parameter changes with track name.
   * @param track Track to make assignment
   * @param parameter parameter to observe
   */
  private void addPopupDisplayToTrackParameter(Track track, Parameter parameter) {
    track.name().markInterested();
    parameter.name().markInterested();
    parameter.displayedValue().addValueObserver((value) -> {
      if(settingParameterPopup.get()) {
        host.showPopupNotification(track.name().get() + " / " + parameter.name().get() + ": " + value);
      }
    });
  }
}

