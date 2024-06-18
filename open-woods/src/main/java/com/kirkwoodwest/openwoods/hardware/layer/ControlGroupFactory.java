package com.kirkwoodwest.openwoods.hardware.layer;

@FunctionalInterface
public interface ControlGroupFactory<T, S extends ControlGroup<T>> {
  S create(T hardware, int id);
}
