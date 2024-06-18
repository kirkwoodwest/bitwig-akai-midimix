package com.kirkwoodwest.openwoods.misc;

import com.bitwig.extension.controller.api.Action;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PrintActions {

  public static void print(ControllerHost host){
    final Application app = host.createApplication();
    final Action[] list;
    list = app.getActions();
    Arrays.sort(list, Comparator.comparing(Action::getName));
    host.println("|---|---|");
    for (Action action : list) {
      host.println("|" + action.getName() + "|`" + action.getId() + "`|");
    }
    host.println("|---|---|");
  }

  public static void print(ControllerHost host, int totalPages, int currentPage){
    final Application app = host.createApplication();
    final Action[] list;
    list = app.getActions();
    Arrays.sort(list, Comparator.comparing(Action::getName));

    List<Action> paginated = paginate(list, totalPages, currentPage);
    host.println("|Action Name|Action ID|");
    host.println("|---|---|");

    for (Action action : paginated) {
      host.println("|" + action.getName() + "|`" + action.getId() + "`|");
    }
    host.println("|---|---|");
  }



  // Function to paginate content
  private static List<Action> paginate(Action[] content, int totalPages, int currentPage) {
    List<Action> pageContent = new ArrayList<>();
    int totalItems = content.length;
    int itemsPerPage = totalItems / totalPages;
    int startIndex = (currentPage - 1) * itemsPerPage;
    int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

    for (int i = startIndex; i < endIndex; i++) {
      pageContent.add(content[i]);
    }

    return pageContent;
  }
}