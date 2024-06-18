package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.RemoteControl;
import com.bitwig.extension.controller.api.Track;
import com.kirkwoodwest.closedwoods.trackmaster.cursor.CursorRemoteControlsPageLocked;
import com.kirkwoodwest.openwoods.locks.Lock;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteBuilder;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControlsPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Container for project remotes and parameters. Allows for quick creation of project remote pages and the parameters with a specific tag in mind.
 */
public class ProjectRemotes {
  private final List<OwRemoteControlsPage> pages = new ArrayList<>();
  private final ControllerHost host;
  private final int numRemotes;
  private final List<OwRemoteControl> owRemoteControls = new ArrayList<>();
  private static final int PARAMETER_COUNT = 8;

  /**
   * Constructs a ProjectRemotes instance.
   *
   * @param host          The controller host.
   * @param remoteBaseTag The base tag for the remotes.
   * @param numRemotePages    The number of remote pages
   */
  public ProjectRemotes(ControllerHost host, String remoteBaseTag, int numRemotePages) {
    this.host = host;
    this.numRemotes = numRemotePages;
    IntStream.range(0, numRemotePages).forEach(i -> setupRemoteControls(remoteBaseTag, host, i));
  }

  /**
   * Sets up remote controls for a given track and pageIndex.
   *
   * @param remoteBaseTag The base tag for the remotes.
   * @param rootTrack     The root track.
   * @param pageIndex     The pageIndex of the remote.
   */
  private void setupRemoteControls(String remoteBaseTag, ControllerHost host, int pageIndex) {
    String filterExpression = createFilterExpression(remoteBaseTag, pageIndex);

    boolean lockPage = filterExpression.isEmpty();
    OwRemoteControlsPage owRemoteControlsPage = OwRemoteBuilder.buildGlobalRemotes("rootTrackRemoteControls", host, PARAMETER_COUNT, filterExpression, pageIndex, lockPage);
    owRemoteControls.addAll(owRemoteControlsPage.getRemoteControls());
    pages.add(owRemoteControlsPage);
  }

  /**
   * Marks the properties of a RemoteControl as interested.
   *
   * @param remoteControl The remote control to mark.
   */
  private void markRemoteControlInterested(RemoteControl remoteControl) {
    remoteControl.exists().markInterested();
    remoteControl.name().markInterested();
    remoteControl.isBeingMapped().markInterested();
    remoteControl.displayedValue().markInterested();
    remoteControl.modulatedValue().markInterested();
    remoteControl.value().markInterested();
    remoteControl.setIndication(true);
  }

  /**
   * Creates a filter expression based on the base tag and index.
   *
   * @param baseTag The base tag.
   * @param index   The index for the filter expression.
   * @return The created filter expression.
   */
  private String createFilterExpression(String baseTag, int index) {
    return baseTag.isEmpty() ? "" : baseTag + (index + 1);
  }

  /**
   * Retrieves a list of PagedRemoteControls for a specific page index.
   *
   * @param pageIndex The index of the page.
   * @return A list of PagedRemoteControls.
   */
  public List<OwRemoteControl> getRemoteControlsFromPage(int pageIndex) {
    int startIndex = pageIndex * PARAMETER_COUNT;
    return IntStream.range(startIndex, startIndex + PARAMETER_COUNT)
            .mapToObj(owRemoteControls::get)
            .collect(Collectors.toList());
  }

  /**
   * Returns a copy of the list of all paged remote controls.
   *
   * @return A list of PagedRemoteControls.
   */
  public List<OwRemoteControl> getRemoteControls() {
    return new ArrayList<>(owRemoteControls);
  }

  /**
   * Retrieves a specific PagedRemoteControl based on page and parameter index.
   *
   * @param pageIndex       The index of the page.
   * @param parameterIndex  The index of the parameter.
   * @return The PagedRemoteControl at the specified page and parameter index.
   */
  public OwRemoteControl getRemoteControl(int pageIndex, int parameterIndex) {
    int index = pageIndex * PARAMETER_COUNT + parameterIndex;
    return owRemoteControls.get(index);
  }

  /**
   * Returns a copy of the list of all CursorRemoteControlsPages.
   *
   * @return A list of CursorRemoteControlsPages.
   */
  public List<CursorRemoteControlsPage> getCursorRemotePages() {
     return pages.stream().map(OwRemoteControlsPage::getPage).collect(Collectors.toList());
  }

  /**
   * Retrieves a specific CursorRemoteControlsPage based on its index.
   *
   * @param pageIndex The index of the page.
   * @return The CursorRemoteControlsPage at the specified index.
   */
  public CursorRemoteControlsPage getCursorRemotePage(int pageIndex) {
    return pages.get(pageIndex).getPage();
  }

  /**
   * Returns the number of remotes.
   *
   * @return The number of remotes.
   */
  public int getNumRemotes() {
    return numRemotes;
  }

  /**
   * Returns the number of parameters per page.
   *
   * @return The number of parameters per page.
   */
  public int getNumParameters() {
    return PARAMETER_COUNT;
  }
}
