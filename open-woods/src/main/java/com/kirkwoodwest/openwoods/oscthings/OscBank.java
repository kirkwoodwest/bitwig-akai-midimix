package com.kirkwoodwest.openwoods.oscthings;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.values.SettableStringValueImpl;

public class OscBank<T extends ObjectProxy>{
  public OscBank(Bank<T> bank, OscController oscController, String oscPath, String groupID) {
    bank.scrollPosition().markInterested();
    bank.itemCount().markInterested();
    int sizeOfBank = bank.getSizeOfBank();

    int capacityOfBank = bank.getCapacityOfBank();

    {
      //Led for number of items in bank.
      String target = oscPath + "/item_count";
      oscController.addIntegerValue(groupID, target,  bank.itemCount());
    }
    {
      //Led for Page Position in the bank 1/4 page/totalPages
      String target = oscPath + "/page_position";
      class PagePosition {
        int scrollPosition = 0;
        int sizeOfBank = 0;
        SettableStringValueImpl settableStringValue = new SettableStringValueImpl("0/0");

        public PagePosition() {
          //Led for Page Position in the bank 1/4 page/totalPages
          bank.scrollPosition().addValueObserver((v) -> {
            scrollPosition = v;
            this.update();
          });

          bank.itemCount().addValueObserver((v)->{
            sizeOfBank = v;
            this.update();
          });
        }

        private void update() {
          int numItems = sizeOfBank;
          int totalPages = (numItems + capacityOfBank - 1) / capacityOfBank; // Total pages including partial page

          int page;
          if (scrollPosition >= (totalPages - 1) * capacityOfBank) {
            // If scrollPosition is on the last page (including partially filled)
            page = totalPages;
          } else {
            // Calculate the current page based on scroll position
            page = 1 + ( (scrollPosition + capacityOfBank - 1) / capacityOfBank );
          }
          settableStringValue.set(page + "/" + totalPages);

        }

        public StringValue getPagePosition() {
          return settableStringValue;
        }
      }
      PagePosition pp = new PagePosition();
      oscController.addStringValue(groupID, target, pp.getPagePosition());
    }

    {
      //Device Bank Page Left
      String target = oscPath + "/nav_previous";
      oscController.registerOscCallback(target, "Device Bank Page Left", (oscConnection, oscMessage) -> {
        int scrollPosition = bank.scrollPosition().get();
        int newScrollPosition = scrollPosition - capacityOfBank;
        int numItems = bank.itemCount().get();
        if (newScrollPosition < 0) {
          newScrollPosition = 0;
        }
        bank.scrollPosition().set(newScrollPosition);
      });
    }
    {
      //Device Bank Page Right
      String target = oscPath + "/nav_next";
      oscController.registerOscCallback(target, "Device Bank Page Right", (oscConnection, oscMessage) -> {
        int scrollPosition = bank.scrollPosition().get();
        int newScrollPosition = scrollPosition + capacityOfBank;
        int numItems = bank.itemCount().get();
        if (newScrollPosition > numItems - 8) {
          newScrollPosition = numItems - 8;
        }

        bank.scrollPosition().set(newScrollPosition);
      });
    }
  }
}
