package com.kirkwoodwest.openwoods.misc;

import com.bitwig.extension.callback.StringValueChangedCallback;
import com.bitwig.extension.controller.api.StringValue;

public class OWStringValue implements StringValue {
  @Override
  public String get() {
    return null;
  }

  @Override
  public String getLimited(int maxLength) {
    return null;
  }

  @Override
  public void markInterested() {

  }

  @Override
  public void addValueObserver(StringValueChangedCallback callback) {

  }

  @Override
  public boolean isSubscribed() {
    return false;
  }

  @Override
  public void setIsSubscribed(boolean value) {

  }

  @Override
  public void subscribe() {

  }

  @Override
  public void unsubscribe() {

  }
}
