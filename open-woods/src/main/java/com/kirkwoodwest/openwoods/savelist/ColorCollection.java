package com.kirkwoodwest.openwoods.savelist;
import com.bitwig.extension.api.Color;
import java.util.ArrayList;
import java.util.List;

//Class for containing the colors for project remotes
public class ColorCollection {
  private final ArrayList<Color> colors;
  private Color color;

  public ColorCollection(int count){
    colors = new ArrayList<Color>(count);
    for (int i = 0; i < count; i++) {
      colors.add(Color.fromRGB255(0,0,0));
    }
  }

  public ArrayList<Color> getColors() {
    return new ArrayList<Color>(colors);
  }

  public void setColors(List<Color> colors) {
    this.colors.clear();
    this.colors.addAll(colors);
  }

  public Color getColor(int index ){
    return colors.get(index);
  }

  public void setColor(int index, Color color) {
    colors.set(index, color);
    this.color = color;
  }

  public void setTempColor(Color color) {
    this.color = color;
  }

  public Color getTempColor() {
    return color;
  }
}
