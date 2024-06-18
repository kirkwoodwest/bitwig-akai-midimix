package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.DocumentState;
import com.bitwig.extension.controller.api.SettableStringValue;
import com.kirkwoodwest.openwoods.settings.SettingsHelper;
import com.kirkwoodwest.utils.SerializationUtil;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Saves a list of items to the document state.
 * If you decide to implement this class, you'll need to call flush() in order to save data. This is easily done using
 * the Flushables class.
 */
public class SaveListData<T> {
  private final SettableStringValue setting;
  private final ControllerHost host;
  private final Supplier<ArrayList<T>> saveDataSupplier;
  private final Consumer<ArrayList<T>> recallDataConsumer;
  private final Class<T> clazz;
  private String lastSavedValue = "";
  private boolean doSave;
  private boolean isInternalChange = false; // Flag to indicate internal changes

  public SaveListData(ControllerHost host, String label, Supplier<ArrayList<T>> saveDataSupplier, Consumer<ArrayList<T>> recallDataConsumer, Class<T> clazz) {
    this.host = host;
    this.saveDataSupplier = saveDataSupplier;
    this.recallDataConsumer = recallDataConsumer;
    this.clazz = clazz;
    DocumentState documentState = host.getDocumentState();
    setting = documentState.getStringSetting(label, "Save Data", Integer.MAX_VALUE - 1, "");
    setting.markInterested();
    setting.addValueObserver((value) -> {
      if(!value.equals(lastSavedValue)) {
        if (!value.isEmpty() && !isInternalChange) {
          recallData();
          host.requestFlush();
        } else {
          saveState();
        }
      }
      isInternalChange = false; // Reset flag after processing
    });
  }

  public void hideSetting() {
    SettingsHelper.setVisible(setting,false);
  }

  public void saveNextFlush() {
    //This allows us to call this a bunch of times but not save until flush.
    this.doSave = true;
  }

  private synchronized void saveState() {
    ArrayList<T> items = saveDataSupplier.get();
    String serialized = serialize(items);
    if(!lastSavedValue.equals(serialized)) {
      isInternalChange = true;
    }

    lastSavedValue = serialized;
    setting.set(serialized);
  }

  public synchronized void recallData() {
    if (!setting.get().isEmpty() && !lastSavedValue.equals(setting.get())) {
      String serialized = setting.get();
      lastSavedValue = serialized;
      try {
        ArrayList<T> list = deserialize(serialized, clazz);
        recallDataConsumer.accept(list);
      } catch (Exception e) {
        host.showPopupNotification("Error in recalling data: " + e.getMessage());
      }
    }
  }

  private String serialize(ArrayList<T> list){
    return SerializationUtil.serialize(list);
  }

  private ArrayList<T> deserialize(String serialized, Class<T> clazz) {
    return SerializationUtil.deserializeAsList(serialized, clazz);
  }

  /**
   * This needs to be called in order to save data.
   */
  public void flush() {
    if(doSave) {
      saveState();
      doSave = false;
    }
  }
}
