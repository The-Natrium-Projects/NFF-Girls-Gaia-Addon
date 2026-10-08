package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Centaur;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsBowShootingMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsBowAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaBowAttackGoal;
import net.sodiumzh.nff.girls.gaia.inventory.NFFGirlsGaiaBowShootingInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSkeletonInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

import java.util.Arrays;

public class GaiaCentaurEntity extends Centaur implements INFFGirlsBowShootingMob {
    public GaiaCentaurEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    /* AI */

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new NFFRestrictSunGoal(this));
        goalSelector.addGoal(2, new NFFFleeSunGoal(this, 1));
        goalSelector.addGoal(3, new NFFGirlsGaiaBowAttackGoal(this, 1.0D, 20, 15.0F));
        goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1.2d, true));
        goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));

    }

    @Override
    public boolean enableSunSensitivity() {
        return true;
    }

    /* Bow shooting related */

    private boolean justShot = false;

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        var arrow = this.shoot(pTarget, pVelocity);
        if (arrow == null) return;
        justShot = true;
    }

    @Override
    public boolean canShoot() {
        return !this.getAdditionalInventory().orElseThrow().getItem(this.getMainHandItemSlotIndex()).isEmpty()
            && this.getAdditionalInventory().orElseThrow().getItem(this.getMainHandItemSlotIndex()).getItem() instanceof BowItem
            && !this.getAdditionalInventory().orElseThrow().getItem(this.getArrowSlotIndex()).isEmpty();
    }


    @Override
    public void aiStep() {
        super.aiStep();
        /* Handle combat AI */
        if (!this.level().isClientSide)
        {
            if (justShot)
            {
                this.postShoot();
                justShot = false;
            }

            if (this.getTarget() != null) {
                this.checkSwitchingWeapons();
            }
        }
    }

    @Override
    public int getMainHandItemSlotIndex() {
        return 0;
    }

    @Override
    public int getSecondaryWeaponSlotIndex() {
        return 5;
    }

    @Override
    public int getArrowSlotIndex() {
        return 6;
    }

    /* Bow shooting end */

    /* Inventory */

    // TODO shrink to 7. 8 and 9 don't do anything
    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        // 0 mainhand; 1 offhand; 2~4 baubles; 5 swappable weapon; 6 arrows; 78 nothing
        return new NFFTamedMobInventoryWithHandItems(9, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsGaiaBowShootingInventoryMenu(containerId, playerInventory, container, this);
    }

    // Sounds

    @Override
    protected SoundEvent getAmbientSound()
    {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

    @Override
    public boolean shouldSitOnWaiting() {
        return false;
    }
}
