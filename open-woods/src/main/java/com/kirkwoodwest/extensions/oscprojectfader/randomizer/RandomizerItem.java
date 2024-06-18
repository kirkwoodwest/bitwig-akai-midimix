package com.kirkwoodwest.extensions.oscprojectfader.randomizer;

import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;

import java.util.Random;


public class RandomizerItem {

  private final Randomizer randomizer;
  private final OwRemoteControl owRemoteControl;
  private RandomizerItemData data = new RandomizerItemData();
  private Random random = new Random();

  //Constructor
  RandomizerItem(Randomizer randomizer, OwRemoteControl owRemoteControl, double min, double max) {
    this.randomizer = randomizer;
    this.owRemoteControl = owRemoteControl;
    data.min = min;
    data.max = max;
  }

  public OwRemoteControl getOwRemoteControl() {
    return owRemoteControl;
  }

  double getMin() {
    return data.min;
  }

  void setMin(double min) {
    data.min = min;
    randomizer.update();
  }

  double getMax() {
    return data.max;
  }
  void setMax(double max) {
    data.max = max;
    randomizer.update();
  }

  void setLocked(boolean locked) {
    data.locked = locked;
    randomizer.update();
  }

  boolean getLocked() {
    return data.locked;
  }

  double getRandomValue() {
    if(data.locked){
      return -1.0;
    } else {
      return random.nextDouble() * (data.min - data.max) + data.min;
    }
  }

  void randomizeParameter() {
    if(data.locked) return;
    double value = random.nextDouble() * (data.max - data.min) + data.min;
    if(owRemoteControl.exists()) {
      owRemoteControl.getRemoteControl().setImmediately(value);
    }
  }

  RandomizerItemData getData(){
    return data;
  }

  void setData(RandomizerItemData data){
    this.data = data;
  }

  public void init() {
    setMin(0.0);
    setMax(1.0);
  }
}
