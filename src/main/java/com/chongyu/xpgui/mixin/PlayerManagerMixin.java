package com.chongyu.xpgui.mixin;

import com.chongyu.xpgui.core.XPStates;
import com.chongyu.xpgui.core.accessor.PlayerStatsManagerAccess;
import com.chongyu.xpgui.network.ServerSyncHandle;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;onPlayerRespawned(Lnet/minecraft/server/network/ServerPlayerEntity;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void respawnPlayerMixin(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir, TeleportTarget teleportTarget, ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, Vec3d vec3d, byte b, ServerWorld serverWorld2, WorldProperties worldProperties) {

        XPStates playerStatsManager = ((PlayerStatsManagerAccess) player).getPlayerStatsManager();
        XPStates serverPlayerStatsManager = ((PlayerStatsManagerAccess) serverPlayerEntity).getPlayerStatsManager();
        // Set on server
        serverPlayerStatsManager.setXpBox( playerStatsManager.getXp());
        // Set on client
        ServerSyncHandle.writeS2CXPPacket(playerStatsManager, serverPlayerEntity);
    }
}
