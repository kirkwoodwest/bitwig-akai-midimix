package com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3;
import com.kirkwoodwest.utils.MidiUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MidiData {
  //Sysex Template Selection
  //public static final int LAUNCH_CTRL_SYSEX_TEMPLATE_ID = 9;
  //public static final String LAUNCH_CTRL_SELECT_TEMPLATE = arrayToSysex(new int[]{240, 0, 32, 41, 2, 17, 119, LAUNCH_CTRL_SYSEX_TEMPLATE_ID, 247});
  //public static final String LAUNCH_CTRL_LED_SYSEX_START = arrayToSysex(new int[]{240, 0, 32, 41, 2, 17, 120, LAUNCH_CTRL_SYSEX_TEMPLATE_ID});
  //public static final String LAUNCH_CTRL_LED_SYSEX_END = arrayToSysex(new int[]{247});
  static final String SYSEX_HEADER = "F0 00 20 29 02 0D";
  static final String SYSEX_SLEEP_MESSAGE = MessageFormat.format("{0} F0 00 20 29 02 0D 09", SYSEX_HEADER);

  static final String SYSEX_MODE_DAW = MessageFormat.format("{0} 10 01 F7", SYSEX_HEADER);
  static final String SYSEX_MODE_DAW_CLEAR = MessageFormat.format("{0} 12 00 00 00 F7", SYSEX_HEADER);
  static final String SYSEX_MODE_SESSION = MessageFormat.format("{0} 00 00 F7", SYSEX_HEADER);

  static final String SYSEX_PROGRAMMER_MODE = MessageFormat.format("{0} 0E 01 F7", SYSEX_HEADER);

  static final int BUTTON_MIDI_CHANNEL = 0;
  static final int LIGHT_MODE_STATIC_CHANNEL = 0;
  static final int LIGHT_MODE_FLASH_CHANNEL = 1;
  static final int LIGHT_MODE_PULSE_CHANNEL = 2;

  static final int LIGHT_MODE_STATUS_STATIC = MidiUtil.NOTE_ON|LIGHT_MODE_STATIC_CHANNEL;
  static final int LIGHT_MODE_STATUS_FLASH = MidiUtil.NOTE_ON|LIGHT_MODE_FLASH_CHANNEL;
  static final int LIGHT_MODE_STATUS_PULSE = MidiUtil.NOTE_ON|LIGHT_MODE_PULSE_CHANNEL;

  static final List<Integer> GRID_CC_MAP = createGridMap();
  static final List<Integer> FUNCTION_CC_MAP =  createFunctionMap();
  static final List<Integer> SCENE_CC_MAP =  createSceneMap();

  private static List<Integer> createGridMap() {
    final int rows = HardwareLaunchPadMiniMK3.GRID_HEIGHT;
    final int cols = HardwareLaunchPadMiniMK3.GRID_WIDTH;
    final int startValue = 88;

    return IntStream.range(0, rows * cols)
            .map(i -> startValue - i)
            .boxed()
            .collect(Collectors.toList());
  }

  private static List<Integer> createFunctionMap() {
    int[] cc = new int[]{91, 92, 93, 94, 95, 96, 97, 98};
    return IntStream.of(cc).boxed().collect(Collectors.toList());
  }

  private static List<Integer> createSceneMap() {
    int[] cc = new int[]{89, 79, 69, 59, 49, 39, 29, 19};
    return IntStream.of(cc).boxed().collect(Collectors.toList());
  }
}
