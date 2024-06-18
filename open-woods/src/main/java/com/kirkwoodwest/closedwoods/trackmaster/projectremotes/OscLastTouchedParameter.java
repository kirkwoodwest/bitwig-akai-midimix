package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.led.IsTouchedListener;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.adapters.ParameterOscAdapter;

public class OscLastTouchedParameter implements IsTouchedListener {
  public static String ID = "LastTouchedParameter";
  private static OscLastTouchedParameter instance = null;
  private CursorTrack cursorTrack = null;
  private CursorDevice cursorDevice = null;
  private ParameterOscAdapter lastTouchedParameter;

  private OscLastTouchedParameter() {};

  public static OscLastTouchedParameter getInstance() {
    if(instance == null) {
      instance = new OscLastTouchedParameter();
    }
    return instance;
  }

  public void init(OscController oscController, CursorTrack cursorTrack, CursorDevice cursorDevice) {
    lastTouchedParameter = oscController.addParameter(ID, "/trackmaster/last_parameter", null, "Last Touched Parameter");
//
//    //remove parameter if cursor track or cursor device ever change
//    cursorTrack.name().addValueObserver(name -> {
//      if(led != null){
//        led.setParameter(null);
//      }
//    });
//
//    cursorDevice.name().addValueObserver(name -> {
//      if(lastTouchedParameter != null){
//        lastTouchedParameter.setParameter(null);
//      }
//    });
  }

  public void setParameter(Parameter parameter) {
//    if(led != null){
//      led.setParameter(parameter);
//    }
  }

  public Parameter getParameter() {
    return lastTouchedParameter.dataSource;
  }

  public void clearParameter(){
//    if(led != null){
//      led.setSupplier(null);
//    }
  }


  public void touch(){
//    if(led != null){
//      Parameter parameter = led.supplier.get();
//      if(parameter != null){
//        parameter.touch(true);
//        parameter.touch(false);
//      }
//    }
  }

  @Override
  public void isTouched(Parameter parameter) {
    setParameter(parameter);
  }
}
