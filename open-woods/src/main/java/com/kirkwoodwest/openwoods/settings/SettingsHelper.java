package com.kirkwoodwest.openwoods.settings;

import com.bitwig.extension.controller.api.*;

public class SettingsHelper {
	public static void setVisible(Setting setting, boolean visible){
		if(visible){
			setting.show();
		} else {
			setting.hide();
		}
	}

	public static void setVisible(SettableEnumValue setting, boolean visible){
		setVisible((Setting) setting, visible);
	}
	public static void setVisible(SettableStringValue setting, boolean visible){
		setVisible((Setting) setting, visible);
	}
	public static void setVisible(Signal setting, boolean visible){
		setVisible((Setting) setting, visible);
	}

	public static void setEnabled(Setting setting, boolean enabled){
		if (enabled ) {
			setting.enable();
		} else {
			setting.disable();
		}
	}
	public static void setEnabled(SettableEnumValue setting, boolean enabled){
		setEnabled((Setting) setting, enabled);
	}
	public static void setEnabled(SettableStringValue setting, boolean enabled){
		setEnabled((Setting) setting, enabled);
	}
	public static void setEnabled(Signal setting, boolean enabled){
		setEnabled((Setting) setting, enabled);
	}

	public static EnumSetting createEnumSetting(DocumentState documentState, String[] modes, EnumSetting.ModeHandler customHandler) {
		return new EnumSetting(documentState, modes, customHandler);
	}
	public static EnumSetting createEnumSetting(Preferences preferences, String[] modes, EnumSetting.ModeHandler customHandler) {
		return new EnumSetting(preferences, modes, customHandler);
	}

}
