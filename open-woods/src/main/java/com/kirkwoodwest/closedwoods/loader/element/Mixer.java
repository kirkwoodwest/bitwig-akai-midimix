package com.kirkwoodwest.closedwoods.loader.element;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.openwoods.cursortrack.OscCursorTrack;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Mixer {

  public static Map<String, ILoaderElement> createElement(ControllerHost host, OscController oscController, Map<String, Object> selector, CursorTrackBank cursorTrackBank, String oscPath) {
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> mixers = (List<Map<String, Object>>) selector.get("selector.mixer");
    HashMap<String, ILoaderElement> elements = new HashMap<>();
    if (mixers != null) {

      if(mixers.size() > 1) {
        host.println("Only one mixer per selector is supported");
        return elements;
      }

      for (Map<String, Object> mixer : mixers) {
        String mixerId = (String) mixer.get("id");
        final String mixerOscPath = oscPath + "/" +  mixer.get("osc_path");

        IntStream.rangeClosed(1, cursorTrackBank.getCursors().size()).forEach(index -> {
          String path = (mixerOscPath + "/" + index);
          CursorTrack cursorTrack = cursorTrackBank.getCursors().get(index - 1);
          OscCursorTrack oscCursorTrack = new OscCursorTrack(cursorTrack, path, oscController);
          elements.put(mixerId, oscCursorTrack);
        });

      }
    }
    return elements;
  }

  /**
   * Get the number of sends for the mixer selector
   * @param selector
   * @return
   */
  public static int getNumSends(Map<String, Object> selector) {
    int numSends = 0;
    List<Map<String, Object>> mixers = (List<Map<String, Object>>) selector.get("selector.mixer");
    if (mixers != null) {
      for (Map<String, Object> mixer : mixers) {
        int sendCount = (Integer) mixer.get("num_sends");
        numSends = Math.max(numSends, sendCount);
      }
    }
    return numSends;
  }
}
