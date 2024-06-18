package com.kirkwoodwest.openwoods.hardware.layer;

import com.kirkwoodwest.openwoods.hardware.HardwareController;

import java.util.*;

public class ControlLayer<T, S extends ControlGroup<T>> {
  private final ControlGroupFactory<T, S> controlGroupFactory;
  private final int id;
  private final ArrayList<S> subLayers = new ArrayList<>();
  private final ArrayList<S> activeSubLayers = new ArrayList<>();
  private final HardwareController<T, S> hardwareController;
  private boolean active;
  private boolean forceUpdate = false;

  public ControlLayer(HardwareController<T, S> hardwareController, ControlGroupFactory<T, S> controlGroupFactory, int id) {
    this.hardwareController = hardwareController;
    this.controlGroupFactory = controlGroupFactory;
    this.id = id;
  }

  public S createSubLayer() {
    int subLayerId = subLayers.size();
    S subLayer = controlGroupFactory.create(hardwareController.getHardware(), subLayerId);
    subLayers.add(subLayer);
    return subLayer;
  }

  public int getId(){
    return id;
  }

  public void setSubLayerActive(S subLayer, boolean active){
    if(active){
      //check if in the list already and move it to the top...
      activeSubLayers.remove(subLayer);
      activeSubLayers.add(subLayer);
      subLayer.setActive();
    } else {
      subLayer.clearBindings();
      activeSubLayers.remove(subLayer);
      if(this.active){
        //if the layer is active we need to reset the sublayers.
        setActive(true);
      }
    }
    forceUpdate = true;
  }

  public void setActive(boolean b) {

    // Set active flag
    this.active = b;

    // Temporary list to hold subLayerIds for modification
    List<S> tempSubLayers = new ArrayList<>();

    // Iterate over activeSubLayers and collect subLayerIds for modification
    for (S subLayer : activeSubLayers) {
      tempSubLayers.add(subLayer);
    }

    // Now modify activeSubLayers based on collected subLayerIds
    for (S subLayer : tempSubLayers) {
      setSubLayerActive(subLayer, b);
    }
    forceUpdate = true;
  }

  public void flush(){
    boolean doForceUpdate = forceUpdate;
    //reverse layers so that the topmost layer is the last one in the list
    List<S> reversedActiveLayers = new ArrayList<>(activeSubLayers);
    Collections.reverse(reversedActiveLayers);

    if (!reversedActiveLayers.isEmpty()) {
      List<Integer> ledGroupIds = subLayers.get(0).getLedGroupIds();
      ledGroupIds.forEach(ledGroupId -> {
        //get the number of leds in the group
        int ledCount = subLayers.get(0).getLedCount(ledGroupId);
        for (int index = 0; index < ledCount; index++) {
                // Update LedRing
          int finalIndex = index;

          //this stream needs to use the int to get the subLayer
          reversedActiveLayers.stream()
                  .map(subLayer -> subLayer.getLed(ledGroupId, finalIndex))
                  .filter(Objects::nonNull)
                  .findFirst()
                  .ifPresent(led -> led.update(doForceUpdate));
        }
      });
    }
    forceUpdate = false;
  }

}
