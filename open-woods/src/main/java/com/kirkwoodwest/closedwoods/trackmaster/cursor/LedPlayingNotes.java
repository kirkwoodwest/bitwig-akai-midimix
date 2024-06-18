package com.kirkwoodwest.closedwoods.trackmaster.cursor;

import com.bitwig.extension.controller.api.PlayingNote;
import com.bitwig.extension.controller.api.PlayingNoteArrayValue;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class LedPlayingNotes extends Led<PlayingNoteArrayValue> {
  private final Set<Integer> pitches = new HashSet<>();

  public LedPlayingNotes(OscHost oscHost, String oscTarget, Supplier<PlayingNoteArrayValue> supplier) {
    super(oscHost, oscTarget, supplier);
  }

  @Override
  public void update(boolean forceUpdate) {
    PlayingNoteArrayValue playingNoteArrayValue = supplier.get();
    PlayingNote[] playingNotes = playingNoteArrayValue.get();

    // Check if the number of playing notes has changed
    long timestamp = System.currentTimeMillis();
    if (playingNotes.length > 0) {
      Set<Integer> currentPitches = new HashSet<>();
      for (PlayingNote playingNote : playingNotes) {
        int pitch = playingNote.pitch();
        int velocity = playingNote.velocity();
        currentPitches.add(pitch);
        if (!pitches.contains(pitch)) {
          //LogUtil.print("Adding pitch: " + pitch + " &  velocity: " + velocity + " to " + oscTarget);
          oscHost.addMessageToQueue(oscTarget, pitch, velocity, timestamp);
        }
      }

      // Update the pitches set to only include currently playing notes
      pitches.retainAll(currentPitches);

      // Add new pitches that were not in the set before
      pitches.addAll(currentPitches);
    } else {
      pitches.clear();
    }
  }

  public static Supplier<PlayingNoteArrayValue> getSupplier(PlayingNoteArrayValue playingNote) {
    playingNote.markInterested();
    return () -> playingNote;
  }
}
