package com.kirkwoodwest.extensions.oscprojectfader.osc;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.RandomizerItem;
import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.List;
import java.util.stream.IntStream;

public class OscRandomizer {

  /**
   *
   * @param oscController
   * @param randomizer
   * @param oscPath Base osc path to the randomizer
   * @param createBasicCommonParameters This will create osc addresses for exists, value, and name. Useful as an option to combine with other osc tools.
   */
  public OscRandomizer(OscController oscController, Randomizer randomizer, String oscPath, boolean createBasicCommonParameters) {
    OscRandomizerPaths paths = new OscRandomizerPaths(oscPath);
    List<RandomizerItem> items = randomizer.getItems();

    //TODO: None of these work with the new paradigm of using Values instead of raw variable types...

//
//    List<CursorRemoteControlsPage> pages = randomizer.getPages();
//    IntStream.range(0, pages.size()).forEach(i -> {
//      final int oscIndex = i + 1;
//      oscController.addStringValue("Randomizer", paths.getPageName(oscIndex), pages.get(i).getName());
//    });
//
//    IntStream.range(0, randomizer.getItemCount()).forEach(i -> {
//      int pageIndex = randomizer.getItemPageIndex(i);
//      int index = randomizer.getItemIndex(i);
//      final int oscIndex = index + 1;
//      final int oscPageIndex = pageIndex + 1;
//      if (createBasicCommonParameters) {
//        //Exists
//        oscController.addBooleanValue("Randomizer", paths.getParameterExists(oscPageIndex, oscIndex), randomizer.itemIsValid(i));
//
//        //Name
//        oscController.addBooleanValue("Randomizer", paths.getParamName(oscPageIndex, oscIndex), randomizer.itemName(i));
//
//        //Value
//        oscController.addDoubleValue("Randomizer", paths.getParamValue(oscPageIndex, oscIndex), randomizer.itemValue(i));
//        oscController.registerOscCallback(paths.getParamValue(oscPageIndex, oscIndex), "Sets the min value for the parameter", (oscConnection, oscMessage) -> {
//          randomizer.setItemValue(i, oscMessage.getFloat(0));
//        });
//      }
//
//      //Min
//      oscController.addDoubleValue("Randomizer", paths.getParamMin(oscPageIndex, oscIndex), randomizer.itemMin(i));
//      oscController.registerOscCallback(paths.getParamMin(oscPageIndex, oscIndex), "Sets the min value for the parameter", (oscConnection, oscMessage) -> {
//        randomizer.itemSetMin(i, oscMessage.getFloat(0));
//      });
//
//      //Max
//      oscController.addLedDouble("Randomizer", paths.getParamMax(oscPageIndex, oscIndex), randomizer.itemMax(i));
//      oscController.registerOscCallback(paths.getParamMax(oscPageIndex, oscIndex), "Sets the max value for the parameter", (oscConnection, oscMessage) -> {
//        randomizer.itemSetMax(i, oscMessage.getFloat(0));
//      });
//
//      // Locked Option
//      oscController.addLedBoolean("Randomizer", paths.getParamLocked(oscPageIndex, oscIndex), randomizer.itemLocked(i));
//      oscController.registerOscCallback(paths.getParamLocked(oscPageIndex, oscIndex), "Locks the parameter", (oscConnection, oscMessage) -> {
//        boolean bool = oscMessage.getTypeTag().equals(",T");
//        randomizer.itemSetLocked(i, bool);
//      });
//
//      // Randomize Individual Value
//      oscController.registerOscCallback(paths.getParamRandomize(oscPageIndex, oscIndex), "Locks the parameter", (oscConnection, oscMessage) -> {
//        randomizer.randomizeItem(i);
//      });
//    });
//
//    //Randomize All Values
//    oscController.registerOscCallback(paths.RANDOMIZE, "Randomizes all the parameters", (oscConnection, oscMessage) -> {
//      randomizer.randomizeParameters();
//    });
  }
}
