package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Banshee;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.INFFGirlsGaiaChargeAttackingMob;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaFlyingChargeAttackGoal;
import net.sodiumzh.nff.girls.gaia.entity.ai.VexLikeMoveControl;
import net.sodiumzh.nff.girls.inventory.NFFGirlsThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GaiaBansheeEntity extends Banshee implements INFFGirlsTamedSunSensitiveMob, INFFGirlsGaiaChargeAttackingMob {
    /* Initialization */

    public GaiaBansheeEntity(EntityType<? extends GaiaBansheeEntity> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
        this.moveControl = new VexLikeMoveControl(this).setVerticalDeceleration(0.9d);
    }

    /* AI */

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new NFFGirlsGaiaFlyingChargeAttackGoal(this, 1.0D).setInterruptChance(0.2d));
        this.goalSelector.addGoal(5, new NFFFlyingLandGoal(this).setHeightOffset(2.0d).setUsesNavigation(false));
        this.goalSelector.addGoal(6, new NFFGirlsFlyingFollowOwnerGoal(this, 1.0d).setUsesNavigation(false));
        this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this).heightLimit(7).setUsesNavigation(false));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
    }

    /* Interaction */

    /** Inventory **/

    // This enables mob armor and hand items by default.
    // If not needed, use NFFTamedMobInventory class instead.

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventory(3, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsThreeBaublesInventoryMenu(containerId, playerInventory, container, this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

    @Override
    public void playChargeAttackSound() {
        this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
    }

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
    }

}
