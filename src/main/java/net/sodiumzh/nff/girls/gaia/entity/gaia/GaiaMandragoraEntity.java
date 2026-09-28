package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Mandragora;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import org.jetbrains.annotations.Nullable;

public class GaiaMandragoraEntity extends Mandragora implements INFFGirlsTamed {
    public GaiaMandragoraEntity(EntityType<? extends GaiaMandragoraEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable NFFTamedMobInventory createAdditionalInventory() {
        return null;
    }

    @Override
    public @Nullable NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return null;
    }
}
