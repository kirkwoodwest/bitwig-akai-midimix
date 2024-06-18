package com.kirkwoodwest.openwoods.led;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.ArrayList;

public class LedHost {
  ArrayList<ILed> led_Interface_list;
  OscHost osc_host;


  public LedHost(){
    led_Interface_list = new ArrayList<>();
  }

  public void add(ILed led){
    led_Interface_list.add(led);
  }

  public void remove(ILed led){
    led_Interface_list.remove(led);
  }

  /**
   * Updates all Leds
   */
  public void updateLeds(boolean force_update){
    for(ILed led : led_Interface_list){
      led.update(force_update);
    }
  }
}
