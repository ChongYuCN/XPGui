package com.chongyu.xpgui;

import com.chongyu.xpgui.network.PlayerStatsClientPacket;
import net.fabricmc.api.ClientModInitializer;

public class XPGuiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlayerStatsClientPacket.init();
    }
}
