package com.kirkwoodwest.openwoods.oscthings.browser;

public class OscBrowserPaths {
  public static final String DEFAULT_PATH = "/home";
  final String BROWSER_EXISTS;
  final String BROWSER_CANCEL;
  final String BROWSER_CONFIRM;
  private final String path;

  public OscBrowserPaths(String oscPath) {
    path = oscPath;
    BROWSER_EXISTS = path + "/exists";
    BROWSER_CONFIRM = path + "/confirm";
    BROWSER_CANCEL = path + "/cancel";
  }

  private String getFilter(String id) { return path + "/filter/" + id; }
  public String getFilterName(String id, int index) { return getFilter(id) + "/" + index + "/name"; }
  public String getFilterExists(String id, int index) { return getFilter(id) + "/" + index + "/exists"; }
  public String getFilterHits(String id, int index) { return getFilter(id) + "/"  +index + "/hits"; }
  public String getFilterSelected(String id, int index) { return getFilter(id) + "/" + index + "/selected";}
  public String getFilterScroll(String id) { return getFilter(id) +"/scroll"; }
  public String getFilterScrollPageUp(String id) { return getFilter(id) + "/scroll_page_up"; }
  public String getFilterScrollPageDown(String id) { return getFilter(id) + "/scroll_page_down"; }
  public String getFilterReset(String id) { return getFilter(id) + "/reset"; }
  public String getFilterSize(String id) { return getFilter(id) + "/size"; }

  public String getSelectedContentTypeName() { return path + "/type/name"; }
  public String getSelectedContentType() { return path + "/type"; }

  public String getResultsName(int index) { return path + "/filter/results/" + index + "/name"; }

  public String getResultsExists(int index) { return path + "/filter/results/" + index + "/exists"; }
  public String getResultsSelected(int index) { return path + "/filter/results/" + index + "/selected"; }

  public String getResultsScroll() {
    return path + "/results/scroll";
  }

}
