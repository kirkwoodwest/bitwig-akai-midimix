package com.kirkwoodwest.closedwoods.trackmaster.cursor;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.oscthings.OscBank;
import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.function.Supplier;

public class OscDeviceBank {
  public static final String ID = "DeviceBank";
  public static void create(DeviceBank deviceBank, CursorDevice cursorDevice, String oscPath, OscController oscController) {
    //Bank Navigation
    OscBank<Device> oscBank = new OscBank<>(deviceBank, oscController, oscPath, ID);

    int size = deviceBank.getCapacityOfBank();
    for (int i = 0; i < size; i++) {
      final int deviceIndex = i;
      final String oscDevicePath = oscPath + "/" + (i + 1);
      Device device = deviceBank.getItemAt(i);


      CursorDeviceLayer deviceCursorLayer = device.createCursorLayer();
      //Selected
      {
        String target = oscDevicePath + "/selected";
        BooleanValue isSelected = device.createEqualsValue(cursorDevice);
        oscController.addBooleanValue(ID, target, isSelected);
      }

      //Exists
      {
        String target = oscDevicePath + "/exists";
        oscController.addBooleanValue(ID, target, device.exists());
      }

      //Activated
      {
        String target = oscDevicePath + "/activated";
        oscController.addBooleanValue(ID, target, deviceCursorLayer.isActivated());
      }

      //Name
      {
        String target = oscDevicePath + "/name";
        oscController.addStringValue(ID, target, device.name());
      }

      //Preset name
      {
        String target = oscDevicePath + "/preset_name";
        oscController.addStringValue(ID, target, device.presetName());
      }

      //Preset Category
      {
        String target = oscDevicePath + "/preset_category";
        oscController.addStringValue(ID, target, device.presetCategory());
      }

      //Type
      {
        String target = oscDevicePath + "/type";
        device.deviceType().markInterested();
        Supplier<String> supplier = ()->{
          String s = device.deviceType().get();
          s = s.replaceAll("-", " ");
          s = s.toUpperCase();
          return s;
        };
        oscController.addEnumValue(ID, target, device.deviceType());
      }

      //Selection
      {
        String target = oscDevicePath + "/select";
        oscController.registerOscCallback(target, "Select Device " + (i + 1), (oscConnection, oscMessage)->{
          device.selectInEditor();
          oscController.requestFlush();
        });
      }

      //Move device Up
      {
        String target = oscDevicePath + "/move/up";
        oscController.registerOscCallback(target, "Move Device " + (i + 1), (oscConnection, oscMessage)->{
          if(deviceIndex == 0) return;
          Device beforeDevice = deviceBank.getDevice(deviceIndex - 1);
          if(beforeDevice.exists().get()) {
            beforeDevice.beforeDeviceInsertionPoint().moveDevices(device);
          }
         });
      }

      //Move device Down
      {
        String target = oscDevicePath + "/move/down";
        oscController.registerOscCallback(target, "Move Device " + (i + 1), (oscConnection, oscMessage)->{
          if(deviceIndex == size-1) return;
          Device afterDevice = deviceBank.getDevice(deviceIndex + 1);
          if(afterDevice.exists().get()) {
            afterDevice.afterDeviceInsertionPoint().moveDevices(device);
          }
        });
      }

      //Insertion Point Before
      {
        String target = oscDevicePath + "/browse/before";
        oscController.registerOscCallback(target, "Insert Device Before " + (i + 1), (oscConnection, oscMessage)->{
          device.beforeDeviceInsertionPoint().browse();;
        });
      }

      //Insertion Point After
      {
        String target = oscDevicePath + "/browse/after";
        oscController.registerOscCallback(target, "Insert Device After " + (i + 1), (oscConnection, oscMessage)->{
          if(device.exists().get()) {
            device.afterDeviceInsertionPoint().browse();
          } else {
            deviceBank.browseToInsertDevice(deviceIndex);
          }
        });
      }

      //Replace Device, deletes and selects other insertion point, its the only way to clear the browser.
      {
        String target = oscDevicePath + "/browse/replace";
        oscController.registerOscCallback(target, "Replace Device " + (i + 1), (oscConnection, oscMessage)->{
          //        device.replaceDeviceInsertionPoint().browse();  // Does not reset the browser...

          device.deleteObject();

          if(deviceIndex == 0) {
            deviceBank.browseToInsertDevice(0);
          } else {
            Device beforeDevice = deviceBank.getDevice(deviceIndex - 1);
            if(beforeDevice.exists().get()) {
              beforeDevice.afterDeviceInsertionPoint().browse();
            }
          }
        });
      }
//      {
//        //replace device
//        String target = oscDevicePath + "/browse/insert";
//        oscController.registerOscCallback(target, "Replace Device " + (i + 1), (oscConnection, oscMessage)->{
//          Device beforeDevice = deviceBank.getDevice(deviceIndex - 1);
//          if(beforeDevice.exists().get()) {
//            beforeDevice.beforeDeviceInsertionPoint().moveDevices(device);
//          }
//          device.replaceDeviceInsertionPoint().browse();
//          deviceBank.browseToInsertDevice(deviceIndex);
//        });
//      }

      //Remove Device
      {
        String target = oscDevicePath + "/delete";
        oscController.registerOscCallback(target, "Remove Device " + (i + 1), (oscConnection, oscMessage)->{
          device.deleteObject();
          if(deviceIndex == 0 && deviceBank.itemCount().get() > 0) {
            Device beforeDevice = deviceBank.getDevice(0);
            beforeDevice.selectInEditor();
          } else {
            Device beforeDevice = deviceBank.getDevice(deviceIndex - 1);
            beforeDevice.selectInEditor();
          }
        });
      }
    }
  }


}
