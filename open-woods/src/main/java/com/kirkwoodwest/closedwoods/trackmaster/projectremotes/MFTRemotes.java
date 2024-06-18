package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.hardware.valuetarget.VirtualRelativeBindable;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.savelist.ColorCollection;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;
import com.kirkwoodwest.extensions.hardware.MFT.MFTColorUtils;
import com.kirkwoodwest.openwoods.hardware.layer.ControlLayer;
import com.kirkwoodwest.extensions.hardware.MFT.ControlGroupMFT;
import com.kirkwoodwest.utils.MidiUtil;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Create Project remotes for 8 remotes.
 */
public class MFTRemotes {
  public static String ID = "MFTProjectRemotes";
  private final ArrayList<OwRemoteControl> remotes;
  private final ControllerHost host;
  private final ColorCollection colorCollection;
  private final OscMFTMapper oscMFTMapper;
  private final ControlGroupMFT subLayerMain;;
  private  ArrayList<Boolean> buttonStates = new ArrayList<>();

  public MFTRemotes(ControllerHost host, HardwareMFT hardwareMFT, ArrayList<OwRemoteControl> remotes, ColorCollection colorCollection, OscMFTMapper oscMFTMapper){
    this.host = host;

    this.remotes = remotes;
    this.colorCollection = colorCollection;
    this.oscMFTMapper = oscMFTMapper;

    ControlLayer<HardwareMFT, ControlGroupMFT> controlLayer = hardwareMFT.createControlLayer();
    subLayerMain = controlLayer.createSubLayer();
    subLayerMain.setUseVariableSpeedEncoder(true);

    int encoderCount = subLayerMain.getEncoderCount();
    for (int i = 0; i < encoderCount; i++) {
      setupEncoderButton(i);
      setupProjectRemote(i);
      buttonStates.add(false);
    }

    controlLayer.setSubLayerActive(subLayerMain, true);
    hardwareMFT.setLayerActive(controlLayer, true);
  }

  private void setupEncoderButton(final int index) {
    //Encoder Buttons
    HardwareActionBindable pressedAction = host.createAction(()->{
      setButtonStateViaEncoder(index, true);}, ()->"Project Remote Press");
    subLayerMain.addEncoderPressedAction(index, pressedAction);

    HardwareActionBindable releasedAction = host.createAction(()->{
      setButtonStateViaEncoder(index, false);}, ()->"Project Remote Release");
    subLayerMain.addEncoderReleasedAction(index, releasedAction);
  }

  private void setupProjectRemote(final int index) {
    OwRemoteControl owRemoteControl = remotes.get(index);
    RemoteControl remoteControl = owRemoteControl.getRemoteControl();

    subLayerMain.addEncoderBinding(index, remoteControl);

    //Update last touched remote
    VirtualRelativeBindable knobTouched = new VirtualRelativeBindable(host, (v)->{
      oscMFTMapper.setSelectedIndex(index);
    });

    subLayerMain.addEncoderBinding(index, knobTouched.get());

    Supplier<Integer> colorSupplier = () -> {
      if(owRemoteControl.exists()) {
        Color color = colorCollection.getColor(index);
        return MFTColorUtils.colorToInt(color);
      } else {
        return MFTColorUtils.colorBlack();
      }
    };

    subLayerMain.addEncoderColorLed(index, colorSupplier);

    Supplier<Integer> ringSupplier = () -> {
      if(owRemoteControl.exists()) {
        return MidiUtil.doubleTo127(remoteControl.value().get());
      } else {
        return 0;
      }
    };

    subLayerMain.addEncoderRingLed(index, ringSupplier);
  }

  private void setButtonStateViaEncoder(int encoderId, boolean state){
    if (encoderId >=subLayerMain.getEncoderCount() || encoderId < 0) {
      return; //out of bounds
    }
    if(state) {
      oscMFTMapper.setSelectedIndex(encoderId);
    }
    buttonStates.set(encoderId, state);
  }

  public boolean getButtonState(int index){
    return buttonStates.get(index);
  }
}
