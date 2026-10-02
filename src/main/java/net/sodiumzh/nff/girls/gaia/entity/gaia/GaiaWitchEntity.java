package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Witch;
import gaia.registry.GaiaRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IPotionThrower;
import net.sodiumzh.nff.girls.gaia.entity.ai.PotionThrowerGoals;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class GaiaWitchEntity extends Witch implements INFFGirlsTamed, IPotionThrower, IBlocksGaiaDynamicGoals {

    public GaiaWitchEntity(EntityType<? extends GaiaWitchEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new PotionThrowerGoals.PotionEmergencySupportGoal(this, 1.8D, 60, 8.0F));
        goalSelector.addGoal(4, new PotionThrowerGoals.PotionAttackGoal(this, 1.2D, 60, 8.0F).setInterruptChance(0.2d));
        goalSelector.addGoal(4, new PotionThrowerGoals.PotionSupportGoal(this, 1.2D, 60, 8.0F).setInterruptChance(0.2d));
        goalSelector.addGoal(5, new PotionThrowerGoals.PotionIdleSupportGoal(this, 1.2D, 60, 8.0F));
        this.goalSelector.addGoal(6, new NFFFlyingLandGoal(this) {
            public boolean checkCanUse() {return super.checkCanUse() && isRidingBroom();}
        });
        this.goalSelector.addGoal(7, new NFFGirlsFlyingFollowOwnerGoal(this, 1.5d){
            public boolean checkCanUse() {return super.checkCanUse() && isRidingBroom();}
        }.setHoveringHeightOffset(-0.5d));
        this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this){
            public boolean checkCanUse() {return super.checkCanUse() && isRidingBroom();}
        }.heightLimit(7));
        goalSelector.addGoal(7, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false){
            public boolean checkCanUse() {return super.checkCanUse() && !isRidingBroom();}
        });
        goalSelector.addGoal(8, new NFFWaterAvoidingRandomStrollGoal(this, 1.2d){
            public boolean checkCanUse() {return super.checkCanUse() && !isRidingBroom();}
        });
        goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.throwPotion(target, distanceFactor, true);
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(NearestAttackableTargetGoal.class);
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(6, this);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsFourBaublesDefaultInventoryMenu(i, inventory, container, this);
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack stack) {
        super.setItemSlot(equipmentSlot, stack);
        // Reset to enable broom tag
        if (equipmentSlot == EquipmentSlot.OFFHAND) {
            boolean isRidingBroom = stack.is(GaiaRegistry.BROOM.get()) || stack.is(NFFGirlsGaiaTags.WEAPON_BROOMS);
            this.setRidingBroom(isRidingBroom);
            if (!isRidingBroom && this.isNoGravity()) {
                this.setNoGravity(false);
            }
            this.moveControl = isRidingBroom ? this.flyingControl : this.groundControl;
            this.navigation = isRidingBroom ? this.flyingNavigation : this.groundNavigation;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep(); // Make GaiaWitchEntity.aiStep present in stacktrace, so zombie spawning can be blocked by stack walking
    }

    public void setDeltaMovement(Vec3 v) {
        super.setDeltaMovement(v);
    }

    @Override
    protected void beaconMonster(int range, Consumer<LivingEntity> action) {
        super.beaconMonster(range, living -> {
            if (this.isTamedAlliedTo(living))
                action.accept(living);
        });
    }

}
