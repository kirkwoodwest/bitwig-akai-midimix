package com.kirkwoodwest.closedwoods.status;

//Runnable to execute the config,
//int delay to wait before executing the config
//Callback to confirm the config is successful or not

public record ConfigState(Runnable configFunction, int delay) {

}
