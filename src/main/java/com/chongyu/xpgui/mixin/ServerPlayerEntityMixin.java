package com.chongyu.xpgui.mixin;

import com.chongyu.xpgui.core.common.PlayerStatsManagerAccess;
import com.chongyu.xpgui.core.common.PlayerSyncAccess;
import com.chongyu.xpgui.core.common.XPStates;
import com.chongyu.xpgui.network.PlayerStatsServerPacket;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerSyncAccess {
    @Unique
    private final XPStates playerStatsManager = ((PlayerStatsManagerAccess) this).getPlayerStatsManager();

    @Unique
    private int syncedLevelExperience = -99999999;
    @Unique
    private boolean syncTeleportStats = false;
    @Unique
    private int tinySyncTicker = 0;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Inject(method = "playerTick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;totalExperience:I", ordinal = 0, shift = At.Shift.BEFORE))
    private void playerTickMixin(CallbackInfo info) {
        if (playerStatsManager.getXp() != this.syncedLevelExperience) {
            this.syncedLevelExperience = playerStatsManager.getXp();

            if (this.syncTeleportStats) {
                this.syncTeleportStats = false;
                PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
            }
            PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, ((ServerPlayerEntity) (Object) this));
        }
        if (this.tinySyncTicker > 0) {
            this.tinySyncTicker--;
            if (this.tinySyncTicker % 20 == 0) {
                syncStats(false);
            }
        }
    }

    @Inject(method = "onSpawn", at = @At(value = "TAIL"))
    private void onSpawnMixin(CallbackInfo info) {
        PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "copyFrom", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;syncedExperience:I", ordinal = 0))
    private void copyFromMixin(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        syncStats(false);
    }

    @Override
    public void syncStats(boolean syncDelay) {
        this.syncTeleportStats = true;
        this.syncedLevelExperience = -1;
        if (syncDelay)
            this.tinySyncTicker = 40;
    }

    @Override
    public void addXPBox(int add) {
        this.addExperience(-add);
        playerStatsManager.xp += add;

        PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
//        playerEntity.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, playerEntity));
    }

    @Override
    public void setXpBox(int add) {
        playerStatsManager.xp = add;
    }

    @Override
    public boolean canPlus(int plus) {
        if(plus > 0 ){
            return this.totalExperience >= plus && playerStatsManager.xp<= playerStatsManager.GetMaxXp()-plus;
        }else if(plus<0){
            return  playerStatsManager.xp >= -plus;
        }
        return false;
    }
}
