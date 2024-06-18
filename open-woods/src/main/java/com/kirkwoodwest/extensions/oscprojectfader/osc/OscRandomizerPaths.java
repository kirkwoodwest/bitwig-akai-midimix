package com.kirkwoodwest.extensions.oscprojectfader.osc;

public class OscRandomizerPaths {

  private final String oscPath;
  private final String oscParam = "/param";

  String RANDOMIZE;

  //HashMap with String as key and OscTarget as value

  public OscRandomizerPaths(String oscPath){
    this.oscPath        = oscPath;
    this.RANDOMIZE      = oscPath + "/randomize";
  }

  public String getParamValue(int pageIndex, int index) {
    return oscPath + oscParam + "/" + pageIndex + "/" + index + "/value";
  }

  public String getParamRandomize(int pageIndex, int index) {
    return oscPath + oscParam + "/" + pageIndex + "/" + index + "/randomize";
  }

  public String getParamName(int pageIndex, int index) {
    return oscPath + oscParam + "/" + pageIndex + "/" + index + "/name";
  }

  public String getParameterExists(int pageIndex, int index) {
    return oscPath + oscParam + "/" + pageIndex + "/" + index + "/exists";
  }

  public String getParamMin(int pageIndex, int index) {
    return oscPath + oscParam  + "/" + pageIndex + "/" + index + "/min";
  }

  public String getParamMax(int pageIndex, int index) { return oscPath + oscParam  + "/" + pageIndex + "/"+ index + "/max"; }

  public String getParamLocked(int pageIndex, int index) {
    return oscPath + oscParam + "/" + pageIndex + "/" + index + "/locked";
  }

  public String getPageName(int oscIndex) {
    return oscPath + "/" + oscIndex + "/name";
  }
}
