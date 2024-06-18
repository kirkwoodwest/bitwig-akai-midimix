package com.kirkwoodwest.extensions.test.MF3DLayers;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class MF3dLayersExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("0e111a11-b6f9-341e-f96b-aee11eb620a6");

   public MF3dLayersExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "TEST - Midi Fighter 3D Layers";
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
      return "Kirkwood West";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Kirkwood West";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 18;
   }

   @Override
   public int getNumMidiInPorts() { return 1; }

   @Override
   public int getNumMidiOutPorts()
   {
      return 1;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
   }

   @Override
   public MF3DLayersExtension createInstance(final ControllerHost host)
   {
      return new MF3DLayersExtension(this, host);
   }
}
