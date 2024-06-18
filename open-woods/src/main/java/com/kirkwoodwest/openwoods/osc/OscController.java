package com.kirkwoodwest.openwoods.osc;

import com.bitwig.extension.api.opensoundcontrol.OscMethodCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.flush.Flushables;
import com.kirkwoodwest.openwoods.flush.ForceFlushGroup;
import com.kirkwoodwest.openwoods.osc.adapters.*;
import com.kirkwoodwest.openwoods.osc.documenter.OscDocumenter;

import java.io.Flushable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * OscController is a class that manages a group of OscAdapters.
 * provides a way to enable or disable them
 * and flush them all at once
 */
public class OscController implements Flushable {
  private final ControllerHost host;
  private final OscHost oscHost;
  private final String oscPath;
  private final HashMap<String, ArrayList<OscAdapter<?>>> adapters;
  private final HashMap<String, Boolean> enabled = new HashMap<>();
  private final ForceFlushGroup forceFlushGroup = new ForceFlushGroup();
  private final OscDocumenter oscDocumenter;

  public OscController(ControllerHost host, String oscPath) {
    this(host, new OscHost(host, oscPath), oscPath);
  }

  public OscController(ControllerHost host, OscHost oscHost, String oscPath) {
    this.host = host;
    this.oscHost = oscHost;
    this.oscPath = oscPath;
    this.adapters = new HashMap<>();

    oscHost.registerOscCallback( "/osc/refresh", "Refresh", (oscConnection, oscMessage) -> {
      forceNextFlush();
    });

    DocumentState documentState = host.getDocumentState();
    Signal signalSetting = documentState.getSignalSetting("OSC Refresh", "OSC", "OSC Refresh");
    signalSetting.addSignalObserver(this::forceNextFlush);

    //Osc Documentation
    oscDocumenter = new OscDocumenter(host);
    oscHost.registerOscCallback("/osc/doc", "Print Documentation", (oscConnection, oscMessage) -> {
      oscDocumenter.printDocumentation();
    });

    oscDocumenter.printDocumentation();
    //Self add to flushables
    Flushables.getInstance().add(this);
  }

  public OscHost getOscHost() {
    return this.oscHost;
  }

  /**
   * Adds an adapter to a group
   * @param groupId
   * @param adapter
   * @return
   */
  public OscAdapter<?> addAdapter(String groupId, OscAdapter<?> adapter) {
    addAdapterToGroup(groupId, adapter);
    addAdapterInfo(adapter);
    return adapter;
  }

  private void addAdapterInfo(OscAdapter<?> adapter) {
    AdapterInfo adapterInfo = adapter.getAdapterInfo();
    adapterInfo.get().forEach(adapterInfoData -> {
      oscDocumenter.addPath(oscPath +  adapterInfoData.path(), adapterInfoData.description(), adapterInfoData.typeTag(), false, true);
    });
  }

  public void addIndexedPathInfo(String oscPath, String description) {
    oscDocumenter.addIndexedPath(this.oscPath + oscPath, description);
  }


  private void addCallbackInfo(String oscPath, String description, String typeTag) {
    //TODO Complete info...
    oscDocumenter.addPath(this.oscPath + oscPath, description, typeTag, true, false);
  }

  //Shorthand For Known Adapter Types
  public BooleanValueOscAdapter addBooleanValue(String groupId, String oscPath, BooleanValue booleanValue) {
    return (BooleanValueOscAdapter) addAdapter(groupId, new BooleanValueOscAdapter(booleanValue, this, oscPath, ""));
  }

  public SettableBooleanValueOscAdapter addSettableBooleanValue(String groupId, String oscPath, SettableBooleanValue settableBooleanValue, String oscDescription) {
    return (SettableBooleanValueOscAdapter) addAdapter(groupId, new SettableBooleanValueOscAdapter(settableBooleanValue, this, oscPath, oscDescription));
  }

  public IntegerValueOscAdapter addIntegerValue(String groupId, String oscPath, IntegerValue integerValue) {
    return (IntegerValueOscAdapter) addAdapter(groupId, new IntegerValueOscAdapter(integerValue, this, oscPath, ""));
  }

  public SettableIntegerValueOscAdapter addSettableIntegerValue(String groupId, String oscPath, SettableIntegerValue settableIntegerValue, String oscDescription) {
    return (SettableIntegerValueOscAdapter) addAdapter(groupId, new SettableIntegerValueOscAdapter(settableIntegerValue, this, oscPath, oscDescription));
  }

  public DoubleValueOscAdapter addDoubleValue(String groupId, String oscPath, DoubleValue doubleValue) {
    return (DoubleValueOscAdapter) addAdapter(groupId, new DoubleValueOscAdapter(doubleValue, this, oscPath, "", true));
  }

  public DoubleValueOscAdapter addDoubleValue(String groupId, String oscPath, DoubleValue doubleValue, boolean useFloats) {
    return (DoubleValueOscAdapter) addAdapter(groupId, new DoubleValueOscAdapter(doubleValue, this, oscPath, "", useFloats));
  }

  public RangedValueOscAdapter addRangedValue(String groupId, String oscPath, RangedValue rangedValue) {
    return (RangedValueOscAdapter) addAdapter(groupId, new RangedValueOscAdapter(rangedValue, this, oscPath, "", true));
  }
  
  
//  public SettableIntegerValueOscAdapter addSettableDoubleValue(String groupId, String oscPath, SettableIntegerValue settableIntegerValue, String oscDescription) {
//    return (SettableIntegerValueOscAdapter) addAdapter(groupId, new SettableIntegerValueOscAdapter(settableIntegerValue, oscHost, oscPath, oscDescription));
//  }

  public StringValueOscAdapter addStringValue(String groupId, String oscPath, StringValue stringValue) {
    return (StringValueOscAdapter) addAdapter(groupId, new StringValueOscAdapter(stringValue, this, oscPath, ""));
  }

  public SettableStringValueOscAdapter addSettableStringValue(String groupId, String oscpath, SettableStringValue settableStringValue, String oscDescription) {
    return (SettableStringValueOscAdapter) addAdapter(groupId, new SettableStringValueOscAdapter(settableStringValue, this, oscpath, oscDescription));
  }

  public ColorValueOscAdapter addColorValue(String groupId, String oscPath, ColorValue colorValue, String oscDescription) {
    return (ColorValueOscAdapter) addAdapter(groupId, new ColorValueOscAdapter(colorValue, this, oscPath, oscDescription));
  }

  public SettableColorValueOscAdapter addSettableColorValue(String groupId, String oscPath, SettableColorValue settableColorValue, String oscDescription) {
    return (SettableColorValueOscAdapter) addAdapter(groupId, new SettableColorValueOscAdapter(settableColorValue, this, oscPath, oscDescription));
  }

  public EnumValueOscAdapter addEnumValue(String groupId, String oscPath, EnumValue enumValue) {
    return (EnumValueOscAdapter) addAdapter(groupId, new EnumValueOscAdapter(enumValue, this, oscPath, ""));
  }

  public ParameterOscAdapter addParameter(String groupId, String oscPath, Parameter parameter, String description) {
    return (ParameterOscAdapter) addAdapter(groupId, new ParameterOscAdapter(parameter, this, oscPath, description));
  }

  /**
   * Enable or disable a group
   * @param groupId
   * @param enabled
   */
  public void setGroupEnabled(String groupId, boolean enabled) {
    if (this.enabled.containsKey(groupId)) {
      this.enabled.put(groupId, enabled);
      if (enabled) {
        forceNextFlush(groupId);
      }
    }
  }

  /**
   * Add an adapter to a group
   * @param groupId
   * @param adapter
   */
  private void addAdapterToGroup(String groupId, OscAdapter<?> adapter) {
    if (!adapters.containsKey(groupId)) {
      adapters.put(groupId, new ArrayList<>());
    }
    enabled.put(groupId, true);
    adapters.get(groupId).add(adapter);
  }

  /**
   * Force a flush on all groups
   */
  public void forceNextFlush() {
    adapters.forEach((group, leds) -> forceFlushGroup.add(group));
    host.requestFlush();
  }

  /**
   * Force a flush on a specific group ID
   *
   * @param groupName
   */
  public void forceNextFlush(String groupName) {
    if (adapters.containsKey(groupName)) {
      forceFlushGroup.add(groupName);
      host.requestFlush();
    }
  }

  public void registerOscCallback(String oscPath, String description, OscMethodCallback callback) {
    addCallbackInfo(oscPath, description, "*");
    this.oscHost.registerOscCallback(oscPath, description, callback);
  }
  //Register Osc Callback with Type Tag Pattern
  public void registerOscCallback(String oscPath, String typeTagPattern, String description, OscMethodCallback callback) {
    addCallbackInfo(oscPath, description, typeTagPattern);
    this.oscHost.registerOscCallback(oscPath, typeTagPattern, description, callback);
  }

  public void registerOscIntegerCallback(String oscPath, String description, Consumer<Integer> callback) {
    addCallbackInfo(oscPath, description, ",i");
    this.oscHost.registerOscCallback(oscPath, description, (oscConnection, oscMessage) -> callback.accept(oscMessage.getInt(0)));
  }

  public void registerOscNoParameterCallback(String oscPath, String description, Runnable callback) {
    addCallbackInfo(oscPath, description, "");
    this.oscHost.registerOscCallback(oscPath, description, (oscConnection, oscMessage) -> callback.run());
  }

  public void addMessageToQueue(String oscPath, Object... message) {
    this.oscHost.addMessageToQueue(oscPath, message);
  }

  /**
   * Request a flush from Bitwig
   */
  public void requestFlush() {
    host.requestFlush();
  }

  public void printDocumentation() {
    oscDocumenter.printDocumentation();
  }

  @Override
  public void flush() {
    HashSet<String> updateGroup = new HashSet<>(this.forceFlushGroup.get());

    adapters.forEach((ledGroup, adapterGroup) -> {
      if (!enabled.get(ledGroup)) return;
      final boolean doForceFlush = updateGroup.contains(ledGroup);
      adapterGroup.forEach(adapter -> {
        try {
          if (doForceFlush) adapter.forceNextFlush();
          adapter.flush();
        } catch (Exception e) {
          host.errorln("Problem with flushing Adapter in group [" + ledGroup + "] : " + adapter.getClass());
        }
      });
    });
    oscHost.sendQueue();
  }
}