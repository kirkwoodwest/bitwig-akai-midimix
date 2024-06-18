package com.kirkwoodwest.openwoods.flush;

public class ForceFlushQueue {
	private boolean queuedForceFlush = false;
	public boolean forceUpdate = false;

	public void forceNextFlush(){
		queuedForceFlush = true;
	}

	public boolean getForceFlush(){
		boolean forceFlush = queuedForceFlush;
		queuedForceFlush = false;
		return forceFlush;
	}
}
