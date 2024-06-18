package com.kirkwoodwest.extensions.oscprojectfader;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class OscFaderCursorExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("1d511b11-b6f9-141d-d96a-1bb73eb650a2");

   public OscFaderCursorExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "OSC Project Fader";
   }
   
   @Override
   public String getAuthor()
   {
      return "Kirkwood West";
   }

   @Override
   public String getVersion()
   {
      return "0.0";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "Open Sound Control";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Project Fader";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 18;
   }

   @Override
   public int getNumMidiInPorts() { return 0; }

   @Override
   public int getNumMidiOutPorts()
   {
      return 0;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
   }

   @Override
   public OscFaderCursorExtension createInstance(final ControllerHost host)
   {
      return new OscFaderCursorExtension(this, host);
   }
}
