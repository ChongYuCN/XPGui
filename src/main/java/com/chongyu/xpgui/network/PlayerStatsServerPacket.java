package com.chongyu.xpgui.network;

import com.chongyu.xpgui.core.common.PlayerSyncAccess;
import com.chongyu.xpgui.core.common.XPStates;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerStatsServerPacket {
    public static final Identifier XP_PACKET = new Identifier("xpgui", "player_xp_box");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(XP_PACKET, (server, player, handler, buffer, sender) -> {
            if (player == null)
                return;
            ((PlayerSyncAccess) player).addXPBox(buffer.readInt());
            ((PlayerSyncAccess) player).setXpBox(buffer.readInt());
        });

    }

    //同步box
    public static void writeS2CXPPacket(XPStates playerStatsManager, ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(playerStatsManager.getXp());

        ServerPlayNetworking.send(serverPlayerEntity,XP_PACKET, buf);
    }

    public static void writeS2CXPPacket2(XPStates playerStatsManager, int experience) {
        playerStatsManager.addXPBox(experience);
        playerStatsManager.setXpBox(playerStatsManager.getXp());
    }
}
