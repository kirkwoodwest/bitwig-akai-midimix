package com.kirkwoodwest.hardware.MFT;

import com.bitwig.extension.api.Color;
import com.kirkwoodwest.utils.ColorUtil;

public class MFTColorUtils {
  static public double[][] color_map_double = {
          new double[]{0.0, 0.0, 0.0},
          new double[]{0.0, 0.0, 1.0},
          new double[]{0.0, 0.08235294, 1.0},
          new double[]{0.0, 0.13333334, 1.0},
          new double[]{0.0, 0.18039216, 1.0},
          new double[]{0.0, 0.23137255, 1.0},
          new double[]{0.0, 0.26666668, 1.0},
          new double[]{0.0, 0.3137255, 1.0},
          new double[]{0.0, 0.3647059, 1.0},
          new double[]{0.0, 0.41568628, 1.0},
          new double[]{0.0, 0.46666667, 1.0},
          new double[]{0.0, 0.49803922, 1.0},
          new double[]{0.0, 0.54901963, 1.0},
          new double[]{0.0, 0.6, 1.0},
          new double[]{0.0, 0.64705884, 1.0},
          new double[]{0.0, 0.69803923, 1.0},
          new double[]{0.0, 0.7490196, 1.0},
          new double[]{0.0, 0.78039217, 1.0},
          new double[]{0.0, 0.83137256, 1.0},
          new double[]{0.0, 0.88235295, 1.0},
          new double[]{0.0, 0.93333334, 1.0},
          new double[]{0.0, 0.98039216, 1.0},
          new double[]{0.0, 1.0, 0.98039216},
          new double[]{0.0, 1.0, 0.92941177},
          new double[]{0.0, 1.0, 0.88235295},
          new double[]{0.0, 1.0, 0.83137256},
          new double[]{0.0, 1.0, 0.78039217},
          new double[]{0.0, 1.0, 0.7490196},
          new double[]{0.0, 1.0, 0.69803923},
          new double[]{0.0, 1.0, 0.64705884},
          new double[]{0.0, 1.0, 0.6},
          new double[]{0.0, 1.0, 0.54901963},
          new double[]{0.0, 1.0, 0.49803922},
          new double[]{0.0, 1.0, 0.46666667},
          new double[]{0.0, 1.0, 0.41568628},
          new double[]{0.0, 1.0, 0.3647059},
          new double[]{0.0, 1.0, 0.3137255},
          new double[]{0.0, 1.0, 0.2627451},
          new double[]{0.0, 1.0, 0.23137255},
          new double[]{0.0, 1.0, 0.18039216},
          new double[]{0.0, 1.0, 0.12941177},
          new double[]{0.0, 1.0, 0.08235294},
          new double[]{0.0, 1.0, 0.03137255},
          new double[]{0.0, 1.0, 0.0},
          new double[]{0.047058824, 1.0, 0.0},
          new double[]{0.09803922, 1.0, 0.0},
          new double[]{0.14901961, 1.0, 0.0},
          new double[]{0.2, 1.0, 0.0},
          new double[]{0.24705882, 1.0, 0.0},
          new double[]{0.28235295, 1.0, 0.0},
          new double[]{0.32941177, 1.0, 0.0},
          new double[]{0.38039216, 1.0, 0.0},
          new double[]{0.43137255, 1.0, 0.0},
          new double[]{0.48235294, 1.0, 0.0},
          new double[]{0.5137255, 1.0, 0.0},
          new double[]{0.5647059, 1.0, 0.0},
          new double[]{0.6156863, 1.0, 0.0},
          new double[]{0.6666667, 1.0, 0.0},
          new double[]{0.7137255, 1.0, 0.0},
          new double[]{0.7490196, 1.0, 0.0},
          new double[]{0.79607844, 1.0, 0.0},
          new double[]{0.84705883, 1.0, 0.0},
          new double[]{0.8980392, 1.0, 0.0},
          new double[]{0.9490196, 1.0, 0.0},
          new double[]{1.0, 1.0, 0.0},
          new double[]{1.0, 0.9647059, 0.0},
          new double[]{1.0, 0.9137255, 0.0},
          new double[]{1.0, 0.8627451, 0.0},
          new double[]{1.0, 0.8156863, 0.0},
          new double[]{1.0, 0.7647059, 0.0},
          new double[]{1.0, 0.73333335, 0.0},
          new double[]{1.0, 0.68235296, 0.0},
          new double[]{1.0, 0.6313726, 0.0},
          new double[]{1.0, 0.5803922, 0.0},
          new double[]{1.0, 0.5294118, 0.0},
          new double[]{1.0, 0.49803922, 0.0},
          new double[]{1.0, 0.44705883, 0.0},
          new double[]{1.0, 0.4, 0.0},
          new double[]{1.0, 0.34901962, 0.0},
          new double[]{1.0, 0.29803923, 0.0},
          new double[]{1.0, 0.24705882, 0.0},
          new double[]{1.0, 0.21568628, 0.0},
          new double[]{1.0, 0.16470589, 0.0},
          new double[]{1.0, 0.11372549, 0.0},
          new double[]{1.0, 0.0627451, 0.0},
          new double[]{1.0, 0.015686275, 0.0},
          new double[]{1.0, 0.0, 0.015686275},
          new double[]{1.0, 0.0, 0.0627451},
          new double[]{1.0, 0.0, 0.11372549},
          new double[]{1.0, 0.0, 0.16470589},
          new double[]{1.0, 0.0, 0.21568628},
          new double[]{1.0, 0.0, 0.24705882},
          new double[]{1.0, 0.0, 0.29803923},
          new double[]{1.0, 0.0, 0.34901962},
          new double[]{1.0, 0.0, 0.4},
          new double[]{1.0, 0.0, 0.44705883},
          new double[]{1.0, 0.0, 0.49803922},
          new double[]{1.0, 0.0, 0.5294118},
          new double[]{1.0, 0.0, 0.5803922},
          new double[]{1.0, 0.0, 0.6313726},
          new double[]{1.0, 0.0, 0.68235296},
          new double[]{1.0, 0.0, 0.7294118},
          new double[]{1.0, 0.0, 0.7647059},
          new double[]{1.0, 0.0, 0.8156863},
          new double[]{1.0, 0.0, 0.8666667},
          new double[]{1.0, 0.0, 0.9137255},
          new double[]{1.0, 0.0, 0.9647059},
          new double[]{1.0, 0.0, 1.0},
          new double[]{0.9490196, 0.0, 1.0},
          new double[]{0.8980392, 0.0, 1.0},
          new double[]{0.84705883, 0.0, 1.0},
          new double[]{0.8, 0.0, 1.0},
          new double[]{0.7490196, 0.0, 1.0},
          new double[]{0.7137255, 0.0, 1.0},
          new double[]{0.6627451, 0.0, 1.0},
          new double[]{0.6156863, 0.0, 1.0},
          new double[]{0.5647059, 0.0, 1.0},
          new double[]{0.5137255, 0.0, 1.0},
          new double[]{0.48235294, 0.0, 1.0},
          new double[]{0.43137255, 0.0, 1.0},
          new double[]{0.38039216, 0.0, 1.0},
          new double[]{0.33333334, 0.0, 1.0},
          new double[]{0.28235295, 0.0, 1.0},
          new double[]{0.24705882, 0.0, 1.0},
          new double[]{0.19607843, 0.0, 1.0},
          new double[]{0.14901961, 0.0, 1.0},
          new double[]{0.09803922, 0.0, 1.0},
          new double[]{0.0, 0.0, 0.0},
  };

  public static Color intToColor(int hue){
    double[] colorDouble = color_map_double[hue];
    double r = colorDouble[0];
    double g = colorDouble[1];
    double b = colorDouble[2];
    return Color.fromRGB(r,g,b);
  }

  static public int colorToInt(Color color) {
    return rgbToInt(color.getRed(), color.getGreen(), color.getBlue());
  }

  static public int rgbToInt(double R, double G, double B) {
    //Get distance from RGB
    double distance = 900.0f;
    int shortest_distance_index = 0;
    int size = color_map_double.length;
    for(int i=0; i < size;i++) {
      double r2 = color_map_double[i][0];
      double g2 = color_map_double[i][1];
      double b2 = color_map_double[i][2];

      double new_distance = ColorUtil.doDistance(r2, g2, b2, R, G, B);
      if (Double.compare(new_distance, distance) < 0) {
        shortest_distance_index = i;
        distance = new_distance;
      }
    }
    int color_index = shortest_distance_index;
    return color_index;
  }

  public static int colorBlack() {
    return 0;
  }
}
