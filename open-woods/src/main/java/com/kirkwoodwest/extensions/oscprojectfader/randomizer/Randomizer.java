package com.kirkwoodwest.extensions.oscprojectfader.randomizer;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.utils.SerializationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Randomizer {

  private final SettableStringValue saveData;
  private final ControllerHost host;
  private final List<CursorRemoteControlsPage> cursorRemotePages;
  private final List<RandomizerItem> items = new ArrayList<>();
  private boolean doSave;
  private boolean isSaving;

  public Randomizer(ControllerHost host, List<OwRemoteControl> remoteControls, List<CursorRemoteControlsPage> cursorRemotePages) {
    this.host = host;
    this.cursorRemotePages = setupRemotes(remoteControls, cursorRemotePages);
    this.saveData = createSaveData(host);
  }

  private SettableStringValue createSaveData(ControllerHost host) {
    final SettableStringValue saveData;
    DocumentState documentState = host.getDocumentState();
    saveData = documentState.getStringSetting("randomizer", "randomizer", Integer.MAX_VALUE - 1, "");
    saveData.markInterested();
    saveData.addValueObserver((value) -> {
      if (!value.isEmpty()) {
        if(isSaving) {
          restoreState();
          host.requestFlush();
        }
      } else {
        //Its empty so we better initialize the items again and save state.
        items.forEach(item -> {
            item.init();
            saveState();
        });
      }
    });
    return saveData;
  }

  private List<CursorRemoteControlsPage> setupRemotes(List<OwRemoteControl> remoteControls, List<CursorRemoteControlsPage> cursorRemotePages) {
    remoteControls.forEach(pagedRemoteControl -> {
      items.add(new RandomizerItem(this, pagedRemoteControl, 0.0, 1.0));
      RemoteControl remoteControl = pagedRemoteControl.getRemoteControl();
      remoteControl.name().markInterested();
      remoteControl.value().markInterested();
    });

    cursorRemotePages.forEach(cursorRemoteControlsPage -> {
      cursorRemoteControlsPage.getName().markInterested();
    });
    return cursorRemotePages;
  }

  public void randomizeParameters() {
    items.forEach(RandomizerItem::randomizeParameter);
  }

  public void randomizeItem(int index){
    items.get(index).randomizeParameter();
  }

  public double getRandomValue(int index){
    return items.get(index).getRandomValue();
  }

  public void setItemValue(int index, double value) {
    items.get(index).getOwRemoteControl().getRemoteControl().value().set(value);
  }

  public List<RandomizerItem> getItems() {
    return items;
  }
  public List<CursorRemoteControlsPage> getPages() {
    return cursorRemotePages;
  }

  public int getItemCount() {
    return items.size();
  }

  public int getItemIndex(int i) {
    return items.get(i).getOwRemoteControl().getRemoteIndex();
  }

  public int getItemPageIndex(int index) {
    return items.get(index).getOwRemoteControl().getPageIndex();
  }

  public BooleanValue itemIsValid(int index){
    //TODO: This is incorrect... u
    return items.get(index).getOwRemoteControl().getRemoteControl().exists();
  }

  public Supplier<String> itemName(int index){
    return ()->{
      if(items.get(index).getOwRemoteControl().exists()){
        return items.get(index).getOwRemoteControl().getRemoteControl().name().get();
      } else {
        return "";
      }
    };
  }

  public Supplier<Double> itemValue(int index){
    return ()->{
      if(items.get(index).getOwRemoteControl().exists()){
        return items.get(index).getOwRemoteControl().getRemoteControl().value().get();
      } else {
        return 0.0;
      }
    };
  }


  public Supplier<Double> itemMin(int index){
    return ()->items.get(index).getMin();
  }

  public void itemSetMin(int index, double min){
    items.get(index).setMin(min);
  }

  public Supplier<Double> itemMax(int index){
    return ()->items.get(index).getMax();
  }

  public void itemSetMax(int index, double max){
    items.get(index).setMax(max);
  }

  public Supplier<Boolean> itemLocked(int index){
    return items.get(index)::getLocked;
  }

  public void itemSetLocked(int index, boolean locked){
    items.get(index).setLocked(locked);
  }


  public synchronized void saveState() {
    //Build array of serialized Items
    ArrayList<RandomizerItemData> list = new ArrayList<>();
    items.forEach(item -> {
      list.add(item.getData());
    });

    String serialized = serialize(list);

    //Check if the data is the same as the last save, this prevents the save from being called and the document from being marked dirty.
    if (serialized.equals(saveData.get())) {
      isSaving = false;
      return;
    }
    saveData.set(serialized);
  }

  public synchronized void restoreState(){
    if(!saveData.get().isEmpty()) {
      String serialized = saveData.get();
      try {
        ArrayList<RandomizerItemData> list = deserialize(serialized);

        boolean sizeMismatch = list.size() != items.size();
        int documentSize = list.size();
        int projectSize = items.size();

        items.forEach(item -> {
          if(!list.isEmpty()) {
            item.setData(list.remove(0));
          }
        });
        if(sizeMismatch){
          host.errorln("Randomizer: Restore State Failed.  Project Remote Size Changed.");
          host.showPopupNotification("Randomizer: # of remotes different than document and project. Document: " + documentSize + " Project: " + projectSize);
        }

      } catch (Exception e) {
        host.println(e.getMessage());
        host.showPopupNotification("Randomizer: Corrupt Data in settings.");
      }

    }
  }

  public String serialize(ArrayList<RandomizerItemData> list){
    return SerializationUtil.serialize(list);
  }

  public ArrayList<RandomizerItemData> deserialize(String serialized) {
    Object deserializedObject = SerializationUtil.deserialize(serialized);
    if (deserializedObject instanceof ArrayList<?>) {
      ArrayList<?> arrayList = (ArrayList<?>) deserializedObject;
      ArrayList<RandomizerItemData> result = new ArrayList<>();
      for (Object item : arrayList) {
        if (item instanceof RandomizerItemData) {
          result.add((RandomizerItemData) item);
        } else {
          throw new ClassCastException("Deserialized list contained an object that is not a RandomizerItemData");
        }
      }
      return result;
    } else {
      throw new ClassCastException("Deserialized object is not an ArrayList");
    }
  }

  public void update() {
    this.doSave = true;
    isSaving = true;
    host.requestFlush();
  }

  public void flush() {
    if(doSave) {
      saveState();
      doSave = false;
    }
  }

}
