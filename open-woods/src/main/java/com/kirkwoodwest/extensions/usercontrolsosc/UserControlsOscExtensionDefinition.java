package com.kirkwoodwest.extensions.usercontrolsosc;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class UserControlsOscExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("1c514402-b6f9-441d-896b-abb73eb623d6");

   public UserControlsOscExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Basic OSC";
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
   public String getHardwareVendor()
   {
      return "Open Sound Control";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Basic OSC";
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
   public UserControlsOscExtension createInstance(final ControllerHost host)
   {
      return new UserControlsOscExtension(this, host);
   }
}
