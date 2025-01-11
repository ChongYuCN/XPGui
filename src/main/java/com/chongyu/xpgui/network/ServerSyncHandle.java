package com.chongyu.xpgui.network;

import com.chongyu.xpgui.core.XPStates;
import com.chongyu.xpgui.network.networkings.XPGuiXPSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

/*
xpgui网络通信需要发两个包：
第一个：C2S，客户端向服务端发送：经验消耗，xp值
第二个：S2C，服务端向客户端：xp值
该类为S2C的注册及玩家同步。
 */
public class ServerSyncHandle {
	//在mod主类main类的onInitializeClient()方法调用【注册】
	//服务端向客户端发送数据包：服务端注册【同步xpgui的xp值】
	public static void init() {
		PayloadTypeRegistry.playS2C().register(XPGuiXPSyncPayload.ID, XPGuiXPSyncPayload.CODEC);
	}

	//同步box,服务端发包同步数据：当玩家经验发生变化时更新【同步xpgui的xp值】
	public static void writeS2CXPPacket(XPStates playerStatsManager, ServerPlayerEntity serverPlayerEntity) {
		int xpValue = playerStatsManager.getXp();
		ServerPlayNetworking.send(serverPlayerEntity, new XPGuiXPSyncPayload(xpValue));
	}

	//更新经验仓库数值
	public static void updateXPBox(XPStates playerStatsManager, int experience) {
		playerStatsManager.addXPBox(experience);
		playerStatsManager.setXpBox(playerStatsManager.getXp());
	}
}
