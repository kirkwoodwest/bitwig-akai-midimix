package com.kirkwoodwest.extensions.usercontrolsosc.interfaces;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class OscVUMeterBank {
	private final TrackBank trackBank;
	private boolean peak_enabled;
	private boolean rms_enabled;

	public OscVUMeterBank(ControllerHost host, OscHost oscHost, TrackBank trackBank, int numTracks, boolean vu_meters_enabled, int range, boolean  peak_enabled, boolean rms_enabled) {
		if(trackBank == null) {
			this.trackBank = host.createTrackBank(numTracks,0,1,true);
		} else {
			this.trackBank = trackBank;
		}
//		this.trackBank = null;

		for (int i = 0; i < numTracks; i++) {
			Track        channel = this.trackBank.getItemAt(i);
			final String vu_base = "/track/" + (i + 1) + "/vu";
			final int xx = i;
			channel.name().addValueObserver((name) -> {
				host.println("Trackname: " + xx + ": " + name);
			});
			if (peak_enabled) {
				//VU Meter  Peak
				final String target_peak = vu_base + "/peak";
				boolean      is_peak     = true;
				channel.addVuMeterObserver(1023, -1, is_peak, (vu_level) -> {
					if (vu_meters_enabled) {
						//osc_handler.addMessageToQueue(target_peak, (int) vu_level);
						if(peak_enabled) oscHost.sendMessage(target_peak, (int) vu_level);
					}
				});
			}
			if (rms_enabled) {
				//VU Meter RMS
				final String target_rms = vu_base + "/rms";
				boolean      is_peak    = false;
				channel.addVuMeterObserver(1023, -1, is_peak, (vu_level) -> {
					if (vu_meters_enabled) {
						if(rms_enabled) {
							//	osc_handler.addMessageToQueue(target_rms, (int) vu_level);
							oscHost.sendMessage(target_rms, (int) vu_level);
						}
					}
				});
			}
		}
	}

	public void setPeakOutput(boolean b) {
		peak_enabled = b;
	}

	public void setRmsOutput(boolean b) {
		rms_enabled = b;
	}

	public TrackBank getTrackBank() {
		return trackBank;
	}
}
