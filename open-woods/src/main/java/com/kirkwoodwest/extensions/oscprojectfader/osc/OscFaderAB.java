package com.kirkwoodwest.extensions.oscprojectfader.osc;

import com.kirkwoodwest.extensions.oscprojectfader.fader.FaderAB;
import com.kirkwoodwest.extensions.oscprojectfader.fader.FaderAB.FADER_SIDE;

import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.function.Supplier;

/**
 * Stores values for a set of remote controls and allows you to recall them and morph between them.
 * Recommended to flush() this object before any other controller updates in order to update the parameters.
 */
public class OscFaderAB {
  private final OscController oscController;
  private final FaderAB remoteFader;


  /**
   * Creates a new OscFader
   * @param oscController
   * @param remoteFader
   */
  public OscFaderAB(OscController oscController, final FaderAB remoteFader, String basePath) {
    this.oscController = oscController;
    this.remoteFader = remoteFader;
    OscFaderPaths oscPath = new OscFaderPaths(basePath);

    int numPresets = remoteFader.getNumPresets();

    //Presets
    for (int i = 0; i < numPresets; i++) {
      final int index = i;
      final int oscOutIndex = i +1;
      {
        //Saving
        oscController.registerOscCallback(oscPath.getPresetSave(oscOutIndex), "Save Preset in slot " + oscOutIndex, (oscConnection, oscMessage) -> {
          remoteFader.savePreset(index);
        });
      }
      {
        //Deleting
        oscController.registerOscCallback(oscPath.getPresetClear(oscOutIndex), "Clears the preset in slot " + oscOutIndex, (oscConnection, oscMessage) -> {
          remoteFader.deletePreset(index);
        });
      }
      {
        //Randomizing
        oscController.registerOscCallback(oscPath.getPresetRandom(oscOutIndex), "Clears the preset in slot " + oscOutIndex, (oscConnection, oscMessage) -> {
          remoteFader.randomizePreset(index);
        });
      }
      {
        //Existing
        Supplier<Boolean> presetValue = () -> {
          return remoteFader.getPresetExists(index);
        };

        //TODO: OSC Fader
      //  oscController.addLedBoolean("OscFader", oscPath.getPresetExists(oscOutIndex), presetValue);
      }
    }


    //Fader A & B

    //Select A Input
    oscController.registerOscCallback(oscPath.FADER_A_SELECT, "Select Slot A Preset", (oscConnection, oscMessage) -> {
      int index = oscMessage.getInt(0);
      if(index > 0 && index <= numPresets) {
        remoteFader.loadPreset(index - 1, FADER_SIDE.A);
      }
    });

    //Select A Output

//TODO: Fix value type...
//    oscController.addLedInteger("OscFader", oscPath.FADER_A_SELECT, () -> {
//      return remoteFader.getSelectedPresetIndex(FADER_SIDE.A) + 1;
//    });

    //Select B
    oscController.registerOscCallback(oscPath.FADER_B_SELECT, "Select Slot B Preset", (oscConnection, oscMessage) -> {
      int index = oscMessage.getInt(0);
      if(index > 0 && index <= numPresets) {
        remoteFader.loadPreset(index - 1, FADER_SIDE.B);
      }
    });

    //TODO: Fix value type...
//    //Select B Output
//    oscController.addLedInteger("OscFader", oscPath.FADER_B_SELECT, () -> {
//      return remoteFader.getSelectedPresetIndex(FADER_SIDE.B) + 1;
//    });

    //Fader
    oscController.registerOscCallback(oscPath.FADER_DUAL_POSITION, "Fader Position", (oscConnection, oscMessage) -> {
      float value = oscMessage.getFloat(0);
      remoteFader.setMorphValue(value);
    });


    //TODO: Fix value type...
//    //Morph Value
//    Supplier<Double> faderValue = remoteFader::getMorphValue;
//    oscController.addLedDouble("OscFader", oscPath.FADER_DUAL_POSITION,faderValue);
  }
}
