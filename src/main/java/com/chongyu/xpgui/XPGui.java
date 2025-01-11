package com.chongyu.xpgui;

import com.chongyu.xpgui.network.PlayerStatsServerPacket;
import net.fabricmc.api.ModInitializer;

public class XPGui implements ModInitializer {
    @Override
    public void onInitialize() {
        PlayerStatsServerPacket.init();
    }



}
