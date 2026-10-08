package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Banshee;
import gaia.entity.Witch;
import gaia.registry.GaiaRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.INFFGirlsGaiaChargeAttackingMob;
import net.sodiumzh.nff.girls.gaia.entity.IPotionThrower;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaFlyingChargeAttackGoal;
import net.sodiumzh.nff.girls.gaia.entity.ai.PotionThrowerGoals;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class GaiaWitchEntity extends Witch implements INFFGirlsTamed, IPotionThrower, IBlocksGaiaDynamicGoals, INFFGirlsGaiaChargeAttackingMob {

    protected static final EntityDataAccessor<Boolean> IS_CHARGING =
        SynchedEntityData.defineId(GaiaWitchEntity.class, EntityDataSerializers.BOOLEAN);

    public GaiaWitchEntity(EntityType<? extends GaiaWitchEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING, false);
    }

    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new PotionThrowerGoals.PotionEmergencySupportGoal(this, 1.8D, 60, 8.0F));
        goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.275d, true)
            .setStartCondition(g -> this.isMelee() && !this.isRidingBroom())
            .setInterruptCondition(g -> !this.isMelee() || this.isRidingBroom()));
        this.goalSelector.addGoal(3, new NFFGirlsGaiaFlyingChargeAttackGoal(this, 1.0D)
            .setInterruptChance(0.2d)
            .setStartCondition(g -> this.isMelee() && this.isRidingBroom())
            .setInterruptCondition(g -> !this.isMelee() || !this.isRidingBroom()));
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
        super.aiStep();
        // Witch will get debuff when carrying a too strong melee weapon
        // Calculate each 0.5s to save resource
        if (this.tickCount % 10 == 0) {
            Optional<Double> optMeleeAtk = this.getMeleeWeaponAtk();
            if (optMeleeAtk.isPresent()) {
                double atk = optMeleeAtk.get();
                if (atk >= 15.0d) {
                    this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, 1));
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, 1));
                } else if (atk >= 6.0d) {
                    this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19));
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19));
                }
            }
        }
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

    /**
     * If the mob is melee, return its weapon atk; otherwise return empty.
     */
    private Optional<Double> getMeleeWeaponAtk() {
        if (!(this.getMainHandItem().getItem() instanceof TieredItem ti)) return Optional.of(0d);
        double baseAtk = this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        double weaponAtk = ti.getAttributeModifiers(EquipmentSlot.MAINHAND, this.getMainHandItem())
            .get(Attributes.ATTACK_DAMAGE).stream().filter(Objects::nonNull)
            .mapToDouble(am -> {
                if (am.getOperation().equals(AttributeModifier.Operation.ADDITION)) return am.getAmount();
                // For multiply-total, apply to base damage only, as we don't know the exact atk
                else return am.getAmount() * baseAtk;
            }).sum();
        return weaponAtk >= 1d ? Optional.of(weaponAtk) : Optional.empty();
    }

    private boolean isMelee() {
        return getMeleeWeaponAtk().isPresent();
    }

    public boolean isCharging() {
        return this.entityData.get(IS_CHARGING);
    }

    public void setIsCharging(boolean charging) {
        this.entityData.set(IS_CHARGING, charging);
    }

    @Override
    public void playChargeAttackSound() {

    }

}
