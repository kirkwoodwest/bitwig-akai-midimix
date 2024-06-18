package com.kirkwoodwest.openwoods.cursortrack;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.PlayingNoteArrayValue;
import com.kirkwoodwest.openwoods.osc.OscController;

public class OscPlayingNotes {
  public OscPlayingNotes(ControllerHost host, CursorTrack cursorTrack, OscController oscController, String oscPath) {
        //Playing Notes
//    PlayingNoteArrayValue playingNoteArrayValue = cursorTrack.playingNotes();
//    LedPlayingNotes led = new LedPlayingNotes(oscController.getOscHost(), path.NOTE_PLAYED, LedPlayingNotes.getSupplier(playingNoteArrayValue));
//    //cursorTrack.playingNotes().addValueObserver();
    //oscController.addLed("NotesPlayed", led);

   /* //This doesn't really belong here but it needs to be done...
    oscController.registerOscCallback("/trackmaster/config/playing_notes_enabled","Enable Midi Not Reporting", (oscConnection, message)->{
      if(message.getTypeTag().equals(",T"))
        playingNotesEnabled = true;
      else if(message.getTypeTag().equals(",F")) {
        playingNotesEnabled = false;
      }
      oscController.setEnabledState("NotesPlayed", playingNotesEnabled);

    });*/
  }
}
