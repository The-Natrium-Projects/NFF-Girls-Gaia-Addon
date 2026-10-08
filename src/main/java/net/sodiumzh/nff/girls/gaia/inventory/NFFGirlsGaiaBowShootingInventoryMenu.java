package net.sodiumzh.nff.girls.gaia.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.inventory.NFFGirlsInventoryMenuPreset0;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.math.GuiPos;

import java.util.function.Predicate;

public class NFFGirlsGaiaBowShootingInventoryMenu extends NFFGirlsInventoryMenuPreset0 {

    public NFFGirlsGaiaBowShootingInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob) {
        super(containerId, playerInventory, container, mob);
    }

    protected void addMenuSlots() {
        this.addGeneralSlot(0, this.rightRowPos().slotBelow(3), null);
        this.addGeneralSlot(1, this.leftRowPos().slotBelow(3), null);
        this.addBaubleSlot(2, this.leftRowPos(), "0");
        this.addBaubleSlot(3, this.leftRowPos().slotBelow(1), "1");
        this.addBaubleSlot(4, this.leftRowPos().slotBelow(2), "2");
        this.addGeneralSlot(5, this.rightRowPos().slotBelow(2), null);
        this.addGeneralSlot(6, this.rightRowPos(), (stack) -> stack.getItem() instanceof ArrowItem);
        this.addGeneralSlot(7, this.rightRowPos().slotLeft(), stack -> false);
        this.addGeneralSlot(8, this.rightRowPos().slotLeft().slotBelow(), stack -> false);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        int[] order = new int[]{6, 2, 3, 4, 0, 1, 5};
        return this.quickMovePreset(order.length, player, index, order);
    }

    protected GuiPos getPlayerInventoryPosition() {
        return GuiPos.valueOf(32, 101);
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.container.stopOpen(pPlayer);
    }
}
