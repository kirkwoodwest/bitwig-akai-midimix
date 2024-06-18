package com.kirkwoodwest.openwoods.oscthings.launcher;

public class ClipStatus {
	private boolean is_playing = false;
	private float r;
	private float g;
	private float b;

	public ClipStatus(){

	}


	public void setIsPlaying(boolean is_playing){
		this.is_playing = is_playing;
	}

	public boolean getIsPlaying(){
		return is_playing;
	}

	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public float[] getColor() {
		return new float[]{this.r, this.g, this.b};
	}
}
