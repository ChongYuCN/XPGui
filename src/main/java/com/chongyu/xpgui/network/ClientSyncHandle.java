package com.chongyu.xpgui.network;

import com.chongyu.xpgui.core.XPStates;
import com.chongyu.xpgui.core.accessor.PlayerStatsManagerAccess;
import com.chongyu.xpgui.network.networkings.XPGUIC2SPack;
import com.chongyu.xpgui.network.networkings.XPGuiXPSyncPayload;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import java.util.Objects;

/*
xpgui网络通信需要发两个包：
第一个：C2S，客户端向服务端发送：经验消耗，xp值
第二个：S2C，服务端向客户端：xp值
该类为S2C的注册，和C2S的调用
 */
public class ClientSyncHandle
{
	//在客户端main类的onInitializeClient()方法调用【注册】
	//服务端向客户端发送的数据包：客户端注册。【同步xugui的xp值】XPGuiXPSyncPayload
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(XPGuiXPSyncPayload.ID, (payload, context) -> {
			PacketByteBuf newBuffer = new PacketByteBuf(Unpooled.buffer());
			newBuffer.writeInt(payload.getXpValue());
			context.client().execute(() -> {
				XPStates playerStatsManager = ((PlayerStatsManagerAccess) context.player()).getPlayerStatsManager();
				playerStatsManager.setXpBox(newBuffer.readInt());
			});
		});
	}

	//C2S的调用客户端数据包XPGUIC2SPack
	//向xp仓库存储时调用，即XPScreen中按钮执行时
	//客户端向服务端发送数据：当按下+—xpgui按钮时调用
	public static void writeC2SXPPacket(XPStates playerStatsManager,int experience) {
		//方法二：客户端向服务端发送数据：当按下+—xpgui按钮时调用
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeInt(experience);
		buf.writeInt(playerStatsManager.getXp());

		CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(new XPGUIC2SPack(buf));
		Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(packet);
	}
}
