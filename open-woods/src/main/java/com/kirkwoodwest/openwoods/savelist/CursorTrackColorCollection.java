package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.CursorTrack;

/**
 * Cursor Track color collection which updates the last used color to the cursor track color.
 */
public class CursorTrackColorCollection extends ColorCollection {
  public CursorTrackColorCollection(CursorTrack cursorTrack, int colorCount) {
   super(colorCount);
    cursorTrack.color().addValueObserver((r,g,b)->{
      setTempColor(Color.fromRGB(r,g,b));
    });
  }
}
