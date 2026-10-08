package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Siren;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsBowShootingMob;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsBowAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsSkeletonMeleeAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaBowAttackGoal;
import net.sodiumzh.nff.girls.gaia.inventory.NFFGirlsGaiaBowShootingInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSkeletonInventoryMenu;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class GaiaSirenEntity extends Siren implements INFFGirlsTamed, IBlocksGaiaDynamicGoals, IHasRareVariant, INFFGirlsBowShootingMob {

    private static final EntityDataAccessor<Integer> RARE_VARIANT =
        SynchedEntityData.defineId(GaiaSirenEntity.class, EntityDataSerializers.INT);

    public GaiaSirenEntity(EntityType<? extends GaiaSirenEntity> entityType, Level level) {
        super(entityType, level);
        this.entityData.define(RARE_VARIANT, -1);
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

    /* Bow shooting related */

    private boolean justShot = false;

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        var absArrow = this.shoot(pTarget, pVelocity);
        if (!(absArrow instanceof Arrow arrow)) return;
        arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        justShot = true;
    }

    @Override
    public boolean canShoot() {
        return !this.getAdditionalInventory().orElseThrow().getItem(4).isEmpty()
            && this.getAdditionalInventory().orElseThrow().getItem(4).getItem() instanceof BowItem
            && !this.getAdditionalInventory().orElseThrow().getItem(8).isEmpty();
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
                checkSwitchingWeapons();
            }
        }
    }

    /* Bow shooting end */

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(RangedBowAttackGoal.class, MobAttackGoal.class);
    }

    // As this mob's inventory was 9, keep the 8/9th slots but don't allow any item in it
    // So that player can remove the arrows from the extra slot
    // TODO shrink to 7
    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(9, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsGaiaBowShootingInventoryMenu(containerId, playerInventory, container, this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("rareVariant", this.getRareVariantID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("rareVariant", Tag.TAG_ANY_NUMERIC))
            this.setRareVariantID(nbt.getInt("rareVariant"));
        // Port equipment for 0.x.34 inventory change
        // TODO remove in 0.x.35
        this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
        // Siren's default boots is a leather boot (see Siren class)
        ItemStack bootsDefault = new ItemStack(Items.LEATHER_BOOTS);
        bootsDefault.enchant(Enchantments.DEPTH_STRIDER, 2);
        this.setItemSlot(EquipmentSlot.FEET, bootsDefault);
    }

    private static final IHasRareVariant.RareVariant VARIANT_LADINA =
        new IHasRareVariant.RareVariant("ladina", 0.02d, "entity.nffgirlsgaia.variant.ladina");
    private static final List<IHasRareVariant.RareVariant> RARE_VARIANTS = List.of(VARIANT_LADINA);

    @Override
    public int getRareVariantID() {
        return this.entityData.get(RARE_VARIANT);
    }

    @Override
    public void setRareVariantID(int id) {
        this.entityData.set(RARE_VARIANT, id);
    }

    @Override
    public @Nullable IHasRareVariant.RareVariant rareVariantByID(int id) {
        return id >= 0 && id < RARE_VARIANTS.size() ? RARE_VARIANTS.get(id) : null;
    }

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
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


}
