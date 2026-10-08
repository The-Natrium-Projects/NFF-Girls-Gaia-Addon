package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Dryad;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaAiGoalGroups;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class GaiaDryadEntity extends Dryad implements INFFGirlsTamed, IBlocksGaiaDynamicGoals
{
	/* Data sync */

	/* Initialization */

	public GaiaDryadEntity(EntityType<? extends GaiaDryadEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		NFFGirlsGaiaAiGoalGroups.COMMON_MELEE.addTo(this, 0);
	}

	/* Interaction */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventory(4, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsFourBaublesInventoryMenu(containerId, playerInventory, container, this);
	}

	/* Save and Load */

	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
	}

	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}

	@Override
	public List<Class<? extends Goal>> getGoalsToRemove() {
		return List.of(MobAttackGoal.class, AvoidEntityGoal.class);
	}

	// This may cause mob losing target
	@Override
	public void stopBeingAngry() {
	}
}
