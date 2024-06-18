package com.kirkwoodwest.openwoods.oscthings;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.function.Supplier;

public class OscCursorRemoteControlsPage {

  public OscCursorRemoteControlsPage(CursorRemoteControlsPage page, OscController oscController, String oscPath, String groupID) {

    page.selectedPageIndex().markInterested();
    page.pageCount().markInterested();

    {
      //Led for Page Position in the bank 1/4 page/totalPages
      String target = oscPath + "/current_page";
      Supplier<String> supplier = () -> {
        int selectedPage = page.selectedPageIndex().get();
        int numItems = page.pageCount().get();
        return (selectedPage + 1) + "/" + numItems;
      };
      //TODO: Fix this...
      // oscController.addStringValue(groupID, target, supplier);
    }
    {
      //Device Bank Page Left
      String target = oscPath + "/nav_previous";
      oscController.registerOscCallback(target, "Device Bank Page Left", (oscConnection, oscMessage) -> {
        page.selectPrevious();
        oscController.forceNextFlush(groupID);
      });
    }
    {
      //Device Bank Page Right
      String target = oscPath + "/nav_next";
      oscController.registerOscCallback(target, "Device Bank Page Right", (oscConnection, oscMessage) -> {
        page.selectNext();
        oscController.forceNextFlush(groupID);
      });
    }
  }
}
