package net.sodiumzh.nff.girls.gaia.entity;

import com.github.mechalopa.hmag.registry.ModEffects;
import com.github.mechalopa.hmag.registry.ModPotions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.INFFSafeTarget;
import net.sodiumzh.nff.girls.entity.projectile.NFFSafeThrownPotionEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributes;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.math.WeightedRandomSelector;
import net.sodiumzh.nfu.registry.NFUEffects;
import net.sodiumzh.nfu.util.NFUContainerStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPotionThrower extends INFFGirlsTamed {

    static Map<MobEffect, Potion> POTION_MAP = new HashMap<>();

    public default void throwPotion(LivingEntity target, float distanceFactor, boolean isAttackingEnemy) {
        Vec3 vec3 = target.getDeltaMovement();
        double d0 = target.getX() + vec3.x - this.asMob().getX();
        double d1 = target.getEyeY() - (double)1.1F - this.asMob().getY();
        double d2 = target.getZ() + vec3.z - this.asMob().getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        Tuple2<MobEffectInstance, Potion> effectAndPotion =
            isAttackingEnemy ? this.getAttackingPotion(target) : this.getSupportingPotion(target, !isInCombat());

        NFFSafeThrownPotionEntity thrownpotion = new NFFSafeThrownPotionEntity(this.asMob().level(), this.asMob())
            .setIntendedTarget(target)
            .setTargetType(isAttackingEnemy ? INFFSafeTarget.TargetType.NON_ALLY : INFFSafeTarget.TargetType.ALLY)
            .setEffectOverride(List.of(effectAndPotion.getA()))
            .setRevertsEffectForUndead(true);
        thrownpotion.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), effectAndPotion.getB()));

        if (target.equals(this.asMob())) {
            thrownpotion.setPos(this.asMob().getEyePosition());
            thrownpotion.setDeltaMovement(0d, 0d, 0d);
            this.asMob().level().addFreshEntity(thrownpotion);
            // Directly call onHit
            NFUReflectionStatics.invokeDeclaredMethod(thrownpotion, Projectile.class, "m_6532_",
                HitResult.class, new EntityHitResult(this.asMob()));
        }
        else {
            thrownpotion.setPos(this.asMob().getEyePosition().add(this.asMob().getForward().scale(0.5d)));
            thrownpotion.setXRot(thrownpotion.getXRot() + 20.0F);
            this.asMob().swing(InteractionHand.MAIN_HAND);
            thrownpotion.shoot(d0, d1 + d3 * 0.1D, d2, 0.75F, 2.0F);
            this.asMob().getLookControl().setLookAt(d0, d1, d2);
            if (!this.asMob().isSilent()) {
                this.asMob().level().playSound((Player) null, this.asMob().getX(), this.asMob().getY(), this.asMob().getZ(),
                    SoundEvents.WITCH_THROW, this.asMob().getSoundSource(), 1.0F, 0.8F + this.asMob().getRandom().nextFloat() * 0.4F);
            }
            this.asMob().level().addFreshEntity(thrownpotion);
        }
    }

    public default Tuple2<MobEffectInstance, Potion> getAttackingPotion(LivingEntity target) {
        float atk = (float) this.asMob().getAttributeValue(Attributes.ATTACK_DAMAGE);
        int amplifierPoison = this.asMob().getAttributes().hasAttribute(NFFGirlsEntityAttributes.POISON_ASPECT.get()) ?
            (int)Math.round(this.asMob().getAttributeValue(NFFGirlsEntityAttributes.POISON_ASPECT.get())) : 0;
        int amplifierWither = this.asMob().getAttributes().hasAttribute(NFFGirlsEntityAttributes.WITHER_ASPECT.get()) ?
            (int)Math.round(this.asMob().getAttributeValue(NFFGirlsEntityAttributes.WITHER_ASPECT.get())) - 1 : -1;
        int healHarmAmplifier = (int) Math.round(Math.log10(Math.max(1d, atk / 6d)) / 0.30103d);

        WeightedRandomSelector<Tuple2<MobEffectInstance, Potion>> selector = new WeightedRandomSelector<>();
        // Instant damage
        selector.add(
            // 0.30103 ~= log10(2), rounding ATK to the closest amplifier (dmg = 6 * 2 ^ amplifier in vanilla)
            Tuple2.of(new MobEffectInstance(MobEffects.HARM, 1, healHarmAmplifier), Potions.HARMING),
            2d);
        // Poison
        if (!target.getMobType().equals(MobType.UNDEAD)) {
            selector.add(
                Tuple2.of(new MobEffectInstance(MobEffects.POISON, Math.round(atk * 100f), amplifierPoison), Potions.POISON),
                1d);
        }
        // Slowness
        selector.add(
            Tuple2.of(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Math.round(atk * 100f), amplifierPoison), Potions.SLOWNESS),
            1d);
        // Weakness
        selector.add(
            Tuple2.of(new MobEffectInstance(MobEffects.WEAKNESS, Math.round(atk * 100f), amplifierPoison), Potions.WEAKNESS),
            1d);
        // Wither
        if (amplifierWither >= 0) {
            MobEffectInstance wither = new MobEffectInstance(MobEffects.WITHER, Math.round(atk * 90f), amplifierWither);
            if (target.canBeAffected(wither))
                selector.add(Tuple2.of(wither, Potions.HARMING), 2d);
        }
        // Burning
        if (this.getXpLevel() >= 15 && !target.fireImmune()) {
            selector.add(Tuple2.of(new MobEffectInstance(ModEffects.COMBUSTION.get(), Math.round(atk * 50f)), ModPotions.COMBUSTION.get()),
            1d);
        }
        return selector.select(this.asMob().getRandom());
    }

    public default Tuple2<MobEffectInstance, Potion> getSupportingPotion(LivingEntity target, boolean healingOnly) {
        float atk = (float) this.asMob().getAttributeValue(Attributes.ATTACK_DAMAGE);
        int amplifierPoison = this.asMob().getAttributes().hasAttribute(NFFGirlsEntityAttributes.POISON_ASPECT.get()) ?
            (int)Math.round(this.asMob().getAttributeValue(NFFGirlsEntityAttributes.POISON_ASPECT.get())) : 0;
        int amplifierWither = this.asMob().getAttributes().hasAttribute(NFFGirlsEntityAttributes.WITHER_ASPECT.get()) ?
            (int)Math.round(this.asMob().getAttributeValue(NFFGirlsEntityAttributes.WITHER_ASPECT.get())) - 1 : -1;
        int healHarmAmplifier = (int) Math.round(Math.log10(Math.max(1d, atk / 6d)) / 0.30103d);

        WeightedRandomSelector<Tuple2<MobEffectInstance, Potion>> selector = new WeightedRandomSelector<>();
        boolean isHealthEmergency = target.getHealth() / target.getMaxHealth() < 0.25d;
        if (isHealthEmergency)
            return Tuple2.of(new MobEffectInstance(MobEffects.HEAL, 1, healHarmAmplifier), Potions.HEALING);
        Emergency emergencyType = getEmergency(target);
        if (emergencyType != Emergency.NONE)
            return Tuple2.of(new MobEffectInstance(emergencyType.getSolutionEffect().getA(), 900, 0), emergencyType.getSolutionEffect().getB());
        if (healingOnly)
            return Tuple2.of(new MobEffectInstance(MobEffects.HEAL, 1, healHarmAmplifier), Potions.HEALING);
        // Instant heal
        if (target.getHealth() < target.getMaxHealth()) {
            selector.add(
                // 0.30103 ~= log10(2), rounding ATK to the closest amplifier (dmg = 4 * 2 ^ amplifier in vanilla, 1.5 atk = 1 heal)
                Tuple2.of(new MobEffectInstance(MobEffects.HEAL, 1, healHarmAmplifier), Potions.HEALING),
                2d);
        }
        // Regeneration
        if (!target.getMobType().equals(MobType.UNDEAD)) {
            selector.add(Tuple2.of(new MobEffectInstance(MobEffects.REGENERATION, Math.round(atk * 100f), amplifierPoison), Potions.REGENERATION), 1d);
        }
        // Speed
        selector.add(Tuple2.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Math.round(atk * 100f), amplifierPoison), Potions.SWIFTNESS), 1d);
        // Strength
        selector.add(Tuple2.of(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Math.round(atk * 100f), amplifierPoison), Potions.STRENGTH), 1d);
        return selector.select(this.asMob().getRandom());
    }

    public static Emergency getEmergency(LivingEntity test) {
        if (test.isUnderWater() && !test.hasEffect(MobEffects.WATER_BREATHING)
            && test.canDrownInFluidType(ForgeMod.WATER_TYPE.get())
            && (double) test.getAirSupply() / (double) test.getMaxAirSupply() <= 0.1d)
            return Emergency.DROWNING;
        if (test.isOnFire() && !test.fireImmune() && !test.hasEffect(MobEffects.FIRE_RESISTANCE))
            return Emergency.BURNING;
        if (test instanceof Player
            && (test.level().getMaxLocalRawBrightness(test.getOnPos()) < 5 || test.level().isNight())
            && !test.hasEffect(MobEffects.NIGHT_VISION))
            return Emergency.BLIND_AT_NIGHT;
        return Emergency.NONE;
    }

    public default Optional<LivingEntity> getTargetingAlly() {
        List<LivingEntity> allies = this.asMob().level()
            .getEntitiesOfClass(LivingEntity.class, this.asMob().getBoundingBox().inflate(8, 8, 8), e -> {
                if (!this.isTamedAlliedTo(e)) return false;
                if (this.asMob().distanceToSqr(e) > 64d) return false;
                if (!this.asMob().hasLineOfSight(e)) return false;
                return true;
            });
        if (allies.isEmpty()) return Optional.empty();
        return Optional.ofNullable(NFUContainerStatics.randomPick(allies));
    }

    public default Optional<LivingEntity> getAllyInEmergency() {
        List<LivingEntity> allies = this.asMob().level()
            .getEntitiesOfClass(LivingEntity.class, this.asMob().getBoundingBox().inflate(8, 8, 8), e -> {
                if (!this.isTamedAlliedTo(e)) return false;
                if (this.asMob().distanceToSqr(e) > 64d) return false;
                if (!this.asMob().hasLineOfSight(e)) return false;
                return !IPotionThrower.getEmergency(e).equals(Emergency.NONE);
            });
        if (allies.isEmpty()) return Optional.empty();
        if (allies.contains(this.asMob())) return Optional.of(this.asMob());
        if (this.getOwnerInDimension() != null && allies.contains(this.getOwnerInDimension()))
            return Optional.of(this.getOwnerInDimension());
        else return Optional.ofNullable(NFUContainerStatics.randomPick(allies));
    }

    /** Get ally to throw healing potions when out of combat. */
    public default Optional<LivingEntity> getIdleHealingAlly() {
        List<LivingEntity> allies = this.asMob().level()
            .getEntitiesOfClass(LivingEntity.class, this.asMob().getBoundingBox().inflate(8, 8, 8), e -> {
                if (!this.isTamedAlliedTo(e)) return false;
                if (this.asMob().distanceToSqr(e) > 64d) return false;
                if (!this.asMob().hasLineOfSight(e)) return false;
                return e.getHealth() < e.getMaxHealth();
            });
        if (allies.isEmpty()) return Optional.empty();
        if (allies.contains(this.asMob())) return Optional.of(this.asMob());
        if (this.getOwnerInDimension() != null && allies.contains(this.getOwnerInDimension()))
            return Optional.of(this.getOwnerInDimension());
        else return Optional.ofNullable(NFUContainerStatics.randomPick(allies));
    }

    public default boolean isInCombat() {
        return this.asMob().level().getEntitiesOfClass(LivingEntity.class, this.asMob().getBoundingBox().inflate(8d, 8d, 8d),
                this::isTamedAlliedTo)
            .stream()
            .filter(e -> this.asMob().distanceToSqr(e) <= 64d)
            .filter(e -> e.getLastHurtByMob() != null || e.getLastHurtMob() != null || (e instanceof Mob mob && mob.getTarget() != null))
            .anyMatch(e -> this.asMob().hasLineOfSight(e));
    }

    public static enum Emergency {
        DROWNING(MobEffects.WATER_BREATHING, Potions.WATER_BREATHING),
        BURNING(MobEffects.FIRE_RESISTANCE, Potions.FIRE_RESISTANCE),
        BLIND_AT_NIGHT(MobEffects.NIGHT_VISION, Potions.NIGHT_VISION),
        NONE(NFUEffects.EMPTY.get(), Potions.WATER);
        private Emergency(MobEffect effect, Potion potion){
            this.solution = Tuple2.of(effect, potion);
        }
        private final Tuple2<MobEffect, Potion> solution;
        public Tuple2<MobEffect, Potion> getSolutionEffect() {
            return solution;
        }
    }

}
