package com.chongyu.xpgui.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class XPStates {
    public final PlayerEntity playerEntity;
    public static final int MAX_XP = 10000;
    public int xp;

    public XPStates(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public void readNbt(NbtCompound tag) {
        this.xp = tag.getInt("XPBox");
    }
    public void writeNbt(NbtCompound tag) {
        tag.putInt("XPBox", this.xp);
    }

    public int getXp() {
        return xp;
    }
    public void setXpBox(int b) {
        this.xp = b;
    }

    public void addXPBox(int add) {
        if(add + this.xp <= MAX_XP ){
            this.playerEntity.addExperience(-add);
            this.xp += add;
        }else {
            this.playerEntity.addExperience(-(MAX_XP - this.xp));
            this.xp = MAX_XP;
        }

    }

    public boolean canPlus(int plus) {
        if(plus > 0 ){
            return this.playerEntity.totalExperience >= plus && this.xp<= MAX_XP-plus;
        }else if(plus<0){
            return  this.xp >= -plus;
        }
        return false;
    }

    public boolean canPlusTotal() {
        return playerEntity.totalExperience> 0;
    }


    public  int GetMaxXp() {
        return MAX_XP;
    }
}
