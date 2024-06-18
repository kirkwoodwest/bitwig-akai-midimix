package com.kirkwoodwest.extensions.usercontrolsosc.interfaces;

import com.bitwig.extension.controller.api.*;

//This class uses a CursorTrack, SuperBank and a trackBank. We build some document prefrencees so we can send a signal
// to this class to set the current position of that of the cursor track.
public class TrackBankNavigator {

  static public void createTrackBankCursor(ControllerHost host, String label, String category, String action, CursorTrack cursorTrack, TrackBank trackBank) {

    DocumentState documentState = host.getDocumentState();

    //mark names on each itemAt in trackBank as interested. using stream.
    int sizeOfBank = trackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      Track track = trackBank.getItemAt(i);
      track.name().markInterested();
    }

    SettableStringValue targetTrack = documentState.getStringSetting(label + "Tracks", category, 24, "---");
    targetTrack.markInterested();
    //Create a signal that will be used to set the current position of the track bank to that of the cursor track.
    Signal setTrackBankToCursorTrack = documentState.getSignalSetting(label, category, action);

    //Add a signal observer to the signal we just created.
    trackBank.scrollPosition().markInterested();
    cursorTrack.position().markInterested();

    setTrackBankToCursorTrack.addSignalObserver(() -> {
      trackBank.scrollPosition().set(cursorTrack.position().get());
      host.scheduleTask(() -> {
        String firstName = trackBank.getItemAt(0).name().get();
        String lastName = trackBank.getItemAt(trackBank.getSizeOfBank() - 1).name().get();
        if(lastName.equals("")) {
          lastName = "...";
        }
        targetTrack.set(firstName + " - " + lastName);
        host.showPopupNotification(label + " Set: " + firstName + " - " + lastName);
      }, 100);
    });
  }
}
