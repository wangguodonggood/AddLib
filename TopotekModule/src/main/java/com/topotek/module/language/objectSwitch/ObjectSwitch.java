package com.topotek.module.language.objectSwitch;

public abstract class ObjectSwitch<O> {

    protected abstract void switchOn();

    protected abstract void switchOff();

    protected abstract void sendObject(O o);

    protected abstract void on();

    protected abstract void off();

    protected abstract void receiveObject(O o);
}
