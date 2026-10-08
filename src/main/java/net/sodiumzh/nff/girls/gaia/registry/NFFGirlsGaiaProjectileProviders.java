package net.sodiumzh.nff.girls.gaia.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import gaia.entity.YukiOnna;
import gaia.registry.GaiaRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaYukiOnnaTamingProcess;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributes;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.entity.NFUItemProjectileEntity;
import net.sodiumzh.nfu.entity.ServerEntityMotion;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.math.Field3D;
import net.sodiumzh.nfu.math.IInequalityPattern3D;
import net.sodiumzh.nfu.math.Inequality3D;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import net.sodiumzh.nfu.util.NFUResourceLocation;

import java.util.Optional;
import java.util.function.Function;

public class NFFGirlsGaiaProjectileProviders {

    public static final Function<Mob, NFUEffectZoneEntity> YUKI_ONNA_SNOW_ZONE_INNER = owner ->
        NFUEffectZoneEntity.create(owner).setScale(8d, 8d)
            .setLifetime(-1)
            .setGravity(0)
            .particle(ParticleTypes.SNOWFLAKE, 300)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setOnServerLivingOverlap((z, e) -> {
                if (!(e instanceof YukiOnna) && e.getBoundingBox().getCenter().distanceToSqr(z.getBoundingBox().getCenter()) <= 64d) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 3));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5, 1));
                    if (e.tickCount % 10 == 0)
                        e.hurt(new DamageSource(e.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FREEZE),
                            owner, owner, e.position()), 2f);
                }
            })
            .setOnServerTick(z -> {
                if (!owner.isAlive())
                    z.discard();
                else if (!(NFFTamingMapping.getProcess(owner) instanceof GaiaYukiOnnaTamingProcess proc && proc.isInAnyProcess(owner))) {
                    z.discard();
                } else {
                    z.alignCenterTo(owner.getBoundingBox().getCenter(), true);
                }
            })
            .setIdentifier(new ResourceLocation("nffgirlsgaia:yuki_onna_snow_zone_inner"));

    public static final Function<Mob, NFUEffectZoneEntity> YUKI_ONNA_SNOW_ZONE_OUTER = owner ->
        NFUEffectZoneEntity.create(owner).setScale(16d, 16d)
            .setLifetime(-1)
            .setGravity(0)
            .particle(ParticleTypes.SNOWFLAKE, 200)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setOnServerLivingOverlap((z, e) -> {
                double distSqr = e.getBoundingBox().getCenter().distanceToSqr(z.getBoundingBox().getCenter());
                if (!(e instanceof YukiOnna) && distSqr >= 64d && distSqr <= 256d) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 1));
                    if (owner.tickCount % 30 == 3)
                        e.hurt(new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FREEZE),
                            owner, owner, owner.position()), 2f);
                }
            })
            .setOnServerTick(z -> {
                if (!owner.isAlive())
                    z.discard();
                else if (!(NFFTamingMapping.getProcess(owner) instanceof GaiaYukiOnnaTamingProcess proc && proc.isInAnyProcess(owner))) {
                    z.discard();
                } else {
                    z.alignCenterTo(owner.getBoundingBox().getCenter());
                }
            })
            .setIdentifier(new ResourceLocation("nffgirlsgaia:yuki_onna_snow_zone_outer"));


    /**
     * Storm magic effect of friended Yuki-Onna.
     */
    public static final Function<LivingEntity, NFUEffectZoneEntity> YUKI_ONNA_SNOW_EFFECT_FRIENDED = owner ->
        NFUEffectZoneEntity.create(owner).setScale(6d, 6d)
            .particle(ParticleTypes.SNOWFLAKE, 100)
            .setIdentifier(NFUResourceLocation.of(NFFGirlsGaia.MOD_ID, "aquatic_effect_vortex"))
            .setLifetime(10 * 20)
            .setGravity(0f)
            .setOnServerLivingOverlap((z, l) -> {
                if (!l.equals(z.getOwner()) && INFFTamed.get(z.getOwner()).filter(t -> t.isTamedAlliedTo(l)).isEmpty()) {
                    if (l.tickCount % 5 == 0) {
                        l.hurt(new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FREEZE),
                            z, z.getOwner(), l.position()),
                            Optional.ofNullable(z.getOwner())
                                .map(o -> (float)((LivingEntity)o).getAttributeValue(Attributes.ATTACK_DAMAGE) / 4f).orElse(0f));
                    }
                    l.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15 * 20, 3));
                }
            });


    /** Vortex force field dragging to the center horizontally and downward vertically. */
    private static final Field3D VORTEX_FORCE_FIELD = NFFGirlsGaiaGeometries.VORTEX.get().field()
        .scaleValue(new Vec3(0.05d, 0.05d, 0.05d))
        .putValueAddition(new Vec3(0.0, -0.04, 0.0))
        .setBaseDefDomain(Inequality3D.limitedInOne());

    /**
     * Vortex field summoned by Cecaelia and Mermaid during the friending process.
     */
    public static final Function<LivingEntity, NFUEffectZoneEntity> VORTEX = owner ->
        NFUEffectZoneEntity.create(owner).setScale(8d, 8d)
            .particle(ParticleTypes.BUBBLE, 70)
            .particleAreaShape(IInequalityPattern3D.CONE_SLIM.get().inequality()
                .scale(new Vec3(1.4d, 1.6d, 1.4d)))
            .particleAreaBoundingBox(new AABB(-1.4, -1.4, -1.4, 1.4, 1.4, 1.4))
            /*.particleVelocityFunction(NFFGirlsGaiaGeometries.VORTEX.get().field()
                .scaleValue(new Vec3(0.1d, 0.1d, 0.1d))
                .putValueAddition(new Vec3(0.0, -0.05, 0.0)))*/
            .setLifetime(10 * 20)
            .setGravity(0f)
            .setOnServerGenericEntityOverlap((z, e) -> {
                if (!e.equals(z.getOwner())
                    && (e instanceof LivingEntity || e instanceof ItemEntity)
                    && !e.getType().is(NFFGirlsTags.AQUATIC_MOB)
                    && !(e instanceof Player p && (p.isCreative() || p.isSpectator()))
                    && e.isInWaterOrBubble()) {
                    ServerEntityMotion.accel(VORTEX_FORCE_FIELD.relToAbs(z.getBoundingBox()).apply(e.getBoundingBox().getCenter()))
                        .apply(e);
                }
            });

    /**
     * Explosive projectile of friended Cecaelia.
     */
    public static final Function<LivingEntity, NFUItemProjectileEntity> BUBBLE_BOMB_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner).particle(ParticleTypes.BUBBLE, 3)
            .setLifetime(6 * 20)
            .setFireImmune(true)
            .setItem(GaiaRegistry.PROJECTILE_BUBBLE.get().getDefaultInstance())
            .particle(ParticleTypes.BUBBLE, 3)
            .setLiquidResistanceFactor(0.01f)
            .setAirResistanceFactor(0.01f)
            .setGravity(0.01f)
            .setOnHitLiving((proj, h) -> {
                if (h.getEntity() instanceof LivingEntity l
                    && INFFTamed.get(owner).filter(t -> t.isTamedAlliedTo(l)).isEmpty())
                {
                    l.hurt(new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC),
                        proj, owner, proj.position()), 3f + (float)(owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) * 0.5f);
                    proj.level().explode(proj,
                        new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC), proj, owner, proj.position()),
                        null,
                        proj.getBoundingBox().getCenter(),
                        1f + 0.05f * (float) owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue(),
                        false,
                        Level.ExplosionInteraction.NONE);
                    proj.discard();
                }
            })
            .setOnTick(e -> {
                if (!e.isInWaterOrBubble()) e.discard();
            });

    /**
     * Bubble magic of friendly Cecaelia.
     */
    public static final Function<Mob, NFUEffectZoneEntity> BUBBLE_SPHERE = owner ->
        NFUEffectZoneEntity.create(owner).setScale(3d, 3d)
            .setLifetime(6 * 20)
            .particle(ParticleTypes.BUBBLE, 100)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setOnServerLivingOverlap((z, l) -> {
                if (INFFTamed.get(owner).filter(t -> t.isTamedAlliedTo(l)).isEmpty()
                    && z.getBoundingBox().getCenter().distanceToSqr(l.getEyePosition()) <= 4d)
                {
                    if (z.tickCount % 5 == 1) {
                        l.hurt(new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC),
                            z, owner, l.position()), 3f + (float)(owner.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) * 0.25f);
                    }
                    if (l.equals(owner.getTarget()) && l.getBoundingBox().getCenter().distanceToSqr(l.getBoundingBox().getCenter()) <= 1d)
                        ServerEntityMotion.zero()
                            .addMovement(l.getBoundingBox().getCenter().subtract(z.getBoundingBox().getCenter()))
                            .addAccel(z.getDeltaMovement().reverse())
                            .apply(z);
                }
            })
            .setOnServerTick(e -> {
                if (!e.isInWaterOrBubble()) e.discard();
            });

    // Valkyrie projectiles on friending

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_THUNDER_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.LIGHTNING_PARTICLE.get().getDefaultInstance())
            .particle(ParticleTypes.SMOKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, proj.level());
                lightningBolt.setPos(proj.position());
                lightningBolt.setDamage((float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                proj.level().addFreshEntity(lightningBolt);
                proj.discard();
            })
            .setOnTick(proj -> {
                if (proj.level().getBlockState(proj.blockPosition()).liquid())
                    proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_EXPLOSIVE_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.BURNING_CORE.get().getDefaultInstance())
            .particle(ParticleTypes.FLAME, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                proj.level().explode(proj,
                    new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC), proj, owner, proj.position()),
                    null,
                    proj.getBoundingBox().getCenter(),
                    (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.05f + 1.5f,
                    false,
                    proj.level().getRandom().nextDouble() < 0.25d ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
                proj.discard();
            });

    public static final Function<Mob, NFUEffectZoneEntity> VALKYRIE_ICE_ZONE = owner ->
        NFUEffectZoneEntity.create(owner).setScale(6d, 6d)
            .setLifetime(10 * 20)
            .setGravity(0)
            .particle(ParticleTypes.SNOWFLAKE, 200)
            .particleAreaShape(IInequalityPattern3D.SPHERE.get().inequality())
            .setBlockOverlapFilter((z, pos, bs) -> bs.is(Blocks.FIRE))
            .setOnServerLivingOverlap((z, e) -> {
                if (!e.equals(owner)
                    && e.getBoundingBox().getCenter().distanceToSqr(z.getBoundingBox().getCenter()) <= 36d) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 2));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 3));
                    if (e.tickCount % 10 == 0)
                        e.hurt(new DamageSource(e.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FREEZE),
                            z, owner, e.position()),
                            (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.2f);
                }
            })
            .setServerBlockOverlap((z, pos, bs) -> {
                z.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                z.level().playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (z.level().random.nextFloat() - z.level().random.nextFloat()) * 0.8F);
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_ICE_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(Items.SNOWBALL.getDefaultInstance())
            .particle(ParticleTypes.SNOWFLAKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                var iceZone = VALKYRIE_ICE_ZONE.apply(owner);
                iceZone.alignCenterTo(proj.position());
                proj.level().addFreshEntity(iceZone);
                proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_COMMON_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(Items.NETHER_STAR.getDefaultInstance())
            .particle(ParticleTypes.CRIT, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setHitIgnoresOwner(true)
            .setIdentifier(new ResourceLocation("nffgirlgaia:valkyrie_common_projectile"))
            .setOnHitLiving((proj, h) -> {
                h.getEntity().hurt(new DamageSource(
                    proj.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC),
                    proj, owner, h.getEntity().position()),
                    (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                proj.discard();
            });

    // Friended Valkyrie projectiles

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_THUNDER_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.LIGHTNING_PARTICLE.get().getDefaultInstance())
            .particle(ParticleTypes.SMOKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setHitIgnoresLiving((proj, l) -> INFFTamed.get(proj.getOwner()).filter(t -> t.isTamedAlliedTo(l)).isPresent())
            .setOnHitBlockOrLiving((proj, h) -> {
                // Prevent lightning if an ally is within 3 blocks
                if (proj.level().getEntitiesOfClass(LivingEntity.class,
                    proj.getBoundingBox().inflate(3d),
                    l -> INFFTamed.get(proj.getOwner()).filter(t -> t.isTamedAlliedTo(l)).isPresent()).isEmpty())
                {
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, proj.level());
                    lightningBolt.setPos(proj.position());
                    lightningBolt.setDamage((float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    proj.level().addFreshEntity(lightningBolt);
                    proj.discard();
                }
            })
            .setOnTick(proj -> {
                if (proj.level().getBlockState(proj.blockPosition()).liquid())
                    proj.discard();
            });


    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_ICE_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(Items.SNOWBALL.getDefaultInstance())
            .particle(ParticleTypes.SNOWFLAKE, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                var iceZone = VALKYRIE_ICE_ZONE.apply(owner);
                iceZone.alignCenterTo(proj.position());
                iceZone.setLivingOverlapFilter((z, l) -> INFFTamed.get(z.getOwner()).filter(t -> t.isTamedAlliedTo(l)).isPresent());
                proj.level().addFreshEntity(iceZone);
                proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> VALKYRIE_EXPLOSIVE_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setLifetime(10 * 20)
            .setGravity(0.06f)
            .setItem(ModItems.BURNING_CORE.get().getDefaultInstance())
            .particle(ParticleTypes.FLAME, 10)
            .setLiquidResistanceFactor(0.2f)
            .setAirResistanceFactor(0.01f)
            .setOnHitBlockOrLiving((proj, h) -> {
                if (h instanceof EntityHitResult eh && !(eh.getEntity() instanceof LivingEntity)) return;
                proj.level().explode(proj,
                    new DamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC), proj, owner, proj.position()),
                    null,
                    proj.getBoundingBox().getCenter(),
                    (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.05f + 1.5f,
                    false,
                    Level.ExplosionInteraction.NONE);
                proj.discard();
            });

    public static final Function<Mob, NFUItemProjectileEntity> POISON_PROJECTILE_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setScale(0.2d, 0.2d)
            .setLifetime(10 * 20)
            .setItem(GaiaRegistry.PROJECTILE_POISON.get().getDefaultInstance())
            .setOnHitBlock((proj, hs) -> proj.discard())
            .setOnHitLiving((proj, ehs) -> {
                INFFGirlsTamed tamed = INFFGirlsTamed.get(owner).orElse(null);
                if (tamed == null) { proj.discard(); return; }
                if (ehs.getEntity() instanceof LivingEntity living && !tamed.isTamedAlliedTo(living)) {
                    living.hurt(proj.damageSources().indirectMagic(proj, owner),
                        (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2f);
                    int amplifier = (int) Math.round(owner.getAttributeValue(NFFGirlsEntityAttributes.POISON_ASPECT.get()));
                    int time = (int) Math.round(owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 100);
                    living.addEffect(new MobEffectInstance(MobEffects.POISON, time, amplifier));
                    proj.discard();
                }
            });

    public static final Function<Mob, NFUItemProjectileEntity> ANT_PHEROMONE_PROJECTILE = owner ->
        NFUItemProjectileEntity.create(owner)
            .setScale(0.2d, 0.2d)
            .setLifetime(30 * 20)
            .setGravity(0f)
            .setItem(GaiaRegistry.PROJECTILE_MAGIC.get().getDefaultInstance())
            .setHitIgnoresOwner(true)
            .setOnHitBlock((proj, hs) -> proj.discard())
            .setOnHitLiving((proj, ehs) -> {
                INFFGirlsTamed tamed = INFFGirlsTamed.get(owner).orElse(null);
                if (tamed == null) { proj.discard(); return; }
                if (ehs.getEntity() instanceof LivingEntity living && tamed.isTamedAlliedTo(living)) {
                    living.addEffect(new MobEffectInstance(NFFGirlsGaiaEffects.ANT_PHEROMONE.get(), 5 * 60 * 20));
                    living.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
                    NFUParticleStatics.sendGlintParticlesToEntityDefault(living);
                }
                proj.discard();
            })
            .setOnTick(proj -> {
                EntityComponentAPI.getDataComponent(proj).getVariable("target", Entity.class).ifPresent(e -> {
                    // Add a hit check tolerance, as there's an issue that the projectile flashes on the screen
                    // maybe due to hit check
                    if (e instanceof LivingEntity le &&
                        NFUMathStatics.getBoxSurfaceDistSqr(e.getBoundingBox(), proj.getBoundingBox()) < 0.25d) {
                        le.addEffect(new MobEffectInstance(NFFGirlsGaiaEffects.ANT_PHEROMONE.get(), 5 * 60 * 20));
                        le.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
                        NFUParticleStatics.sendGlintParticlesToEntityDefault(le);
                        proj.discard();
                    }
                    else if (e.isAlive())
                        proj.setVelocitySynched(e.getEyePosition().subtract(proj.getEyePosition()).normalize().scale(0.2d));
                });
            });

    public static final Function<Mob, NFUItemProjectileEntity> WEB_BULLET_FRIENDED = owner ->
        NFUItemProjectileEntity.create(owner)
            .setScale(0.2d, 0.2d)
            .setGravity(0.01f)
            .setLifetime(10 * 20)
            .setItem(GaiaRegistry.PROJECTILE_WEB.get().getDefaultInstance())
            .setHitIgnoresOwner(true)
            .setOnHitBlock((proj, hs) -> {
                BlockPos pos = NFUMathStatics.getBlockPos(proj.getBoundingBox().getCenter());
                if (proj.level().getBlockState(pos).is(Blocks.AIR))
                    proj.level().setBlock(pos, Blocks.COBWEB.defaultBlockState(), 1 + 2);
                proj.discard();
            })
            .setOnHitLiving((proj, ehs) -> {
                INFFGirlsTamed tamed = INFFGirlsTamed.get(owner).orElse(null);
                if (tamed == null) { proj.discard(); return; }
                if (ehs.getEntity() instanceof LivingEntity living && !tamed.isTamedAlliedTo(living)) {
                    living.hurt(living.level().damageSources().indirectMagic(proj, owner),
                        (float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2f);
                    BlockPos pos = NFUMathStatics.getBlockPos(living.getBoundingBox().getCenter());
                    if (proj.level().getBlockState(pos).is(Blocks.AIR))
                        proj.level().setBlock(pos, Blocks.COBWEB.defaultBlockState(), 1 + 2);
                    proj.discard();
                }
            });

}
