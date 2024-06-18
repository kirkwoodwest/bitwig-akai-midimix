package com.kirkwoodwest.openwoods.oscthings.launcher;

public class OscClipLauncherPaths {

  private final String basePath;
  private final String TRACK_BASE = "/track/";
  private final String CLIP_BASE = "/";
  private final String LAUNCHER_BASE = "/launcher";

  public OscClipLauncherPaths(String path) {
    this.basePath = path;
  }

  private String getTrack(int i) {
    return basePath + TRACK_BASE + (i + 1) + LAUNCHER_BASE;
  }
  
  private String getClip(int i) {
    return CLIP_BASE + (i + 1);
  }
  
  public String getSceneName(int i) {
    return basePath + "/scene/" + i + "/name";
  }

  public String getSceneColor(int i) {
    return basePath + "/scene/" + i + "/color";
  }

  public String getSceneLaunch(int i) {
    return basePath + "/scene/" + i + "/launch";
  }

  public String getSceneLaunchRelease(int i) {
    return basePath + "/scene/" + i + "/launch_release";
  }

  public String getSceneLaunchAlt(int i) {
    return basePath + "/scene/" + i + "/launch_alt";
  }

  public String getSceneLaunchAltRelease(int i) {
    return basePath + "/scene/" + i + "/launch_alt_release";
  }

  public String getClipName(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/name";
  }

  public String getClipColor(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/color";
  }

  public String getClipIsPlaying(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/playing";
  }

  public String getClipIsRecording(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/recording";
  }

  public String getClipIsRecordingQueued(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/recording_queued";
  }

  public String getClipIsStopQueued(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/stop_queued";
  }

  public String getClipIsPlaybackQueued(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/playback_queued";
  }

  public String getClipExists(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/exists";
  }
  public String getClipHasContent(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/exists";
  }

  public String getClipLaunch(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/launch";
  }

  public String getClipLaunchRelease(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/launch_release";
  }

  public String getClipLaunchAlt(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/launch_alt";
  }

  public String getClipLaunchAltRelease(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/launch_alt_release";
  }

  public String getClipLaunchWithOptions(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/launch_with_options";
  }

  public String getClipLaunchLastWithOptions(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/launch_alt_with_options";
  }

  public String getClipDuplicate(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/clip/duplicate";
  }

  public String getClipCreateEmpty(int trackIndex, int slotIndex) {
    return getTrack(trackIndex) + getClip(slotIndex) + "/clip/create";
  }

  public String getTrackStop(int trackIndex) {
    return getTrack(trackIndex) + "/stop";
  }
  
  public String getTrackStopAlt(int trackIndex) {
    return getTrack(trackIndex) + "/stopAlt";
  }

  public String getIsStopQueued(int trackIndex) {
    return getTrack(trackIndex) + "/is_stop_queued";
  }

  public String getIsStopped(int trackIndex) {
    return getTrack(trackIndex) + "/is_stopped";
  }

  public String getTrackIndex() {
    return basePath +  "/*/" + LAUNCHER_BASE;
  }

  public String getClipIndex() {
    return basePath + getTrackIndex() + "/" + CLIP_BASE + "*";
  }
}
