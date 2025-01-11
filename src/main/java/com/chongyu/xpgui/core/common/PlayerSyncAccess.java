package com.chongyu.xpgui.core.common;

public interface PlayerSyncAccess {

    void syncStats(boolean syncDelay);

    void addXPBox(int add);
    void setXpBox(int add);
    boolean canPlus(int plus);


}