package com.kirkwoodwest.openwoods.oscthings.browser;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.osc.OscController;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class BrowserFilterBank  {

  private final BrowserFilterItemBank bank;
  private final ArrayList<BrowserFilterItem> browserFilterItems = new ArrayList<>();

  private final BrowserFilterItem wildcardItem;
  private ControllerHost host;
  private final OscController oscController;
  private final OscBrowserPaths oscPath;
  private final BrowserFilterColumn browserFilterColumn;
  private String id;

  public BrowserFilterBank(ControllerHost host, OscController oscController, OscBrowserPaths oscPath, BrowserFilterColumn browserFilterColumn, int size, String id) {
    this.host = host;
    this.oscController = oscController;
    this.oscPath = oscPath;
    this.browserFilterColumn = browserFilterColumn;

    this.id = id;
    bank = browserFilterColumn.createItemBank(size);

    this.wildcardItem = browserFilterColumn.getWildcardItem();
    wildcardItem.isSelected().markInterested();

    IntStream.range(0, size).forEach(i -> {
      final int index = i;
      final int oscIndex = i + 1;
      BrowserFilterItem filterBankItem = bank.getItemAt(i);
      filterBankItem.hitCount().markInterested();
      filterBankItem.name().markInterested();
      filterBankItem.exists().markInterested();
      filterBankItem.isSelected().markInterested();

      oscController.addStringValue(OscBrowser.ID, oscPath.getFilterName(id, oscIndex), filterBankItem.name());
      oscController.addBooleanValue(OscBrowser.ID, oscPath.getFilterExists(id, oscIndex),filterBankItem.exists());
      oscController.addIntegerValue(OscBrowser.ID, oscPath.getFilterHits(id, oscIndex),filterBankItem.hitCount());

      if(i!=0) {
        oscController.addSettableBooleanValue(OscBrowser.ID, oscPath.getFilterSelected(id, oscIndex), filterBankItem.isSelected(), "Is selected");
      } else {
        //0 can never be selected but you can tell it to reset the browser selection
        oscController.registerOscCallback(oscPath.getFilterSelected(id, oscIndex), "select", this::reset);
      }
      browserFilterItems.add(filterBankItem);
    });

    //Filter Bank Manipulation
    oscController.registerOscCallback(oscPath.getFilterScroll(id), "Scroll by X amount", this::scrollBy);
    oscController.registerOscCallback(oscPath.getFilterScrollPageUp(id), "Scroll Page Up", this::scrollPageUp);
    oscController.registerOscCallback(oscPath.getFilterScrollPageDown(id), "Scroll Page Down", this::scrollPageDown);
    oscController.registerOscCallback(oscPath.getFilterReset(id), "Sets selection to 0", this::reset);
  }

  private void scrollPageUp(OscConnection oscConnection, OscMessage oscMessage) {
    bank.scrollByPages(-1);
  }

  private void scrollPageDown(OscConnection oscConnection, OscMessage oscMessage) {
    bank.scrollByPages(1);
  }

  private void scrollBy(OscConnection oscConnection, OscMessage oscMessage) {
    int amountToScroll = (int) Math.floor(oscMessage.getFloat(0));
    bank.scrollBy(amountToScroll);
  }
  private void reset(OscConnection oscConnection, OscMessage oscMessage) {
    resetCurrentView();
    //Force Update the browser
    oscController.forceNextFlush(OscBrowser.ID);
  }

  public void selectByName(String s) {
    int capacityOfBank = bank.getCapacityOfBank();
    for (int i = 0; i < capacityOfBank; i++) {
      BrowserFilterItem filterBankItem = bank.getItemAt(i);
      String            filterBankName             = filterBankItem.name().get();
      if(s.equals(filterBankName)) {
        filterBankItem.isSelected().set(true);
      }
    }
  }

  private void resetCurrentView() {
    int capacityOfBank = bank.getCapacityOfBank();
    for (int i = 0; i < capacityOfBank; i++) {
      if(bank.getItemAt(i).isSelected().get()) {
        bank.getItemAt(i).isSelected().set(false);
      }
    }
  }

  public void selectByIndex(int index) {
    if (index > -1 && index < bank.getCapacityOfBank()) {
      bank.getItemAt(index).isSelected().set(true);
    }
  }
}
