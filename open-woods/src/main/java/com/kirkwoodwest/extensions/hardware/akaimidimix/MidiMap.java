package com.kirkwoodwest.extensions.hardware.akaimidimix;

public class MidiMap {
  public static final int MIDI_CHANNEL = 0;
  private static final int BANK_LEFT_CC = 25;
  private static final int BANK_RIGHT_CC = 26;

  static int[] CC_BANKS = new int[]{BANK_LEFT_CC, BANK_RIGHT_CC};
  public static final int[] CC_KNOBS_1 = new int[]{16,20,24,28,46,50, 54, 44 };
  public static final int[] CC_KNOBS_2 = new int[]{17,21,25,29,47,51, 55, 59 };
  public static final int[] CC_KNOBS_3 = new int[]{18,22,26,30,28,52, 56, 60 };
  public static final int[] CC_FADERS  = new int[]{19,23,27,31,49,53, 57, 61 };

  static final int[] BTN_ROW_1 = new int[]{1,4,7,10,13,16, 19, 22 }; //MUTE
  static final int[] BTN_ROW_2 = new int[]{3,6,9,12,15,18, 21, 24 }; //RECORD ARM
  static final int[] BTN_ROW_3 = new int[]{2,5,8,11,14,17, 20, 23 }; //SOLO

  public static final int[] LED_BTNS_1 =  new int[]{1,4,7,10,13,16, 19, 22 }; //MUTE
  public static final int[] LED_BTNS_2 =  new int[]{3,6,9,12,15,18, 21, 24 }; //RECORD ARM
  public static final int[] LED_BTNS_3 =  new int[]{2,5,8,11,14,17, 20, 23 }; //SOLO
}
