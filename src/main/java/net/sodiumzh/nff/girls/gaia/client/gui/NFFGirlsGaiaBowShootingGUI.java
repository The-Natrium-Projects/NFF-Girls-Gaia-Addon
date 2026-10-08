package net.sodiumzh.nff.girls.gaia.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsGUIPreset0;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nfu.math.GuiPos;

public class NFFGirlsGaiaBowShootingGUI extends NFFGirlsGUIPreset0 {

    public NFFGirlsGaiaBowShootingGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob) {
        super(menu, playerInventory, mob);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
        this.addMainScreen(graphics);
        this.addSlotBg(graphics, 0, rightRowPos().slotBelow(3), 1, 1);
        this.addSlotBg(graphics, 1, leftRowPos().slotBelow(3), 1, 0);
        this.addBaubleSlotBg(graphics, 2, leftRowPos());
        this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(1));
        this.addBaubleSlotBg(graphics, 4, leftRowPos().slotBelow(2));
        this.addSlotBg(graphics, 5, rightRowPos().slotBelow(2), 2, 1);
        this.addSlotBg(graphics, 6, rightRowPos(), 2, 0);
        this.addMobRenderBox(graphics, 2);
        this.addInfoBox(graphics);
        this.addAttributeInfo(graphics, infoPos());
        this.renderMob(graphics, GuiPos.valueOf(0, 3));
    }
}
