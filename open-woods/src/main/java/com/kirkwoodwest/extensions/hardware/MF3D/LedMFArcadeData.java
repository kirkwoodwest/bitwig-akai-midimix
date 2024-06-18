package com.kirkwoodwest.extensions.hardware.MF3D;

import com.bitwig.extension.api.Color;
import com.kirkwoodwest.utils.ColorUtil;

public class LedMFArcadeData extends LedMFButtonDataMidi {
  private static LedMFArcadeData BLACK_COLOR;
  static {
    BLACK_COLOR = new LedMFArcadeData(Color.fromRGB(0, 0, 0));
  }
  private Color color;
  private int mfColor;
  private AnimationState animationState = AnimationState.BRIGHTNESS_LEVEL;
  private int mfBrightness = BRIGHTNESS_LEVEL[BRIGHTNESS_LEVEL.length-1];
  private int gateLevel;
  private int pulseLevel;



  enum AnimationState {
    BRIGHTNESS_LEVEL,
    GATE,
    PULSE
  }

  public LedMFArcadeData(Color color){
    setColor(color);
  }

  public LedMFArcadeData(Color color, double brightness) {
    this(color);
    setBrightness(brightness);
  }

  public void setColor(Color color) {
    this.color = color;

    //get nearest distance to color in table...
    int index = ColorUtil.getIndexFromColor(color, DOUBLE_COLORS);
    this.mfColor = INT_COLOR_CODES.get(index);
  }



  public Color getColor() {
    return color;
  }

  public Integer getMfColor() {
    return mfColor;
  }

  public void setMfColor(Integer mfColor) {
    //find nearest index of an item in intColorCodes
    int index = INT_COLOR_CODES.indexOf(mfColor);
    if(index == -1) {
      throw new RuntimeException("Invalid color code: " + mfColor);
    }
    this.mfColor = mfColor;
    this.color = DOUBLE_COLORS.get(index);
  }

  public void setBrightness(double brightness){
    int range = BRIGHTNESS_LEVEL[1] - BRIGHTNESS_LEVEL[0];
    int brightnessLevel = (int) (brightness * range);
    brightnessLevel += BRIGHTNESS_LEVEL[0];
    this.mfBrightness = brightnessLevel;
    animationState = AnimationState.BRIGHTNESS_LEVEL;
  }

  public int getBrightness() {
    return mfBrightness;
  }

  public void setGate(int gate){
    int range = GATE[1] - GATE[0];
    //valid gate values are 0 - the gate range.
    if(gate < 0) gate = 0;
    if(gate > range) gate = range;
    int gateLevel = gate + GATE[0];
    this.gateLevel = gateLevel;
    animationState = AnimationState.GATE;
  }

  public int getGate() {
    return gateLevel;
  }

  //8 Levels here..
  public void setPulse(int pulse){
    int range = PULSE[1] - PULSE[0];
    //valid pulse values are 0 - the pulse range.
    if(pulse < 0) pulse = 0;
    if(pulse > range) pulse = range;
    int pulseLevel = pulse + PULSE[0];
    this.pulseLevel = pulseLevel;
    animationState = AnimationState.PULSE;
  }

  public int getPulse() {
    return pulseLevel;
  }


  public AnimationState getAnimationState() {
    return animationState;
  }

  public static boolean compare(LedMFArcadeData c1, LedMFArcadeData c2) {
    return ColorUtil.compare(c2.color, c1.color) &&
            c2.getBrightness() == c1.getBrightness() &&
            c2.getGate() == c1.getGate() &&
            c2.getPulse() == c1.getPulse() &&
            c2.getAnimationState() == c1.getAnimationState();
  }

  public static LedMFArcadeData blackColor() {
    return BLACK_COLOR;
  }
}
