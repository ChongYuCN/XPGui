package com.chongyu.xpgui.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;

public interface XPGuiPack extends CustomPayload {
    void write(RegistryByteBuf buf);

    void apply(PlayerEntity player);
}
