package com.chongyu.xpgui;

import com.chongyu.xpgui.network.ServerSyncHandle;
import com.chongyu.xpgui.network.XPGuiPack;
import com.chongyu.xpgui.network.networkings.XPGUIC2SPack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketDecoder;
import net.minecraft.network.packet.CustomPayload;

public class XPGui implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerSyncHandle.init();//S2C数据包XPGuiXPSyncPayload服务端注册
        registerPacketReader(XPGUIC2SPack.XP_VALUE, XPGUIC2SPack::new);//C2S数据包XPGUIC2SPack注册（只需服务端注册即可）
    }


    private <T extends XPGuiPack> void registerPacketReader(CustomPayload.Id<T> id, PacketDecoder<RegistryByteBuf, T> decode) {
        PayloadTypeRegistry.playC2S().register(id, PacketCodec.ofStatic((buf, v) -> v.write(buf), decode));
        ServerPlayNetworking.registerGlobalReceiver(id, (payload, context) -> {
            context.player().getServer().execute(() -> {
                ((XPGuiPack)payload).apply(context.player());
            });
        });
    }
}
