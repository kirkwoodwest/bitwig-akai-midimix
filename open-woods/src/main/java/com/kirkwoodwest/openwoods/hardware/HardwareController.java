package com.kirkwoodwest.openwoods.hardware;
import com.kirkwoodwest.openwoods.HardwareBasic;
import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.openwoods.hardware.layer.ControlGroup;
import com.kirkwoodwest.openwoods.hardware.layer.ControlGroupFactory;
import com.kirkwoodwest.openwoods.hardware.layer.ControlLayer;

import java.io.Flushable;
import java.util.ArrayList;

public class HardwareController<T, S extends ControlGroup<T>> extends HardwareBasic implements Flushable {
  private T hardware = null;
  private final ControlGroupFactory<T, S> controlGroupFactory;
  private ControlLayer<T, S> activeControlLayer = null;
  private final ArrayList<ControlLayer<T, S>> controlLayers = new ArrayList<>();

  public HardwareController(ControllerHost host, int midiPortIndex, ControlGroupFactory<T, S> controlGroupFactory) {
    super(host.getMidiInPort(midiPortIndex), host.getMidiOutPort(midiPortIndex));
    this.controlGroupFactory = controlGroupFactory;
  }

  public void setHardware(T hardware){
    this.hardware = hardware;
  }

  public T getHardware() {
    return hardware;
  }

  public ControlLayer<T, S> createControlLayer() {
    int layerId = controlLayers.size();
    S subLayer = controlGroupFactory.create(hardware, layerId);
    ControlLayer<T, S> newControlLayer = new ControlLayer<>(this, controlGroupFactory, layerId);
    controlLayers.add(newControlLayer);
    return newControlLayer;
  }

  public void setLayerActive(ControlLayer<T, S>  layer, boolean active){
    if(active){
      layer.setActive(true);
      activeControlLayer = layer;
    } else {
      layer.setActive(false);
      activeControlLayer = null;
    }
  }

  @Override
  public void flush(){
    if(activeControlLayer != null){
      activeControlLayer.flush();
    }
  }
}
