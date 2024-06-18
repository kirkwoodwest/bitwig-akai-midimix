package com.kirkwoodwest.extensions.oscprojectfader.osc;

public class OscFaderPaths {
  private final String oscPath;
  public final String FADER_A_SELECT;
  public final String FADER_B_SELECT;
  public final String FADER_X1_SELECT;
  public final String FADER_X2_SELECT;
  public final String FADER_Y1_SELECT;
  public final String FADER_Y2_SELECT;
  public final String FADER_DUAL_POSITION;
  private final String FADER_X_POSITION;
  private final String FADER_Y_POSITION;
  private final String NUM_PARAMETERS;


  //HashMap with String as key and OscTarget as value

  public OscFaderPaths(String oscPath){
    this.oscPath        = oscPath;
    NUM_PARAMETERS      = oscPath + "/num_parameters";
    FADER_A_SELECT      = oscPath + "/a/select";
    FADER_B_SELECT      = oscPath + "/b/select";
    FADER_DUAL_POSITION = oscPath + "/dual/position";
    FADER_X1_SELECT     = oscPath + "/x1/select";
    FADER_X2_SELECT     = oscPath + "/x2/select";
    FADER_Y1_SELECT     = oscPath + "/y1/select";
    FADER_Y2_SELECT     = oscPath + "/y2/select";
    FADER_X_POSITION    = oscPath + "/quad/x/position";
    FADER_Y_POSITION    = oscPath + "/quad/y/position";
  }

  public String getPresetSave(int index) {
    return oscPath + "/preset/" + index + "/save";
  }

  public String getPresetRandom(int index) {
    return oscPath + "/preset/" + index + "/randomize";
  }

  public String getPresetClear(int index) {
    return oscPath + "/preset/" + index + "/clear";
  }

  public String getPresetExists(int index) {
    return oscPath + "/preset/" + index + "/exists";
  }
}
