package com.kirkwoodwest.openwoods.oscthings.browser;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.PopupBrowser;
import com.kirkwoodwest.openwoods.flush.ForceFlushQueue;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.osc.OscController;


import java.util.ArrayList;

public class OscBrowser {
  public static final String ID = "BROWSER";
  public static final String BROWSER_EXISTS_ID = "BROWSER_EXISTS";
  private final PopupBrowser popupBrowser;
  private final ArrayList<Led> leds = new ArrayList<>();
  private final ArrayList<ForceFlushQueue> filterBanks = new ArrayList<>();
  int BANK_SIZE = 16;

  private final BrowserFilterBank filterSmartCollectionColumn;
  private final BrowserFilterBank filterCategory;
  private final BrowserFilterBank filterDeviceType;
  private final BrowserFilterBank filterFileType;
  private final BrowserFilterBank filterLocation;
  private final BrowserFilterBank filterCreator;
  private final BrowserFilterBank filterDevice;
  private final BrowserFilterBank filterBankTag;
  private final BrowserResultsBank resultsBank;

  public OscBrowser(ControllerHost host, OscController oscController, String oscPath) {
    OscBrowserPaths paths = new OscBrowserPaths(oscPath);
    OscBrowserPreferences pref = new OscBrowserPreferences(host);
    popupBrowser = host.createPopupBrowser();

    oscController.addStringValue(BROWSER_EXISTS_ID, paths.getSelectedContentTypeName(), popupBrowser.selectedContentTypeName());
    oscController.addBooleanValue(BROWSER_EXISTS_ID, paths.BROWSER_EXISTS, popupBrowser.exists());
    popupBrowser.exists().addValueObserver((v)->{
      if(v){
        oscController.setGroupEnabled(ID, true);
      } else {
        oscController.setGroupEnabled(ID, false);
      }
    });

    popupBrowser.selectedContentTypeIndex().markInterested();
    oscController.registerOscCallback(paths.getSelectedContentType(), "Browser Content Type", this::selectContentType);

    oscController.registerOscCallback(paths.BROWSER_CONFIRM, "Confirm Browser Selection", this::confirm);
    oscController.registerOscCallback(paths.BROWSER_CANCEL, "Confirm Browser Selection", this::cancel);

    //Setup Filters and Results
    filterSmartCollectionColumn = new BrowserFilterBank(host, oscController, paths, popupBrowser.smartCollectionColumn(), pref.getSmartFilterSize(), "smartfilter");
    filterCategory = new BrowserFilterBank(host, oscController, paths,  popupBrowser.categoryColumn(), pref.getCategorySize(), "category");
    filterDeviceType = new BrowserFilterBank(host, oscController, paths, popupBrowser.deviceTypeColumn(), pref.getDeviceSize(), "devicetype");
    filterFileType = new BrowserFilterBank(host, oscController, paths,  popupBrowser.fileTypeColumn(), pref.getFileTypeSize(), "filetype");
    filterLocation = new BrowserFilterBank(host, oscController, paths, popupBrowser.locationColumn(), pref.getLocationSize(), "location");
    filterCreator = new BrowserFilterBank(host, oscController, paths,  popupBrowser.creatorColumn(), pref.getCreatorSize(), "creator");
    filterDevice = new BrowserFilterBank(host, oscController, paths, popupBrowser.deviceColumn(), pref.getDeviceSize(), "device");
    filterBankTag = new BrowserFilterBank(host, oscController, paths, popupBrowser.tagColumn(), pref.getBankTagSize(), "tag");
    resultsBank = new BrowserResultsBank(oscController, paths, popupBrowser.resultsColumn(), pref.getResultsBankSize());
  }

  private void selectContentType(OscConnection oscConnection, OscMessage oscMessage) {
    int index = (int) Math.floor(oscMessage.getFloat(0));
    popupBrowser.selectedContentTypeIndex().set(index);
  }

  private void cancel(OscConnection oscConnection, OscMessage oscMessage) {
    popupBrowser.cancel();
  }

  private void confirm(OscConnection oscConnection, OscMessage oscMessage) {
    popupBrowser.commit();
  }
  
}
