package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Mandragora;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaAiGoalGroups;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GaiaMandragoraEntity extends Mandragora implements INFFGirlsTamed {
    public GaiaMandragoraEntity(EntityType<? extends GaiaMandragoraEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventory(4, this);
    }

    @Override
    public @Nullable NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsFourBaublesInventoryMenu(i, inventory, container, this);
    }

    @Override
    protected void registerGoals() {
        NFFGirlsGaiaAiGoalGroups.COMMON_MELEE.addTo(this, 0);
    }


    /**
     * Disable this method to prevent nausea to players. Use doBeaconMonster() instead.
     */
    @Override
    protected void beaconMonster(int range, Consumer<LivingEntity> action) {
        return;
    }

    protected void doBeaconMonster(int range, Consumer<LivingEntity> action) {
        super.beaconMonster(range, action);
    }

    private int getDebuffRange() {
        return Math.min(2 + this.getXpLevel() / 10, 16);
    }

    public void aiStep() {
        super.aiStep();
        this.setIsScreaming(this.getTarget() != null);
        if (this.isScreaming()) {
            this.doBeaconMonster(getDebuffRange(), (living) -> {
                if (living instanceof Mob mob && this.isTamedAlliedTo(mob.getTarget())) {
                    int xpLevel = this.getXpLevel();
                    living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 99, 0, true, true));
                    if (xpLevel >= 10)
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 99, 0, true, true));
                    if (xpLevel >= 30)
                        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 99, 0, true, true));
                    if (xpLevel >= 50)
                        living.addEffect(new MobEffectInstance(MobEffects.WITHER, 99, 0, true, true));
                }
            });
        }
    }
}
