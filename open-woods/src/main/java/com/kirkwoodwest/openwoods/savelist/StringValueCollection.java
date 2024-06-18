package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.controller.api.StringValue;

public class StringValueCollection extends StringCollection {
  public StringValueCollection(StringValue stringValue, int size) {
    super(size);
    stringValue.addValueObserver(this::setTempString);
  }
}
