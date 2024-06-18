package com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3;

import com.bitwig.extension.api.Color;
import com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3.HardwareLaunchPadMiniMK3.LIGHT_MODE_STATUS;
import com.bitwig.extension.controller.api.MidiOut;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.utils.ColorUtil;
import com.kirkwoodwest.utils.MidiUtil;

import java.util.function.Supplier;

public class LedLaunchPadMiniMK3 extends Led<LedLaunchPadMiniMK3.LedData> {
  Color color = Color.nullColor();
 LIGHT_MODE_STATUS lightModeStatus = LIGHT_MODE_STATUS.STATIC;
  public LedLaunchPadMiniMK3(MidiOut midiOut, int cc, Supplier<LedData> supplier) {
    super(midiOut, 0, MidiUtil.NOTE_ON, cc, supplier);
  }

  @Override
  public void sendData(LedData data) {
    if(data == null) return;
    Color                                        color           = data.color;
    LIGHT_MODE_STATUS lightModeStatus = data.lightModeStatus;

    if(this.color.equals(color) && this.lightModeStatus.equals(lightModeStatus)) return;
    sendData(lightModeStatus, color);

  }

  private void sendData(LIGHT_MODE_STATUS light_mode, Color color) {
    int launchPadColor = ColorUtil.getIndexFromColor(color, LaunchPadMiniMK3Colors.getMap());
    super.midiOut.sendMidi(light_mode.status, data2, launchPadColor);
  }

  public record LedData(LIGHT_MODE_STATUS lightModeStatus, Color color) {
  }
}
