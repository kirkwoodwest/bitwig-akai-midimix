package com.kirkwoodwest.openwoods.locks;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;

class LockCursorRemotePage extends Lock<CursorRemoteControlsPage> {
  private final CursorRemoteControlsPage page;

  public LockCursorRemotePage(CursorRemoteControlsPage page, int pageIndex){
    this.page = page;
    this.index = pageIndex;

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

  @Override
  public void reset() {
    int index = page.selectedPageIndex().get();
    if(this.index != index) {
      page.selectedPageIndex().set(this.index);
    }
  }
}
