package com.kirkwoodwest.openwoods.superbank;

import com.bitwig.extension.controller.api.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SuperBankClipLauncherSlots {
	private final ClipLauncherSlotBank clipLauncherSlotBank;
	private final HashMap<Clip, ArrayList<BooleanValue>> clipEqualsValues = new HashMap<>();

	public SuperBankClipLauncherSlots(Track track, int numScenes){
		clipLauncherSlotBank = track.clipLauncherSlotBank();

		int sizeOfBank = clipLauncherSlotBank.getSizeOfBank();
		for (int i = 0; i < sizeOfBank; i++) {
			clipLauncherSlotBank.getItemAt(i).name().markInterested();
		}
	}

	public void createEqualsValues(Clip clip) {
		if(clipEqualsValues.containsKey(clip)) return;
		ArrayList<BooleanValue> equalsValues = new ArrayList<>();
		int sizeOfBank = clipLauncherSlotBank.getSizeOfBank();

		for (int i = 0; i < sizeOfBank; i++) {
			ClipLauncherSlot slot = clipLauncherSlotBank.getItemAt(i);
			BooleanValue equalsValue = slot.createEqualsValue(clip.clipLauncherSlot());
			equalsValue.markInterested();
			equalsValues.add(equalsValue);
		}

		clipEqualsValues.put(clip, equalsValues);
	}

	public int getSlotIndex(Clip clip)  {
		ArrayList<BooleanValue> equalsValues = clipEqualsValues.get(clip);

		if(equalsValues==null) return -1;
		int size = equalsValues.size();
		for (int i = 0; i < size; i++) {
			boolean is_equals = equalsValues.get(i).get();
			if (is_equals) {
				return i;
			}
		}
		return -1;
	}

	public int getClip(Clip clip)  {
		ArrayList<BooleanValue> equalsValues = clipEqualsValues.get(clip);

		if(equalsValues==null) return -1;
		int size = equalsValues.size();
		for (int i = 0; i < size; i++) {
			boolean is_equals = equalsValues.get(i).get();
			if (is_equals) {
				return i;
			}
		}
		return -1;
	}

	public ClipLauncherSlot findByClipName(String clipName) {
		int sizeOfBank = clipLauncherSlotBank.getSizeOfBank();
		for (int i = 0; i < sizeOfBank; i++) {
			ClipLauncherSlot slot = clipLauncherSlotBank.getItemAt(i);
			String name = slot.name().get();
			if(name.equals(clipName)) {
				return slot;
			}
		}
		return null;
	}
}
