package com.kirkwoodwest.openwoods.remotecontrol;

import com.bitwig.extension.callback.IntegerValueChangedCallback;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.values.SettableStringValueImpl;
import com.kirkwoodwest.openwoods.values.SettableBooleanValueImpl;

import java.util.ArrayList;
import java.util.List;

public class OwRemoteControlsPage {
  private final CursorRemoteControlsPage page;
  private final List<OwRemoteControl> owRemoteControls = new ArrayList<>();
  private int index;
  private boolean isLocked;
  
  private final SettableBooleanValueImpl existsInternal = new SettableBooleanValueImpl(false);
  private final SettableStringValueImpl pageNameInternal = new SettableStringValueImpl("");

  // Constructor
  public OwRemoteControlsPage(CursorRemoteControlsPage page, int pageIndex, boolean isLocked) {
    this.page = page;
    this.index = pageIndex;
    this.isLocked = isLocked;
    int parameterCount = page.getParameterCount();

    //Create Parameter Remotes
    for (int i = 0; i < parameterCount; i++) {
      RemoteControl remoteControl = page.getParameter(i);
      OwRemoteControl owRemoteControl = new OwRemoteControl(remoteControl, this, i, pageIndex, isLocked);
      owRemoteControls.add(owRemoteControl);
    }

    IntegerValueChangedCallback valueChanged = (newValue) -> {
      if (isLocked) {
        reset();
        updateState();
      }
    };

    page.pageCount().addValueObserver(valueChanged);
    page.selectedPageIndex().addValueObserver(valueChanged);

    //Page Name
    page.getName().addValueObserver(pageNameChanged -> {
      pageNameInternal.set(pageNameChanged);
      updateState();
    });
  }

  public void setPage(int index) {
    this.index = index;
    reset();
    updateState();
  }

  public void setLocked(boolean isLocked) {
    this.isLocked = isLocked;
    owRemoteControls.forEach(owRemoteControl -> owRemoteControl.setLocked(isLocked));
    reset();
    updateState();
  }

  private void updateState() {
    if (isLocked) {
      exists().set( page.selectedPageIndex().get() == index );
    } else {
      exists().set(true);
    }
    
    if(exists().get()){
      pageNameInternal.set(page.getName().get());
    } else {
      pageNameInternal.set("");
    }
  }

  public CursorRemoteControlsPage getPage() {
    return page;
  }

  public SettableBooleanValueImpl exists() {
    return existsInternal;
  }

  public SettableStringValueImpl getName() {
    return pageNameInternal;
  }

  public List<OwRemoteControl> getRemoteControls() {
    return owRemoteControls;
  }

  public OwRemoteControl getRemoteControl(int index) {
    return owRemoteControls.get(index);
  }

  public int getParameterCount() {
    return owRemoteControls.size();
  }

  public void reset() {
    // Delay the reset if necessary
    int selectedIndex = page.selectedPageIndex().get();
    if (this.index != selectedIndex && this.index < page.pageCount().get()) {
      page.selectedPageIndex().set(this.index);
    }
    if (isLocked) {
      exists().set( page.selectedPageIndex().get() == index );
    } else {
      exists().set(true);
    }
  }
}
