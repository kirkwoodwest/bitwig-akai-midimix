package com.kirkwoodwest.openwoods.oscthings.songbank;


import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.osc.adapters.OscAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SceneSongOscAdapter extends OscAdapter<ClipLauncherSlot> {
  private final ObserverData<String> name = new ObserverData<>("573051265", () -> setDirty(true));
  private final ObserverData<Color> color = new ObserverData<>(com.bitwig.extension.api.Color.blackColor(), () -> setDirty(true));
  private final ObserverData<Boolean> isPlaying = new ObserverData<>(false, () -> setDirty(true));
  private final ClipLauncherSlot clipLauncherSlot;
  private final int index;


  // Another constructor with a different set of parameters
  public SceneSongOscAdapter(ClipLauncherSlot clipLauncherSlot, int index, OscController oscController, String oscTarget, String oscDescription) {
    super(clipLauncherSlot, oscController, oscTarget, oscDescription);
    this.index = index;
    this.clipLauncherSlot = clipLauncherSlot;
    clipLauncherSlot.name().addValueObserver(name::set);
    clipLauncherSlot.color().addValueObserver(
            (r, g, b) -> color.set(Color.fromRGB(r, g, b))
    );
    clipLauncherSlot.isPlaying().addValueObserver(isPlaying::set);

  }

  class ObserverData<T> {
    private volatile T value;
    private final Runnable setParentDirty;
    private volatile boolean dirty;

    public ObserverData(T value, Runnable setParentDirty) {
      this.value = value;
      this.setParentDirty = setParentDirty;
      this.dirty = false;
    }

    public T get() {
      return value;
    }

    public void set(T value) {
      this.value = value;
      dirty = true;
      setParentDirty.run();
    }

    public boolean isDirty() {
      return dirty;
    }

    public void setDirty(boolean dirty) {
      this.dirty = dirty;
    }
  }

  public static String[] parseString(String input) {
    String[] results = {"", "", ""}; // Default to empty strings
    Pattern pattern = Pattern.compile("\\[(.*?)/(.*?)\\](.*)");
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
      results[0] = matcher.group(1).trim(); // Text between "[" and "/"
      results[1] = matcher.group(2).trim(); // Text between "/" and "]"
      results[2] = matcher.group(3).trim(); // Text after "]"
    }

    return results;
  }


    @Override
  public void flush() {
    if (dirty || forceFlush) {

      //TODO: Refactor this so its the actual exists...
      if (name.isDirty() || forceFlush) {
        name.setDirty(false);
        String[] strings = parseString(name.get());
        oscController.addMessageToQueue(oscPath + "/data", index, strings[0], strings[1], strings[2]);
      }

      if(color.isDirty() || forceFlush) {
        color.setDirty(false);
        oscController.addMessageToQueue(oscPath + "/color", index, (float) color.get().getRed(), (float) color.get().getGreen(), (float) color.get().getBlue());
      }

      dirty = false;
      forceFlush = false;
    }
  }
}
