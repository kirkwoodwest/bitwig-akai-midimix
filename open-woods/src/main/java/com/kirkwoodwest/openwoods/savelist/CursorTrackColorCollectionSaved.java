package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;

import java.io.Flushable;
import java.util.ArrayList;

public class CursorTrackColorCollectionSaved extends CursorTrackColorCollection implements Flushable {

  private final SaveListData<Double> saveData;

  public CursorTrackColorCollectionSaved(ControllerHost host, String name, CursorTrack cursorTrack, int colorCount) {
    super(cursorTrack, colorCount);
    saveData = new SaveListData<>(host, name, this::getColorsSerialized, this::setColorsSerialized, Double.class);
    saveData.hideSetting();
  }

  private void setColorsSerialized(ArrayList<Double> colors) {
    //build new colors from the list of doubles
    ArrayList<Color> newColors = new ArrayList<>();

    //Every 3 Indexes is a color
    for (int i = 0; i < colors.size(); i += 3) {
      //get doubles and make new color
      newColors.add(Color.fromRGB(colors.get(i), colors.get(i + 1), colors.get(i + 2)));
    }

    super.setColors(newColors);
    saveData.saveNextFlush();
  }

  public ArrayList<Double> getColorsSerialized() {
    //build new colors from the list of doubles
    ArrayList<Double> newColors = new ArrayList<>();
    for (Color color : super.getColors()) {
      newColors.add(color.getRed());
      newColors.add(color.getGreen());
      newColors.add(color.getBlue());
    }
    return newColors;
  }

  @Override
  public void setColor(int index, Color color) {
    super.setColor(index, color);
    saveData.saveNextFlush();
  }

  @Override
  public void flush() {
    saveData.flush();
  }

}
