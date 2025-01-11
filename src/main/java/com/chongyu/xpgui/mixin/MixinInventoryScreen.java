package com.chongyu.xpgui.mixin;

import com.chongyu.xpgui.core.screen.XPScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    @Unique
    private static final ButtonTextures XP_BUTTON_TEXTURE = new ButtonTextures(Identifier.ofVanilla("xp_button"),Identifier.ofVanilla("xp_button_highlighted"));

    public MixinInventoryScreen(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "init", at = @At("RETURN"))
    protected void init(CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 75, this.height / 2 -76,20,18,XP_BUTTON_TEXTURE,
                (button -> {
                    if (this.client != null) {
                        MinecraftClient.getInstance().setScreen(new XPScreen(this.client,Text.translatable("xpgui.xpgui")));
                    }
                })));
    }

}

