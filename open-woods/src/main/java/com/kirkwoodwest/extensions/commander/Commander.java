package com.kirkwoodwest.extensions.commander;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.extensions.commander.commands.CommandExecutor;
import com.kirkwoodwest.openwoods.superbank.SuperBank;
import com.kirkwoodwest.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class Commander {
  private final SuperBank superBank;
  private final SceneBank sceneBank;
  private final CursorTrack cursorTrack;
  private final CommandExecutor commandExecutor;

  public Commander(ControllerHost host) {
    commandExecutor = new CommandExecutor(this);

    int NUM_SCENES = 128;

    //Bitwig Objects
    superBank = new SuperBank(host, 64, 0, 0);
    cursorTrack = host.createCursorTrack(0, NUM_SCENES);
    sceneBank = host.createSceneBank(NUM_SCENES);
    ClipLauncherSlotBank clipLauncherSlotBank = cursorTrack.clipLauncherSlotBank();
    cursorTrack.name().markInterested();

    //Not used yet.
    PinnableCursorClip launcherCursorClip = cursorTrack.createLauncherCursorClip(16, 128);

    //--------------------------------------------------------------------------------
    // Lock Commander
    {
      //Lock to Commander Track Info
      SettableStringValue lockInfo = host.getDocumentState().getStringSetting("Commander Track", "Config", 24, "Commander Track");
      lockInfo.markInterested();
      cursorTrack.name().addValueObserver(lockInfo::set);

      //Signal create new track at index 0.
      Signal signal = host.getDocumentState().getSignalSetting("Lock to Commander Track", "Config", "Lock to Commander Track");
      signal.addSignalObserver(() -> {
        superBank.setCursorTrackByName(cursorTrack, "Commander");
      });


    }

    for (int i = 0; i < clipLauncherSlotBank.getSizeOfBank(); i++) {
      final int bankIndex = i;
      ClipLauncherSlot slot = clipLauncherSlotBank.getItemAt(i);
      slot.name().markInterested();
    }

    clipLauncherSlotBank.addIsPlayingObserver((index, isPlaying) -> {
      if (isPlaying) {
        doCommand(clipLauncherSlotBank.getItemAt(index).name().get());
      }
    });

  }

  public void doCommand(String name) {
    name = name.toLowerCase().trim();
    List<CommandItem> commandItems = new ArrayList<>();
    String[] split = name.split(";");

    for (String s : split) {
      s = s.trim();
      if (s.startsWith("/")) {
        CommandItem commandItem = new CommandItem(s);
        commandItems.add(commandItem);
        LogUtil.print("Commander: " + s);
      }
    }
    processCommands(commandItems);
  }

  private void processCommands(List<CommandItem> commands) {
    for (int i = 0; i < commands.size(); i++) {
      CommandItem commandItem = commands.get(i);
      String command = commandItem.getCommand();
      List<String> args = commandItem.getArgs();
      commandExecutor.execute(command, args);
    }
  }



  public void selectTrackInEditor(int index){
    superBank.getItemAt(index).selectInEditor();
  }

  public void selectSceneInEditor(int index){
    sceneBank.getItemAt(index).selectInEditor();
  }


  public void trackArm(int index, boolean isArmed) {
    superBank.getItemAt(index).arm().set(isArmed);
  }

  public void trackMute(int index, boolean b) {
    superBank.getItemAt(index).mute().set(b);
  }
  public void trackMuteToggle(int index) {
    superBank.getItemAt(index).mute().toggle();
  }

  public void trackSolo(int index, boolean b) {
    superBank.getItemAt(index).solo().set(b);
  }

  public void trackSoloToggle(int index) {
    superBank.getItemAt(index).solo().toggle();
  }
}
