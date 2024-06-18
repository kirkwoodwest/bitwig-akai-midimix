package com.kirkwoodwest.openwoods.oscthings.browser;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.stream.IntStream;

public class BrowserResultsBank {
  private final BrowserResultsItem cursorItem;
  private BrowserResultsItemBank bank;
  private String id;

  public BrowserResultsBank(OscController oscController, OscBrowserPaths path, BrowserResultsColumn resultsColumn, int size){
    this.id = id;
    bank = resultsColumn.createItemBank(size);
    cursorItem = resultsColumn.createCursorItem();
    cursorItem.isSelected().markInterested();


    IntStream.range(0, size).forEach(i -> {
      BrowserResultsItem resultsItem = bank.getItemAt(i);
      final int         index          = i;
      final int         oscIndex          = i + 1;

      resultsItem.name().markInterested();//.addValueObserver((s)->this.reportFilterItemName(index, s));
      resultsItem.exists().markInterested(); //.addValueObserver((b)->this.setItemExists(index, b));
      resultsItem.isSelected().markInterested();


      oscController.addStringValue(OscBrowser.ID, path.getResultsName(oscIndex), resultsItem.name());
      oscController.addBooleanValue(OscBrowser.ID, path.getResultsExists(oscIndex), resultsItem.exists());
      oscController.addSettableBooleanValue(OscBrowser.ID, path.getResultsSelected(oscIndex), resultsItem.isSelected(), "Selected State of Browser Result");
    });

    //Navigation
    oscController.registerOscCallback(path.getResultsScroll() + "/scroll", "Scroll by X amount", this::scrollBy);
  }

  private void scrollBy(OscConnection oscConnection, OscMessage oscMessage) {
    int amountToScroll = oscMessage.getInt(0);
    bank.scrollBy(amountToScroll);
  }

}
