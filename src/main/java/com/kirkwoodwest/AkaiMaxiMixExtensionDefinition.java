package com.kirkwoodwest;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class AkaiMaxiMixExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("a1fa1af8-1b69-1c2a-9e85-3db407da2e5c");

   public AkaiMaxiMixExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Midimix - MaxiMix";
   }
   
   @Override
   public String getAuthor()
   {
      return "Kirkwood West";
   }

   @Override
   public String getVersion()
   {
      return "1.0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor(){ return "Akai";  }
   
   @Override
   public String getHardwareModel()
   {
      return "Midimix";
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
      switch (platformType) {
         case WINDOWS:
         list.add(
                 new String[]{"MIDI Mix"},
                 new String[]{"MIDI Mix"});
         case MAC:
            list.add(
               new String[]{"MIDI Mix"},
               new String[]{"MIDI Mix"});
          case LINUX:
             list.add(
               new String[]{"MIDI Mix"},
               new String[]{"MIDI Mix"});
      }
   }

   @Override
   public AkaiMaxiMix createInstance(final ControllerHost host)
   {
      return new AkaiMaxiMix(this, host);
   }
}
