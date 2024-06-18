package com.kirkwoodwest.extensions.bugreporting;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class BugReportingExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("1d511a11-b6f9-441d-e96b-abb73eb620a6");

   public BugReportingExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Bug Reporting Extension";
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
      return "Utilities";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Bug Reporting Extension";
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
   public BugReportingExtension createInstance(final ControllerHost host)
   {
      return new BugReportingExtension(this, host);
   }
}
