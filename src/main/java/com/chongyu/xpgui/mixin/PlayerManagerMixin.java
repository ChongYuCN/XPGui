package com.chongyu.xpgui.mixin;

import com.chongyu.xpgui.core.common.PlayerStatsManagerAccess;
import com.chongyu.xpgui.core.common.XPStates;
import com.chongyu.xpgui.network.PlayerStatsServerPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;onPlayerRespawned(Lnet/minecraft/server/network/ServerPlayerEntity;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void respawnPlayerMixin(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> info, BlockPos blockPos, float f, boolean bl, ServerWorld serverWorld,
                                    Optional<Object> optional, ServerWorld serverWorld2, ServerPlayerEntity serverPlayerEntity) {
        XPStates playerStatsManager = ((PlayerStatsManagerAccess) player).getPlayerStatsManager();
        XPStates serverPlayerStatsManager = ((PlayerStatsManagerAccess) serverPlayerEntity).getPlayerStatsManager();
        // Set on client
        PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, serverPlayerEntity);
        // Set on server
        serverPlayerStatsManager.setXpBox( playerStatsManager.getXp());
    }
}
