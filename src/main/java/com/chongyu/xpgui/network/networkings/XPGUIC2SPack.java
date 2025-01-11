package com.chongyu.xpgui.network.networkings;

import com.chongyu.xpgui.core.XPStates;
import com.chongyu.xpgui.core.accessor.PlayerStatsManagerAccess;
import com.chongyu.xpgui.network.XPGuiPack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

//客户端向服务端发送的数据包：存储时消耗的经验值，经验仓库的存储值
public class XPGUIC2SPack implements XPGuiPack {
    public static final CustomPayload.Id<XPGUIC2SPack> XP_VALUE = new Id<>(Identifier.of("xpgui:xp_value"));

    private final int costXp;
    private final int xpValue;

    public XPGUIC2SPack(int costXp,int xpValue) {
        this.costXp = costXp;
        this.xpValue = xpValue;
    }

    public XPGUIC2SPack(RegistryByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    public XPGUIC2SPack(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    @Override
    public void write(RegistryByteBuf buf) {
        buf.writeInt(costXp);
        buf.writeInt(xpValue);
    }

    @Override
    public void apply(PlayerEntity player) {
        XPStates playerStatsManager = ((PlayerStatsManagerAccess) player).getPlayerStatsManager();
        playerStatsManager.addXPBox(costXp);
        playerStatsManager.setXpBox(xpValue);
    }

    @Override
    public Id<XPGUIC2SPack> getId() {
        return XP_VALUE;
    }
}
