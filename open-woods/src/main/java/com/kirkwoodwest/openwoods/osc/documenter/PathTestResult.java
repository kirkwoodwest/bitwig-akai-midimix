package com.kirkwoodwest.openwoods.osc.documenter;

public enum PathTestResult {
  IS_INDEXED_AND_IS_ONE, // It's an indexed path and the index is 1
  IS_INDEXED_NOT_ONE,   // It's an indexed path but the index is not 1
  NOT_INDEXED           // It's not an indexed path
}
