package com.kirkwoodwest;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;
import com.kirkwoodwest.closedwoods.trackmaster.projectremotes.ProjectRemotes;
import com.kirkwoodwest.hardware.HardwareMidiMix;

public class MaxiMixExtension extends ControllerExtension
{
   protected MaxiMixExtension(final MaxiMixExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();      


      //NEW PROJECT REMOTES
      ProjectRemotes projectRemotes = new ProjectRemotes(host, "", 7);
      //New Akai MidiMix Hardware
      HardwareMidiMix hardwareMidiMix = new HardwareMidiMix(host, 0, null);
      hardwareMidiMix.hardware_knobs_row_1.get(0).setBinding()

      //Create Button to do the mapping, should blink in mapping mode.

      // TODO: Perform your driver initialization here.
      // For now just show a popup notification for verification that it is running.
      host.showPopupNotification("MIDIMIX - MaxiMix Initialized");
   }

   @Override
   public void exit()
   {
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
      getHost().showPopupNotification("MIDIMIX - MaxiMix Exited");
   }

   @Override
   public void flush()
   {
      // TODO Send any updates you need here.
   }

   /** Called when we receive short MIDI message on port 0. */
   private void onMidi0(ShortMidiMessage msg) 
   {
      // TODO: Implement your MIDI input handling code here.
   }

   /** Called when we receive sysex MIDI message on port 0. */
   private void onSysex0(final String data) 
   {
      // MMC Transport Controls:
      if (data.equals("f07f7f0605f7"))
            mTransport.rewind();
      else if (data.equals("f07f7f0604f7"))
            mTransport.fastForward();
      else if (data.equals("f07f7f0601f7"))
            mTransport.stop();
      else if (data.equals("f07f7f0602f7"))
            mTransport.play();
      else if (data.equals("f07f7f0606f7"))
            mTransport.record();
   }

   private Transport mTransport;
}
