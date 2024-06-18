package com.kirkwoodwest.extensions.dualfighter;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class DualFighterExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("111d4df1-0b11-1c2a-9e65-adb401de0e5c");

   public DualFighterExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Twin Twister Remotes";
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
   public String getHardwareVendor(){ return "DJ TechTools";  }
   
   @Override
   public String getHardwareModel()
   {
      return "Midi Fighter Twister x 2";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 18;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 0;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 0;
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
   public DualFighterExtension createInstance(final ControllerHost host)
   {
      return new DualFighterExtension(this, host);
   }
}
