package com.kirkwoodwest.closedwoods.trackmaster.cursor;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;

public class CursorRemoteControlsPageLocked {
  private final CursorRemoteControlsPage page;
  private final int index;
  private boolean exists = false;

  //Constructor
  public CursorRemoteControlsPageLocked(CursorRemoteControlsPage page, int pageIndex, boolean isLocked) {
    this.page = page;
    this.index = pageIndex;

    if(!isLocked) {
      exists = true;
      return;
    }


    //If page count changes reset index
    page.pageCount().addValueObserver((count)->{
      reset();
    });

    //If selected page index changes then reset index
    page.selectedPageIndex().addValueObserver((index)->{
      reset();
    });

    //Page Names Changed, this can happen while editing. It keeps the page focused on its target.
    page.pageNames().addValueObserver((pageNames)->{
      reset();
    });
  }

  public CursorRemoteControlsPage getPage() {
    return page;
  }

  public boolean exists(){
    return exists;
  }

  public void reset() {
    int index = page.selectedPageIndex().get();
    if(this.index != index) {
      page.selectedPageIndex().set(this.index);
      exists = false;
    } else {
      exists = true;
    }
  }
}
