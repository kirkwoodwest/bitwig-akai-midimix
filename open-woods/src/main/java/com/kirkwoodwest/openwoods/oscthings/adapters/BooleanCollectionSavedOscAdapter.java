package com.kirkwoodwest.openwoods.oscthings.adapters;

import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.osc.adapters.OscAdapter;
import com.kirkwoodwest.openwoods.savelist.BooleanCollectionSaved;

public class BooleanCollectionSavedOscAdapter extends OscAdapter<BooleanCollectionSaved> {
  // osc Implementation with a boolean collection saved inside
  //Array of dirty values
  private final boolean[] dirtyValues;
  private final int size;

  public BooleanCollectionSavedOscAdapter(OscController oscController, String path, BooleanCollectionSaved booleanCollectionSaved) {
    super(booleanCollectionSaved, oscController, path, "");
    size = booleanCollectionSaved.size();
    dirtyValues = new boolean[size];

    booleanCollectionSaved.addValueObserver((index, value) -> {
      // do something with the value

      dirtyValues[index] = true;
      setDirty(true);
    });
    // constructor
  }

  @Override
  public void flush() {
    if (getDirty() || forceFlush) {
      if (forceFlush) {
        //Force Flush Style gets everything...
        for (int i = 0; i < size; i++) {
            oscController.addMessageToQueue(getPath(), i, dataSource.getValue(i));
            dirtyValues[i] = false;
        }
      } else {
        for (int i = 0; i < size; i++) {
          if (dirtyValues[i]) {
            oscController.addMessageToQueue(getPath(), i, dataSource.getValue(i));
            dirtyValues[i] = false;
          }
        }
      }

      forceFlush = false;
      setDirty(false);
    }
  }
}