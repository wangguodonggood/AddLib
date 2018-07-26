package com.topotek.module.language.objectSwitch;

public abstract class ObjectStreamSwitch<O> extends ObjectSwitch<O> {

    private boolean isSynchronizedSendObject;

    private boolean isSwitch = false;
    private boolean isSwitchOnOrOff;

    private boolean isOnOrOff;

    public ObjectStreamSwitch(boolean isOnOrOff, boolean isSynchronizedSendObject) {
        this.isOnOrOff = isOnOrOff;
        this.isSynchronizedSendObject = isSynchronizedSendObject;
    }

    public boolean getIsOnOrOff() {
        return isOnOrOff;
    }

    @Override
    public void switchOn() {
        switchOnOrOff(true);
    }

    @Override
    public void switchOff() {
        switchOnOrOff(false);
    }

    @Override
    public void sendObject(O o) {
        if (isSynchronizedSendObject)
            synchronizedSendObject(o);
        else
            privateSendObject(o);
    }

    private synchronized void switchOnOrOff(boolean isSwitchOnOrOff) {
        if (!isSwitch) {
            this.isSwitchOnOrOff = isSwitchOnOrOff;
            isSwitch = true;
        }
    }

    private synchronized void synchronizedSendObject(O o) {
        privateSendObject(o);
    }

    private void privateSendObject(O o) {
        if (isSwitch) {
            if (isSwitchOnOrOff) {
                if (!isOnOrOff) {
                    on();
                    isOnOrOff = true;
                }
            } else {
                if (isOnOrOff) {
                    off();
                    isOnOrOff = false;
                }
            }
            isSwitch = false;
        }

        if (isOnOrOff)
            receiveObject(o);
    }

    @Override
    protected abstract void on();

    @Override
    protected abstract void off();

    @Override
    protected abstract void receiveObject(O o);
}
