package com.chongyu.xpgui;

import com.chongyu.xpgui.network.ClientSyncHandle;
import net.fabricmc.api.ClientModInitializer;

public class XPGuiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSyncHandle.init();////S2C数据包XPGuiXPSyncPayload客户端注册
    }
}
