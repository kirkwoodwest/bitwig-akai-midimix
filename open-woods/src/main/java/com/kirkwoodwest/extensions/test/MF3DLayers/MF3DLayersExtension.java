// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.test.MF3DLayers;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.openwoods.hardware.layer.ControlLayer;
import com.kirkwoodwest.extensions.hardware.MF3D.ControlGroupMF3D;
import com.kirkwoodwest.openwoods.superbank.SuperBank;
import com.kirkwoodwest.extensions.hardware.MF3D.LedMFArcadeData;
import com.kirkwoodwest.utils.LogUtil;

import java.util.HashMap;

import static com.kirkwoodwest.utils.LogUtil.reportExtensionStatus;

public class MF3DLayersExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;
  private HashMap<String, Action> selectActions = new HashMap<>();
  private SuperBank superBank;
  private SceneBank sceneBank;
  private ControlLayer<HardwareMF3D, ControlGroupMF3D> controlLayer;

  static class TestColors {
    public static final LedMFArcadeData LED_GREEN;
    public static final LedMFArcadeData LED_RED;
    public static final LedMFArcadeData LED_BLUE;

    static {
      LED_GREEN = new LedMFArcadeData(Color.fromRGB(0.0,1.0,0.0));

      LED_RED = new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0));

      LED_BLUE = new LedMFArcadeData(Color.fromRGB(0.0,0.0,1.0));
    }
  }

  protected MF3DLayersExtension(final MF3dLayersExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    Preferences preferences = host.getPreferences();

    HardwareSurface hardwareSurface = host.createHardwareSurface();
    MidiIn midiIn = host.getMidiInPort(0);
    MidiOut midiOut = host.getMidiOutPort(0);

    HardwareMF3D hardwareMF3D = new HardwareMF3D(host, 0, this::onMidi);

    //Simple test showing midifighter 3d as layers.

    //New Layer
    controlLayer = hardwareMF3D.createControlLayer();
    ControlGroupMF3D subLayer1 = controlLayer.createSubLayer();
    ControlGroupMF3D subLayer2 = controlLayer.createSubLayer();
    ControlGroupMF3D subLayer3 = controlLayer.createSubLayer();

    for (int i = 0; i < 16; i++) {
      HardwareActionBindable pressedAction = getAction(i, "First Pressed: ");
      HardwareActionBindable releasedAction = getAction(i, "First Released: ");
      subLayer1.addArcadeButtonPressedAction(i, pressedAction);
      subLayer1.addArcadeButtonReleasedAction(i, releasedAction);
      subLayer1.addArcadeButtonLed(i, () -> TestColors.LED_RED);
    }

    final LedMFArcadeData[] colorStatus = new LedMFArcadeData[16];
    for (int i = 0; i < 16; i++) {

      final int index = i;
      colorStatus[index] = LedMFArcadeData.blackColor();
      HardwareActionBindable pressedAction = host.createAction(
              ()->{
                host.println("Second Pressed: " + index);
                colorStatus[index] = TestColors.LED_BLUE;
              },
              ()->"");
      HardwareActionBindable releasedAction = host.createAction(
              ()->{
                host.println("Second Released: " + index);
                colorStatus[index] = TestColors.LED_GREEN;
              },
              ()->"");
      subLayer2.addArcadeButtonPressedAction(i, pressedAction);
      subLayer2.addArcadeButtonReleasedAction(i, releasedAction);
      subLayer2.addArcadeButtonLed(i, () -> {
        return colorStatus[index];
      });
    }

    for (int i = 0; i < 16; i++) {
        HardwareActionBindable pressedAction = getAction(i, "Second 1 Pressed: ");
        HardwareActionBindable releasedAction = getAction(i, "Second 1 Released: ");
        subLayer3.addArcadeButtonPressedAction(i, pressedAction);
        subLayer3.addArcadeButtonReleasedAction(i, releasedAction);
        subLayer3.addArcadeButtonLed(i, ()-> TestColors.LED_BLUE);
    }

    subLayer1.addTopButtonPressedAction(0, getSubLayerActiveAction(0, subLayer1, true));
    subLayer1.addTopButtonPressedAction(1, getSubLayerActiveAction(0, subLayer2, true));
    subLayer1.addTopButtonPressedAction(2, getSubLayerActiveAction(0, subLayer3, true));

    ControlGroupMF3D subLayer4 = controlLayer.createSubLayer();
    subLayer4.addArcadeButtonLed(0, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1,0.0), 1.0));
    subLayer4.addArcadeButtonLed(1, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1,0.0), 0.9));
    subLayer4.addArcadeButtonLed(2, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1,0.0), 0.8));
    subLayer4.addArcadeButtonLed(3, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1,0.0), 0.7));
    subLayer4.addArcadeButtonLed(4, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1,0.0), 0.6));
    subLayer4.addArcadeButtonLed(5, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1,0.0), 0.5));
    subLayer4.addArcadeButtonLed(6, ()-> new LedMFArcadeData(Color.fromRGB(1.0,1.0,0.0), 0.4));
    subLayer4.addArcadeButtonLed(7, ()-> new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0), 0.3));
    subLayer4.addArcadeButtonLed(8, ()-> new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0), 0.2));
    subLayer4.addArcadeButtonLed(9, ()-> new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0), 0.1));

    controlLayer.setSubLayerActive(subLayer4, true);

//    controlLayer.setSubLayerActive(subLayer1, true);
    controlLayer.setActive(true);

    //If your reading this... I hope you say hello to a loved one today. <3
    reportExtensionStatus(this, "Initialized");
  }

  private HardwareActionBindable getSubLayerActiveAction(int index, ControlGroupMF3D subLayer, boolean pressed) {
    if(pressed) {
      return host.createAction(
              () -> {
                host.println("SubLayer Activate: " + index);
                controlLayer.setSubLayerActive(subLayer, true);
              },
              () -> "");
    } else {
      return host.createAction(
              () -> {
                host.println("SubLayer Deactivate: " + index);
                controlLayer.setSubLayerActive(subLayer, false);
              },
              () -> "");
    }
  }
  private HardwareActionBindable getAction(int index, String s) {
    return host.createAction(
            ()->{
              host.println(s + index);
            },
            ()->"");
  }

  private HardwareActionBindable getActionLight(int index, String s, ControlGroupMF3D subLayer, int color) {
    return host.createAction(
            ()->{
              host.println(s + index);
            },
            ()->"");
  }

  private void onMidi(int i, int i1, int i2) {

  }

  @Override
  public void exit() {
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    reportExtensionStatus(this, "Exited");
  }

  @Override
  public void flush() {
    controlLayer.flush();
  }


}
