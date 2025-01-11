package com.chongyu.xpgui.network.networkings;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record XPGuiXPSyncPayload(int xpValue) implements CustomPayload {
	public static final PacketCodec<PacketByteBuf, XPGuiXPSyncPayload> CODEC
		= CustomPayload.codecOf(XPGuiXPSyncPayload::write, XPGuiXPSyncPayload::new);

	public static final CustomPayload.Id<XPGuiXPSyncPayload> ID = new Id<>(Identifier.of("xpgui", "xpvalue"));

	public XPGuiXPSyncPayload(PacketByteBuf buf) {
		this(buf.readInt());
	}

	public void write(PacketByteBuf buf) {
		buf.writeInt(xpValue);
	}

	public int getXpValue() {
		return xpValue;
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
