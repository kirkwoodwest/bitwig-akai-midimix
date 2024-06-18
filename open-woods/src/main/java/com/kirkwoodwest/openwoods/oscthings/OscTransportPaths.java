
package com.kirkwoodwest.openwoods.oscthings;


public class OscTransportPaths {

  public final String play;
  public final String stop;

  public final String positionTime;
  public final String bars;
  public final String beats;
  public final String subBeats;
  public final String tempo;
  public final String automationOverrideActive;
  public final String resetAutomationOverride;
  private final String isPlaying;
  public String record;
  public String continuePlayback;
  public String isFillModeActive;
  public String togglePlay;
  public String tapTempo;
  public String fastForward;
  public String restart;
  public String rewind;


  public OscTransportPaths(String oscPath){

    play = oscPath + "/play";
    stop = oscPath + "/stop";
    record = oscPath + "/record";
    togglePlay = oscPath + "/toggle_play";
    restart = oscPath + "/restart";

    tapTempo = oscPath + "/tap_tempo";
    continuePlayback = oscPath + "/continue";
    isFillModeActive = oscPath + "/isFillModeActive";
    isPlaying = oscPath + "/is_playing";
    positionTime = oscPath + "/position/time";
    bars = oscPath + "/bars";
    beats = oscPath + "/beats";
    subBeats = oscPath + "/sub_beats";
    tempo = oscPath + "/tempo";
    rewind = oscPath + "/rewind";

    fastForward = oscPath + "/fast_forward";

    automationOverrideActive = oscPath + "/automation_override_active";
    resetAutomationOverride = oscPath + "/reset_automation_override";
  }
}
