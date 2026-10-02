package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Arachne;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAIUtils;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.gaia.entity.ai.PotionThrowerGoals;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEffects;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.reflection.CachedFieldAccessor;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class GaiaArachneEntity extends Arachne implements INFFGirlsTamed, IBlocksGaiaDynamicGoals {

    /*private static final EntityDataAccessor<Integer> RARE_VARIANT =
        SynchedEntityData.defineId(GaiaArachneEntity.class, EntityDataSerializers.INT);*/
    public static final double MELEE_ATTACK_THRESHOLD = 4.0d;

    public GaiaArachneEntity(EntityType<? extends GaiaArachneEntity> entityType, Level level) {
        super(entityType, level);
        this.navigation = new GaiaArachneEntity.Navigation(this, this.level());
        //this.entityData.define(RARE_VARIANT, -1);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new NFFGirlsRangedAttackGoal(this, 1.0D, 5 * 20, 16.0F) {
            @Override
            public void onStart() {
                super.onStart();
                this.getMob().getAdditionalInventory().orElseThrow().syncToMob(this.getMob().asMob());
                ItemStack mainHand = this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHand = this.getMob().asMob().getItemInHand(InteractionHand.OFF_HAND);
                if (!mainHand.is(NFFGirlsGaiaTags.WEAPON_STAFFS) && offHand.is(NFFGirlsGaiaTags.WEAPON_STAFFS)) {
                    this.getMob().asMob().setItemInHand(InteractionHand.MAIN_HAND, offHand);
                    this.getMob().asMob().setItemInHand(InteractionHand.OFF_HAND, mainHand);
                    this.getMob().getAdditionalInventory().orElseThrow().getFromMob(this.getMob().asMob());
                }
            }
            @Override
            public boolean checkCanUse() {
                this.getMob().getAdditionalInventory().orElseThrow().syncToMob(this.getMob().asMob());
                return super.checkCanUse() && NFFGirlsAIUtils.targetFurtherThan(this.getMob(), MELEE_ATTACK_THRESHOLD)
                    && (this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS)
                    || this.getMob().asMob().getItemInHand(InteractionHand.OFF_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS));
            }
        });
        goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1.275d, true) {
            @Override
            public void onStart() {
                super.onStart();
                this.getMob().getAdditionalInventory().orElseThrow().syncToMob(this.getMob().asMob());
                ItemStack mainHand = this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHand = this.getMob().asMob().getItemInHand(InteractionHand.OFF_HAND);
                if (mainHand.getDamageValue() < offHand.getDamageValue()) {
                    this.getMob().asMob().setItemInHand(InteractionHand.MAIN_HAND, offHand);
                    this.getMob().asMob().setItemInHand(InteractionHand.OFF_HAND, mainHand);
                    this.getMob().getAdditionalInventory().orElseThrow().getFromMob(this.getMob().asMob());
                }
            }

            @Override
            public boolean checkCanUse() {
                this.getMob().getAdditionalInventory().orElseThrow().syncToMob(this.getMob().asMob());
                return super.checkCanUse() && NFFGirlsAIUtils.targetCloserThan(this.getMob(), MELEE_ATTACK_THRESHOLD)
                || (!this.getMob().asMob().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS)
                    && !this.getMob().asMob().getItemInHand(InteractionHand.OFF_HAND).is(NFFGirlsGaiaTags.WEAPON_STAFFS));
            }
        });
        goalSelector.addGoal(7, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(8, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
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

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(RangedAttackGoal.class, MobAttackGoal.class);
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(4);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsTwoBaublesInventoryMenu(i, inventory, container, this);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        var proj = NFFGirlsGaiaProjectileProviders.WEB_BULLET_FRIENDED.apply(this);
        proj.setPos(this.getEyePosition());
        proj.shootTo(target.getBoundingBox().getCenter(), 0.8f, 1f);
        this.swing(InteractionHand.OFF_HAND);
        this.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(proj);
    }


    @Override
    public void aiStep() {
        super.aiStep();
        Player owner = this.getOwnerInDimension();
        if (owner != null && this.hasLineOfSight(owner) && owner.distanceToSqr(this) <= 256d) {
            owner.addEffect(new MobEffectInstance(NFFGirlsGaiaEffects.COBWEB_AFFINITY.get(), 10), this);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
    }


    // Block enemy features

    @Override
    protected void beaconMonster(int range, Consumer<LivingEntity> action) {
        super.beaconMonster(range, living -> {
            if (this.isTamedAlliedTo(living))
                action.accept(living);
        });
    }

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
    }

    @Override
    public boolean shouldSitOnWaiting() {
        return false;
    }

    protected static class Navigation extends WallClimberNavigation {

        private static final Field FIELD_PATH_TO_POSITION =
            NFUReflectionStatics.findFieldIfDeclared(WallClimberNavigation.class, "f_26578_")
                .orElseThrow();

        public Navigation(Mob pMob, Level pLevel) {
            super(pMob, pLevel);
        }

        @Override
        public void stop() {
            super.stop();
            NFUReflectionStatics.setValue(FIELD_PATH_TO_POSITION, this, null);
        }
    }


}
