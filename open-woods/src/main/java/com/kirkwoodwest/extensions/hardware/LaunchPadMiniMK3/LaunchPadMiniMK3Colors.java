package com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3;

import com.bitwig.extension.api.Color;

import java.util.List;

import static com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3.ColorMap.createColorMap;

public class LaunchPadMiniMK3Colors {
	static private final List<Color> MAP = createColorMap();
	static public int getBlack(){
		return 0;
	}

	static public List<Color> getMap(){
		return MAP;
	}
}
