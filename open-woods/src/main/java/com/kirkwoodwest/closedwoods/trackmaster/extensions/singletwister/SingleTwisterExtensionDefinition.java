package com.kirkwoodwest.closedwoods.trackmaster.extensions.singletwister;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.closedwoods.trackmaster.extensions.TrackMasterDefinition;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SingleTwisterExtensionDefinition extends ControllerExtensionDefinition implements TrackMasterDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("252f4df8-1b69-1c2a-9e65-adb401de0e5c");

   public SingleTwisterExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "TrackMaster";
   }
   
   @Override
   public String getAuthor()
   {
      return "Kirkwood West";
   }

   @Override
   public String getVersion()
   {
      return "1.0";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor(){ return "Kirkwood West";  }
   
   @Override
   public String getHardwareModel()
   {
      return "TrackMaster";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 18;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 1;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 1;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
//      switch (platformType) {
//         case WINDOWS:
//         list.add(
//                 new String[]{"Tunnel Maker Midi Notes"},
//                 new String[]{"Tunnel Maker Midi Notes"});
//         list.add(
//                 new String[]{"Midi Fighter 3D"},
//                 new String[]{"Midi Fighter 3D"});
//         list.add(
//                 new String[]{"Midi Fighter Twister"},
//                 new String[]{"Midi Fighter Twister"});
//         list.add(
//                 new String[]{"Midi Fighter Twister 4"},
//                 new String[]{"Midi Fighter Twister 4"});
//         list.add(
//                 new String[]{"Midi Fighter Twister 6"},
//                 new String[]{"Midi Fighter Twister 6"});
//         list.add(
//                 new String[]{"MIDIIN2 (LPMiniMK3 MIDI)"},
//                 new String[]{"MIDIOUT2 (LPMiniMK3 MIDI)"});
//         list.add(
//                 new String[]{"MIDIIN4 (LPMiniMK3 MIDI)"},
//                 new String[]{"MIDIOUT4 (LPMiniMK3 MIDI)"});
//         list.add(
//                 new String[]{"Midi Fighter Twister 2"},
//                 new String[]{"Midi Fighter Twister 2"});
//      }
   }

   @Override
   public SingleTwisterExtension createInstance(final ControllerHost host)
   {
      return new SingleTwisterExtension(this, host);
   }

   @Override
   public List<Class<?>> getControllers() {
      List<Class<?>> controllers = new ArrayList<>();
      controllers.add(HardwareMFT.class);
      controllers.add(HardwareMFT.class);
      controllers.add(HardwareMF3D.class);
      controllers.add(HardwareMF3D.class);
      return controllers;
   }
}
