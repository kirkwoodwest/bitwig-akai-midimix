package com.kirkwoodwest.openwoods.superbank;

import com.bitwig.extension.controller.api.Device;
import com.bitwig.extension.controller.api.DeviceBank;
import com.bitwig.extension.controller.api.DrumPadBank;
import com.bitwig.extension.controller.api.Track;

import java.util.ArrayList;

public class SuperBankDevices {
	private final ArrayList<DrumPadBank> device_drum_pad_banks;
	int DRUMPAD_SCROLL_POSITION = 36;	//Locked at C-1
	int DRUM_PAD_BANK_SIZE = 16;	//16 Pads
	int NUM_DEVICES = 8;	// devices scan max

	DeviceBank device_bank;
	ArrayList<DrumPadBank> drum_pad_banks;

	public SuperBankDevices(Track track) {
		this.drum_pad_banks = new ArrayList<DrumPadBank>();
		this.device_bank = track.createDeviceBank(NUM_DEVICES);

		device_drum_pad_banks = new ArrayList<DrumPadBank>();
		for(int d=0;d<NUM_DEVICES;d++) {
			Device device = device_bank.getItemAt(d);
			device.hasDrumPads().markInterested();
			device.name().markInterested();
			device.deviceType().markInterested();

			DrumPadBank drum_pad_bank = device_bank.getItemAt(d).createDrumPadBank(16);
			device_drum_pad_banks.add(drum_pad_bank);
			drum_pad_bank.setIndication(true);

			//Force Scroll Position
			drum_pad_bank.scrollPosition().addValueObserver((position)->{
				if(drum_pad_bank.scrollPosition().get() != DRUMPAD_SCROLL_POSITION){
					drum_pad_bank.scrollPosition().set(DRUMPAD_SCROLL_POSITION);
				}
			} );

			drum_pad_bank.scrollPosition().set(DRUMPAD_SCROLL_POSITION);

			for(int e=0;e<DRUM_PAD_BANK_SIZE;e++){
				final int drum_pad = e;
				drum_pad_bank.getItemAt(e).exists().markInterested();
			}

			this.drum_pad_banks.add(drum_pad_bank);
		}
	}

	public ArrayList<Device> getDevices(){
		ArrayList<Device> devices = new ArrayList<>();
		for(int d=0;d<NUM_DEVICES;d++) {
			devices.add(device_bank.getItemAt(d));
		}
		return devices;
	}

	public boolean hasDrumPads() {
		for(int d=0;d<NUM_DEVICES;d++) {
			Device device = device_bank.getItemAt(d);
			if(device.hasDrumPads().get()) {
				return true;
			};
		}
		return false;
	}

	public ArrayList<Integer> getDrumPadNotes(){
		ArrayList<Integer> drum_pad_notes = new ArrayList<>();
		for(int d=0;d<NUM_DEVICES;d++) {
			Device device = device_bank.getItemAt(d);
			if(device.hasDrumPads().get()){
				DrumPadBank drum_pad_bank= device_drum_pad_banks.get(d);
				for(int i=0;i<DRUM_PAD_BANK_SIZE;i++){
					boolean exists = drum_pad_bank.getItemAt(i).exists().get();
					int note = i + DRUMPAD_SCROLL_POSITION;
					if (exists) drum_pad_notes.add(note);
				}
				return drum_pad_notes;
			}
		}
		return drum_pad_notes;
	}
}
