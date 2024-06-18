package com.kirkwoodwest.closedwoods.trackmaster.multirecord;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.Track;
import com.kirkwoodwest.openwoods.superbank.SuperBank;

import static com.kirkwoodwest.utils.MathUtil.padInteger;

public class MultiRecordSlot {

	private final String id;
	private int trackCount;
	private int firstTrackIndex;

	private SuperBank superBank;
	public MultiRecordSlot(SuperBank superBank, String id, int trackCount, int selectedTrack) {
		this.superBank = superBank;
		this.id = id;
		this.trackCount = trackCount;
		this.firstTrackIndex = selectedTrack;
	}

	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	public void setFirstTrackIndex(int trackIndex) {
		firstTrackIndex = trackIndex;
	}
	public String getFirstTrackName() {
		return superBank.getItemAt(firstTrackIndex).name().get();
	}

	public String getLastTrackName() {
		return superBank.getItemAt(firstTrackIndex + trackCount - 1).name().get();
	}

	public Track[] getTracks() {
		int startIndex = firstTrackIndex;
		Track[] tracks = new Track[trackCount];
		for (int i = 0; i < trackCount; i++) {
			tracks[i] = superBank.getItemAt(startIndex + i);
		}

		return tracks;
	}

	public boolean getArm() {
		Track[] tracks = this.getTracks();
		int count = 0;
		for (Track track : tracks) {
			if (track.arm().get()) {
				count++;
			}
		}
		return count > 1;
	}

	public boolean getActivated() {
		Track[] tracks = this.getTracks();
		int count = 0;
		for (Track track : tracks) {
			if (track.isActivated().get()) {
				count++;
			}
		}
		return count > 1;
	}

	private Track getFirstTrack() {
		return superBank.getItemAt(firstTrackIndex);
	}

	public void setArm(boolean b) {
		Track[] tracks = this.getTracks();
		for (int i = 0; i < tracks.length; i++) {
			tracks[i].arm().set(b);
		}
	}

	public void setActivated(boolean b) {
		Track[] tracks = this.getTracks();
		for (int i = 0; i < tracks.length; i++) {
			tracks[i].isActivated().set(b);
		}
	}

	public String[] getNames(Track[] tracks) {
		String[] names = new String[tracks.length];
		for (int i = 0; i < tracks.length; i++) {
			names[i] = tracks[i].name().get();
		}
		return names;
	}

	public void setNames(String[] names, boolean useTrackNumber) {
		Track[] tracks = this.getTracks();
		for (int i = 0; i < tracks.length; i++) {
			String name = "" + padInteger(i, 2) + " " + names[i];
			tracks[i].name().set(name);
		}
	}

	public boolean[] getActives(Track[] tracks) {
		boolean[] actives = new boolean[tracks.length];
		for (int i = 0; i < tracks.length; i++) {
			actives[i] = tracks[i].isActivated().get();
		}
		return actives;
	}

	public void setActives(boolean[] actives) {
		Track[] tracks = this.getTracks();
		for (int i = 0; i < tracks.length; i++) {
			tracks[i].isActivated().set(actives[i]);
		}
	}

	public Color[] getColors(Track[] tracks) {
		Color[] color = new Color[tracks.length];
		for (int i = 0; i < tracks.length; i++) {
			color[i] = tracks[i].color().get();
		}
		return color;
	}

	public void setColors(Color[] color) {
		Track[] tracks = this.getTracks();
		for (int i = 0; i < tracks.length; i++) {
			tracks[i].color().set(color[i]);
		}
	}

}
