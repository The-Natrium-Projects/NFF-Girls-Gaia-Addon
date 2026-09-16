package net.sodiumzh.nff.girls.gaia.eventlistener;

import gaia.GrimoireOfGaia;
import gaia.capability.CapabilityHandler;
import gaia.entity.AbstractAssistGaiaEntity;
import gaia.entity.AbstractGaiaEntity;
import gaia.entity.Arachne;
import gaia.entity.type.IDayMob;
import gaia.item.edible.MonsterFeedItem;
import gaia.registry.GaiaRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.tamingprocess.HmagBansheeTamingProcess;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.gaia.entity.NFFGirlsGaiaEntityUtils;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaArachneEntity;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaMummyEntity;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaShamanEntity;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaWitchEntity;
import net.sodiumzh.nff.girls.gaia.event.GaiaMobFinalizeSpawnEvent;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaConfigs;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEntityTypes;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaItems;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.event.entity.NFFMobTamedEvent;
import net.sodiumzh.nfu.exception.ReflectionFailedException;
import net.sodiumzh.nfu.mixin.event.entity.ItemEntityHurtEvent;
import net.sodiumzh.nfu.mixin.event.entity.LivingStartBaseAiStepEvent;
import net.sodiumzh.nfu.mixin.event.entity.MobRegisterGoalsEvent;
import net.sodiumzh.nfu.util.NFUAIStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsGaiaEntityEventListeners
{
	// BM events
	@SubscribeEvent
	public static void onTamed(NFFMobTamedEvent event)
	{
		if (event.mobBefore instanceof AbstractGaiaEntity before 
				&& INFFGirlsTamed.get(event.mobBefriended).isPresent()
				&& event.mobBefriended instanceof AbstractGaiaEntity after)
		{
			after.setBaby(before.isBaby());
			after.setVariant(before.getVariant());
			NFFGirlsGaiaEntityUtils.setMale(after, NFFGirlsGaiaEntityUtils.isMale(before));
			if (after instanceof IHasRareVariant hasRV) {
				int vid = hasRV.pickRareVariant();
				IHasRareVariant.RareVariant v = hasRV.rareVariantByID(vid);
				if (v != null) {
					hasRV.setRareVariantID(vid);
					after.setCustomName(v.displayName());
				}
			}
			INFFGirlsTamed.get(after).ifPresent(tamed -> {
				after.getCapability(CapabilityHandler.CAPABILITY_FRIENDED).ifPresent((cap) -> {
					cap.setFriendly(true);
					cap.setFriendedBy(tamed.getOwnerUUID());
					after.setPersistenceRequired();
				});
			});
		}
	}

	// TODO this event listener is merged from HmagBansheeTamingProcess as the listener in that class only handle HMaG banshee. Merge this to NFFGirls and remove in the next version.
	@Deprecated
	@SubscribeEvent
	public static void preventWitherInProcess(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect().equals(MobEffects.WITHER)) {
			LivingEntity var2 = event.getEntity();
			if (var2 instanceof Mob e) {
				if ((Boolean) NFFTamableComponent.getOptional(e).map((tamable) -> {
					return tamable.getTamingProcess() instanceof HmagBansheeTamingProcess;
				}).orElse(false) && NFFTamableComponent.getOrDefault(e).getTamingProcess().isInAnyProcess(e)) {
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}

	private static final Field GAIA_TARGET_PLAYER_GOAL = NFUReflectionStatics.findFieldIfDeclared(AbstractGaiaEntity.class,
		"targetPlayerGoal").orElseThrow();
	private static final Field GAIA_TARGET_MOB_GOAL = NFUReflectionStatics.findFieldIfDeclared(AbstractGaiaEntity.class,
		"targetMobGoal").orElseThrow();

	@SubscribeEvent
	public static void onJoinLevel(EntityJoinLevelEvent event) {
		// Prevent minion spawn from friended mobs, searching by stack walking
		if (event.getEntity().getType().equals(GaiaRegistry.GRAVEMITE.getEntityType())) {
			if (StackWalker.getInstance().walk(frames ->
				frames.anyMatch(frame -> frame.getClassName().equals(GaiaMummyEntity.class.getName())))) {
				event.setCanceled(true); return;
			}
		}
		else if (event.getEntity() instanceof Mob mob
			&& (mob.getType().equals(EntityType.ZOMBIE) || mob.getType().equals(EntityType.SKELETON)))
		{
			if (mob.getItemBySlot(EquipmentSlot.HEAD).is(GaiaRegistry.HEADGEAR_MOB.get())
				&& NFUReflectionStatics.isRunningInClass(GaiaWitchEntity.class)) {
				event.setCanceled(true); return;
			}
			if (mob.getItemBySlot(EquipmentSlot.HEAD).is(GaiaRegistry.HEADGEAR_BOLT.get())
				&& NFUReflectionStatics.isRunningInClass(GaiaShamanEntity.class)) {
				event.setCanceled(true); return;
			}
		}
		else if (event.getEntity() instanceof Mob mob
			&& mob.getType().equals(EntityType.CAVE_SPIDER))
		{
			if (!mob.level.getEntitiesOfClass(GaiaArachneEntity.class, mob.getBoundingBox().inflate(3d, 3d, 3d)).isEmpty()
				&& NFUReflectionStatics.isRunningInClass(GaiaArachneEntity.class)) {
				event.setCanceled(true); return;
			}
		}
		// Handle disabling male
		if (event.getEntity() instanceof AbstractGaiaEntity gaiaEntity
			&& !NFFGirlsGaiaConfigs.ValueCache.Tweak.SPAWNS_MALE_MOBS
			&& NFFGirlsGaiaEntityUtils.isMale(gaiaEntity)
			&& StackWalker.getInstance().walk(frames ->
				frames.anyMatch(frame -> frame.getClassName().equals(NaturalSpawner.class.getName()) || frame.getClassName().equals(BaseSpawner.class.getName())))) {
			NFFGirlsGaiaEntityUtils.setMale(gaiaEntity, false);
		}
		// Remove targeting player/mob goals
		if (event.getEntity() instanceof AbstractGaiaEntity e && INFFGirlsTamed.get(event.getEntity()).isPresent()) {
			try {
				e.targetSelector.removeGoal((Goal) (GAIA_TARGET_PLAYER_GOAL.get(e)));
				e.targetSelector.removeGoal((Goal) (GAIA_TARGET_MOB_GOAL.get(e)));
				GAIA_TARGET_PLAYER_GOAL.set(e, new TargetGoal(e, false) {
					@Override
					public boolean canUse() {
						return false;
					}
				});
				GAIA_TARGET_MOB_GOAL.set(e, new TargetGoal(e, false) {
					@Override
					public boolean canUse() {
						return false;
					}
				});
			} catch (IllegalAccessException ex) {
				throw new ReflectionFailedException(ex);
			}
		}
	}

	@SubscribeEvent
	public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getEntity() instanceof AbstractGaiaEntity e && event.getSpawnReason().equals(MobSpawnType.NATURAL)) {

			if (event.getEntity().getType().equals(GaiaRegistry.CECAELIA.getEntityType())
				&& event.getEntity().getRandom().nextDouble() > NFFGirlsGaiaConfigs.ValueCache.Tweak.CECAELIA_SPAWN_RATE) {
				event.setResult(Event.Result.DENY);
				return;
			} else if (event.getEntity().getType().is(NFFGirlsGaiaTags.CAN_DISABLE_DAY_SPAWN)
				&& !NFFGirlsGaiaConfigs.ValueCache.Tweak.ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND) {
				event.setResult(Event.Result.DENY);
				return;
			}
		}
	}

	public static void onFinalizeSpawn(LivingSpawnEvent.SpecialSpawn event) {

	}

	@SubscribeEvent
	public static void onMobInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getItemStack().getItem() instanceof MonsterFeedItem) {
			INFFGirlsTamed.get(event.getTarget()).ifPresentOrElse(t -> {
				int amount = 0;
				if (event.getItemStack().is(GaiaRegistry.MONSTER_FEED.get()))
					amount = 10;
				else if (event.getItemStack().is(GaiaRegistry.PREMIUM_MONSTER_FEED.get()))
					amount = 100;
				if (amount > 0) {
					if (!event.getEntity().getLevel().isClientSide) {
						t.getDataAccessor().addXP(amount);
						NFUParticleStatics.sendGlintParticlesToEntityDefault(t.asMob());
						event.getEntity().getItemInHand(event.getHand()).shrink(1);
					}
				}
				event.setCanceled(true);
				event.setCancellationResult(amount == 0 ? InteractionResult.PASS : InteractionResult.sidedSuccess(event.getEntity().level.isClientSide));
			}, () -> {
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.PASS);
			});
		}
		// Allow removing mygo variant
		if (event.getItemStack().is(NFFGirlsGaiaItems.EVIL_GRINDSTONE.get())
			&& event.getTarget() instanceof Mob mob
			&& event.getTarget() instanceof IHasRareVariant v
			&& event.getEntity().isShiftKeyDown())
		{
			boolean done = false;
			int varId = v.getRareVariantID();
			@Nullable IHasRareVariant.RareVariant rv = v.getRareVariant().orElse(null);
			if (rv != null) {
				v.setRareVariantID(-1);
				done = true;
			}
			if (mob.getCustomName() != null
				&& rv != null
				&& mob.getCustomName().getString().equals(rv.displayName().getString())) {
				mob.setCustomName(null);
				done = true;
			}
			if (done) {
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.sidedSuccess(event.getEntity().level.isClientSide));
			}
		}
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		// Cancel explosion damages
		if ((event.getSource().isExplosion())
			&& INFFTamed.get(event.getSource().getEntity()).filter(t -> NFFTamedStatics.isLivingAlliedToBM(t, event.getEntity())).isPresent())
		{
			if (event.getSource().getEntity().getType().equals(NFFGirlsGaiaEntityTypes.GAIA_VALKYRIE.get()))
				event.setCanceled(true);
		}
	}


/*
	public static final Field FIELD_MOB_MOVE_CONTROL = NFUReflectionStatics.findFieldIfDeclared(Mob.class, "f_21342_")
		.orElseThrow(() -> new ReflectionFailedException("field not found"));
	public static final Field FIELD_WITCH_FLYING_CONTROL = NFUReflectionStatics.findFieldIfDeclared(gaia.entity.Witch.class, "flyingControl")
		.orElseThrow(() -> new ReflectionFailedException("field not found"));
	public static final Field FIELD_WITCH_NORMAL_CONTROL = NFUReflectionStatics.findFieldIfDeclared(gaia.entity.Witch.class, "groundControl")
		.orElseThrow(() -> new ReflectionFailedException("field not found"));
	public static final Field FIELD_MOB_NAVIGATION = NFUReflectionStatics.findFieldIfDeclared(Mob.class, "f_21344_")
		.orElseThrow(() -> new ReflectionFailedException("field not found"));

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		// Fix Witch move control issues, also handle friended witch move control update
		if (event.getEntity() instanceof gaia.entity.Witch witch) {
			boolean isRidingBroom = witch.getItemBySlot(EquipmentSlot.OFFHAND).is(GaiaRegistry.BROOM.get())
				|| witch.getItemBySlot(EquipmentSlot.OFFHAND).is(NFFGirlsGaiaTags.WEAPON_BROOMS);
			if (!witch.isRidingBroom() && witch.isNoGravity())
				witch.setNoGravity(false);
			witch.setRidingBroom(isRidingBroom);
			try {
				FIELD_MOB_MOVE_CONTROL.set(witch, isRidingBroom ? FIELD_WITCH_FLYING_CONTROL.get(witch) : FIELD_WITCH_NORMAL_CONTROL.get(witch));
			} catch (IllegalAccessException e) {
				throw new ReflectionFailedException(e);
			}
			// Fix Witch navigation
			witch.getCapability(NFUCapabilities.CAP_ENTITY_DATA).ifPresent(e -> {
				if (e.getTransientParameter("groundNavigation", PathNavigation.class).isEmpty()) {
					e.putTransientParameter("groundNavigation", witch.getNavigation() instanceof GroundPathNavigation ?
						witch.getNavigation() : new GroundPathNavigation(witch, witch.getLevel()));
				}
				if (e.getTransientParameter("flyingNavigation", PathNavigation.class).isEmpty()) {
					e.putTransientParameter("flyingNavigation", witch.getNavigation() instanceof FlyingPathNavigation ?
						witch.getNavigation() : new FlyingPathNavigation(witch, witch.getLevel()));
				}
			});
			PathNavigation nav = witch.getCapability(NFUCapabilities.CAP_ENTITY_DATA).resolve()
				.flatMap(e -> e.getTransientParameter(isRidingBroom ? "flyingNavigation" : "groundNavigation", PathNavigation.class))
				.orElseThrow();
			try {
				FIELD_MOB_NAVIGATION.set(witch, nav);
			} catch (IllegalAccessException e) {
				throw new ReflectionFailedException(e);
			}
		}
	}
*/
	// NFU Mixin events

	@SubscribeEvent
	public static void onGoalsRegister(MobRegisterGoalsEvent event) {
		if (NFFGirlsGaiaConfigs.ValueCache.Tweak.DAY_MOBS_NEUTRAL_IN_BRIGHT_PLACES
			&& Optional.ofNullable(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType())).filter(t -> t.getNamespace().equals(GrimoireOfGaia.MOD_ID)).isPresent()
			&& (event.getEntity() instanceof IDayMob || event.getEntity().getType().is(NFFGirlsGaiaTags.NEUTRAL_IN_BRIGHT_PLACES))
		)
		{
			NFUAIStatics.getTargetPlayerGoal(event.getEntity()).ifPresent(tg ->
				NFUAIStatics.addAndTargetingCondition(tg, target -> event.getEntity().getLightLevelDependentMagicValue() < 0.5f));
		}
	}

	@SubscribeEvent
	public static void preventExplosiveProjectilesBreakingItems(ItemEntityHurtEvent event) {
		if (!NFFGirlsGaiaConfigs.ValueCache.Tweak.EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS
			&& (event.damageSource.isExplosion())
			&& event.damageSource.getDirectEntity() instanceof Projectile)
		{
			ResourceLocation typeKey = ForgeRegistries.ENTITY_TYPES.getKey(event.damageSource.getDirectEntity().getType());
			if (typeKey != null && typeKey.getNamespace().equals(GrimoireOfGaia.MOD_ID)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void removeGaiaDynamicGoals(LivingStartBaseAiStepEvent event) {
		if (event.getEntity() instanceof AbstractAssistGaiaEntity e
			&& INFFGirlsTamed.get(event.getEntity()).isPresent()) {

		}

		if (event.getEntity() instanceof Mob mob
			&& event.getEntity() instanceof IBlocksGaiaDynamicGoals fix
			&& !event.getEntity().level.isClientSide) {
			var toRemove = fix.getGoalsToRemove();
			List<Goal> toRemoveGoals = mob.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal)
				.filter(g -> toRemove.contains(g.getClass())).collect(Collectors.toList());
			toRemoveGoals.addAll(mob.targetSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal)
				.filter(g -> toRemove.contains(g.getClass())).toList());
			toRemoveGoals.forEach(goal -> {
				mob.goalSelector.removeGoal(goal);
				mob.targetSelector.removeGoal(goal);
			});
		}
	}
/*
	@SubscribeEvent
	public static void onAddGoals(MobRegisterGoalsEvent event) {
		if (event.getEntity().getType().equals(GaiaRegistry.WITCH.getEntityType()) && event.getEntity() instanceof gaia.entity.Witch witch) {
			event.getGoalSelector().addGoal(2, new WaterAvoidingRandomFlyingGoal(witch, 1.0d));
		}
	}
*/
	// GAIA Mixin events
	@SubscribeEvent
	public static void onGaiaFinalizeSpawn(GaiaMobFinalizeSpawnEvent event)
	{
		if (INFFGirlsTamed.get(event.getEntity()).isPresent())
			event.setCanceled(true);
	}




}
