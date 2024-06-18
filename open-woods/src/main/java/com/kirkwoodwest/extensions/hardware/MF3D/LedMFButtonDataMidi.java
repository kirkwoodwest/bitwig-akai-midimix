package com.kirkwoodwest.extensions.hardware.MF3D;

import com.bitwig.extension.api.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LedMFButtonDataMidi {
  protected static final int[] BRIGHTNESS_LEVEL = new int[]{18,33};
  protected static final int[] GATE = new int[]{34,41};
  protected static final int[] PULSE = new int[]{42,49};

  private static final int COLOR_RED_BRIGHT = 13;
  private static final int COLOR_RED_DARK = 19;
  private static final int COLOR_YELLOW_BRIGHT = 37;
  private static final int COLOR_YELLOW_DARK = 43;
  private static final int COLOR_GREEN_BRIGHT = 61;
  private static final int COLOR_GREEN_DARK = 67;
  private static final int COLOR_BLUE_BRIGHT = 85;
  private static final int COLOR_BLUE_DARK = 91;
  private static final int COLOR_PINK_BRIGHT = 109;
  private static final int COLOR_PINK_DARK = 115;
  private static final int COLOR_ORANGE_BRIGHT = 25;
  private static final int COLOR_ORANGE_DARK = 31;
  private static final int COLOR_LIME_BRIGHT = 49;
  private static final int COLOR_LIME_DARK = 55;
  private static final int COLOR_TEAL_BRIGHT = 73;
  private static final int COLOR_TEAL_DARK = 79;
  private static final int COLOR_WHITE_BRIGHT = 121;
  private static final int COLOR_BLACK = 0;
  //Poor Approximations
  private static final Color COLOR_DOUBLE_RED_BRIGHT = Color.fromRGB255(220, 50, 50);
  private static final Color COLOR_DOUBLE_RED_DARK = Color.fromRGB255(130, 50, 50);
  private static final Color COLOR_DOUBLE_YELLOW_BRIGHT = Color.fromRGB255(220, 100, 50);
  private static final Color COLOR_DOUBLE_YELLOW_DARK = Color.fromRGB255(130, 50, 50);
  private static final Color COLOR_DOUBLE_GREEN_BRIGHT = Color.fromRGB255(50, 220, 50);
  private static final Color COLOR_DOUBLE_GREEN_DARK = Color.fromRGB255(50, 130, 50);
  private static final Color COLOR_DOUBLE_BLUE_BRIGHT = Color.fromRGB255(50, 50, 220);
  private static final Color COLOR_DOUBLE_BLUE_DARK = Color.fromRGB255(50, 50, 130);
  private static final Color COLOR_DOUBLE_PINK_BRIGHT = Color.fromRGB255(220, 50, 100);
  private static final Color COLOR_DOUBLE_PINK_DARK = Color.fromRGB255(130, 50, 50);
  private static final Color COLOR_DOUBLE_ORANGE_BRIGHT = Color.fromRGB255(220, 50, 50);
  private static final Color COLOR_DOUBLE_ORANGE_DARK = Color.fromRGB255(130, 50, 50);
  private static final Color COLOR_DOUBLE_LIME_BRIGHT = Color.fromRGB255(100, 220, 50);
  private static final Color COLOR_DOUBLE_LIME_DARK = Color.fromRGB255(100, 130, 50);
  private static final Color COLOR_DOUBLE_TEAL_BRIGHT = Color.fromRGB255(50, 100, 220);
  private static final Color COLOR_DOUBLE_TEAL_DARK = Color.fromRGB255(50, 50, 130);
  private static final Color COLOR_DOUBLE_WHITE_BRIGHT = Color.fromRGB255(220, 220, 220);
  private static final Color COLOR_DOUBLE_BLACK = Color.fromRGB255(0, 0, 0);
  protected static final ArrayList<Color> DOUBLE_COLORS = new ArrayList<>(Arrays.asList(
          COLOR_DOUBLE_RED_BRIGHT, COLOR_DOUBLE_RED_DARK,
          COLOR_DOUBLE_YELLOW_BRIGHT, COLOR_DOUBLE_YELLOW_DARK,
          COLOR_DOUBLE_GREEN_BRIGHT, COLOR_DOUBLE_GREEN_DARK,
          COLOR_DOUBLE_BLUE_BRIGHT, COLOR_DOUBLE_BLUE_DARK,
          COLOR_DOUBLE_PINK_BRIGHT, COLOR_DOUBLE_PINK_DARK,
          COLOR_DOUBLE_ORANGE_BRIGHT, COLOR_DOUBLE_ORANGE_DARK,
          COLOR_DOUBLE_LIME_BRIGHT, COLOR_DOUBLE_LIME_DARK,
          COLOR_DOUBLE_TEAL_BRIGHT, COLOR_DOUBLE_TEAL_DARK,
          COLOR_DOUBLE_WHITE_BRIGHT, COLOR_DOUBLE_BLACK
  ));
  protected static final  ArrayList<Integer> INT_COLOR_CODES = new ArrayList<>(Arrays.asList(
          COLOR_RED_BRIGHT, COLOR_RED_DARK,
          COLOR_YELLOW_BRIGHT, COLOR_YELLOW_DARK,
          COLOR_GREEN_BRIGHT, COLOR_GREEN_DARK,
          COLOR_BLUE_BRIGHT, COLOR_BLUE_DARK,
          COLOR_PINK_BRIGHT, COLOR_PINK_DARK,
          COLOR_ORANGE_BRIGHT, COLOR_ORANGE_DARK,
          COLOR_LIME_BRIGHT, COLOR_LIME_DARK,
          COLOR_TEAL_BRIGHT, COLOR_TEAL_DARK,
          COLOR_WHITE_BRIGHT, COLOR_BLACK
  ));
}
