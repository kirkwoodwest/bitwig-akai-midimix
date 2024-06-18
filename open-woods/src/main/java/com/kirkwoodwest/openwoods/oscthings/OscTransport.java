
package com.kirkwoodwest.openwoods.oscthings;


import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.closedwoods.loader.element.ILoaderElement;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.oscthings.adapters.BeatTimeValueOscAdapter;

public class OscTransport implements ILoaderElement  {
	public final String ID = "transport";
	private final ControllerHost host;
	private final OscController oscController;
	private final Transport transport;
	private final SettableBooleanValue settingTransport;
	private final OscTransportPaths oscPath;
	//private final LedOscDouble transportLed;

	public OscTransport(ControllerHost host, OscController oscController, String oscPath) {
		this.host = host;
		this.oscController = oscController;
		this.oscPath = new OscTransportPaths(oscPath);
		settingTransport = this.host.getPreferences().getBooleanSetting("OSC Transport", "OSC Data", true);
		settingTransport.addValueObserver(this::enable);

		transport = host.createTransport();
		transport.getPosition().markInterested();

		//Register Transport Controls

		//Play
		oscController.registerOscCallback(this.oscPath.play, "Play", (oscConnection,oscMessage)->transport.play());
		//Stop
		oscController.registerOscCallback(this.oscPath.stop, "Stop", (oscConnection,oscMessage)->transport.stop());
		//Continue Playback
		oscController.registerOscCallback(this.oscPath.continuePlayback, "Continue Playback", (oscConnection,oscMessage)->transport.continuePlayback());
		//Toggle Play
		oscController.registerOscCallback(this.oscPath.togglePlay, "Toggle Play", (oscConnection,oscMessage)->transport.togglePlay());
		//Restart
		oscController.registerOscCallback(this.oscPath.restart, "Restart", (oscConnection,oscMessage)->transport.restart());
		//Record
		oscController.registerOscCallback(this.oscPath.record, "Record", (oscConnection,oscMessage)->transport.record());
		//Rewind
		oscController.registerOscCallback(this.oscPath.rewind, "Rewind", (oscConnection,oscMessage)->transport.rewind());
		//Fast Forward
		oscController.registerOscCallback(this.oscPath.fastForward, "Fast Forward", (oscConnection,oscMessage)->transport.fastForward());
		//Tap Tempo
		oscController.registerOscCallback(this.oscPath.tapTempo, "Tap Tempo", (oscConnection,oscMessage)->transport.tapTempo());




		//Automation Overrides
		oscController.addBooleanValue(ID, this.oscPath.automationOverrideActive, transport.isAutomationOverrideActive());
		oscController.registerOscCallback(this.oscPath.resetAutomationOverride, "Reset Automation Override", (oscConnection,oscMessage)->transport.resetAutomationOverrides());


		//Is Playing
		oscController.addBooleanValue(ID, this.oscPath.isFillModeActive, transport.isPlaying());
		//Is Fill Mode Active
		oscController.addBooleanValue(ID, this.oscPath.isFillModeActive, transport.isFillModeActive());

		/**
     * Completed
     * 						 * void play()
     * 						 * void continuePlayback()
     * 					 * void stop()
     * 					 			 * void togglePlay()
		 * void restart()
		 * void record()
		 * void rewind()
		 * void fastForward()
		 * void tapTempo()
		 *      * SettableBooleanValue isPlaying()
     *
     *

    /**




     * SettableBooleanValue isArrangerRecordEnabled()
     * SettableBooleanValue isArrangerOverdubEnabled()
     * SettableBooleanValue isClipLauncherOverdubEnabled()
     * SettableEnumValue automationWriteMode()
     * SettableBooleanValue isArrangerAutomationWriteEnabled()
     * SettableBooleanValue isClipLauncherAutomationWriteEnabled()
     * BooleanValue isAutomationOverrideActive()
     * SettableBooleanValue isArrangerLoopEnabled()
     * SettableBeatTimeValue arrangerLoopStart()
     * SettableBeatTimeValue arrangerLoopDuration()
     * SettableBooleanValue isPunchInEnabled()
     * SettableBooleanValue isPunchOutEnabled()
     * SettableBooleanValue isMetronomeEnabled()
     * SettableBooleanValue isMetronomeTickPlaybackEnabled()
     * SettableRangedValue metronomeVolume()
     * SettableBooleanValue isMetronomeAudibleDuringPreRoll()
     * SettableEnumValue preRoll()
     * void toggleLatchAutomationWriteMode()
     * void toggleWriteArrangerAutomation()
     * void toggleWriteClipLauncherAutomation()
     * void resetAutomationOverrides()
     * void returnToArrangement()
     * Parameter tempo()
     * void increaseTempo(Number amount, Number range)
     * SettableBeatTimeValue getPosition()
     * BeatTimeValue playPosition()
     * DoubleValue playPositionInSeconds()
     * SettableBeatTimeValue playStartPosition()
     * SettableDoubleValue playStartPositionInSeconds()
     * void launchFromPlayStartPosition()
     * HardwareActionBindable launchFromPlayStartPositionAction()
     * void jumpToPlayStartPosition()
     * HardwareActionBindable jumpToPlayStartPositionAction()
     * void jumpToPreviousCueMarker()
     * HardwareActionBindable jumpToPreviousCueMarkerAction()
     * void jumpToNextCueMarker()
     * HardwareActionBindable jumpToNextCueMarkerAction()
     * void setPosition(double beats)
     * void incPosition(double beats, boolean snap)
     * SettableBeatTimeValue getInPosition()
     * SettableBeatTimeValue getOutPosition()
     * void addCueMarkerAtPlaybackPosition()
     * HardwareActionBindable addCueMarkerAtPlaybackPositionAction()
     * Parameter crossfade()
     * TimeSignatureValue timeSignature()
     * SettableEnumValue clipLauncherPostRecordingAction()
     * SettableBeatTimeValue getClipLauncherPostRecordingTimeOffset()
     * SettableEnumValue defaultLaunchQuantization()
     * SettableBooleanValue
     *
     */


		//transport.addCueMarkerAtPlaybackPosition();
		//transport.arrangerLoopDuration();



		//Beat Time Value Adapter
		BeatTimeValueOscAdapter beatTimeValueOscAdapter = new BeatTimeValueOscAdapter(transport.getPosition(), transport, oscController, oscPath + "/position", "beat_time");
		oscController.addAdapter(ID, beatTimeValueOscAdapter);

		//Position in Seconds
		oscController.addDoubleValue(ID, this.oscPath.positionTime, transport.playPositionInSeconds());

		//Tempo
		transport.tempo().value().addValueObserver((v)->{
			host.println("Tempo: " + v);
		});


		//TODO: Add transport Value Observers
		/**
		transport.getPosition().addValueObserver();
		//Transport Leds
		{
			//bars
			Supplier<Integer> supplier = () -> {
				String   pos       = transport.getPosition().getFormatted();// time: 001:02:04:62 BARS:BEATS:SUB_BEATS:TICKS
				String[] splitPos = pos.split(":");
				int      bars      = Integer.parseInt(splitPos[0]);
				return bars;
			};
			oscController.addIntegerValue("transport", this.oscPath.bars, supplier);
		}

		{
			//beats
			Supplier<Integer> supplier = () -> {
				String   pos       = transport.getPosition().getFormatted();// time: 001:02:04:62 BARS:BEATS:SUB_BEATS:TICKS
				String[] splitPos = pos.split(":");
				int      beats     = Integer.parseInt(splitPos[1]);
				return beats;
			};
			oscController.addLedInteger("transport", this.oscPath.beats, supplier);
		}

		{
			//sub beats
			Supplier<Integer> supplier = () -> {
				String   pos       = transport.getPosition().getFormatted();// time: 001:02:04:62 BARS:BEATS:SUB_BEATS:TICKS
				String[] splitPos = pos.split(":");
				int      subBeats = Integer.parseInt(splitPos[2]);
				return subBeats;
			};
			oscController.addLedInteger("transport", this.oscPath.subBeats, supplier);
		}

		{
			//tempo
			Supplier<Double> supplier = () -> {
				double   tempo     = transport.tempo().getRaw();
				return tempo;
			};
			oscController.addLedDouble("transport", this.oscPath.tempo, supplier);
		}

		{
			//time
			Supplier<Double> supplier = () -> {
				double   time      = transport.playPositionInSeconds().get();
				return time;
			};

			transportLed = oscController.addLedDouble("transport", this.oscPath.time, supplier);
		}
		 */
	}

	private void throttlePlayPosition(int ticks) {
		//TODO: This should throttle the position so we don't get a spam of updates...
	//	transportLed.throttleUpdates(ticks);
	}

	@Override
	public void refresh() {
		oscController.forceNextFlush(ID);
	}

	public void enable(boolean b) {
		oscController.setGroupEnabled(ID, b);
	}

}
