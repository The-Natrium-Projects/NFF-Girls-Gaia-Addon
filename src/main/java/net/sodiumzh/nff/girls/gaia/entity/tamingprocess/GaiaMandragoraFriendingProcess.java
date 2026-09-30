package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import gaia.entity.Mandragora;
import gaia.entity.prop.CyanFlower;
import gaia.registry.GaiaRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.network.NFUDataSerializers;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class GaiaMandragoraFriendingProcess extends NFFTamingProcess {

    @Override
    public void tamableInit(NFFTamableComponent nffTamableComponent) {

    }

    @Nullable
    public Player getOngoingPlayer(Mob mob) {
        return NFFTamableComponent.getOptional(mob)
            .flatMap(c -> c.getDataComponent().getVariable("ongoingPlayer", UUID.class))
            .map(id -> mob.level().getPlayerByUUID(id))
            .orElse(null);
    }

    public void setOngoingPlayer(@Nonnull Mob mob, @Nullable Player player) {
        NFFTamableComponent.getOptional(mob)
            .ifPresent(c -> c.getDataComponent()
                .putPermanentVariable("ongoingPlayer", player != null ? player.getUUID() : new UUID(0L, 0L), NFUDataSerializers.UUID));
    }

    @Override
    public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand interactionHand) {
        return TamingInteractionResult.unhandled(mob);
    }

    @Override
    public void serverTick(Mob mob) {
        NFFTamableComponent.getOptional(mob).ifPresent(c -> {
            @Nullable Player ongoing = this.getOngoingPlayer(mob);
            c.setAlwaysHostileTo(this.getOngoingPlayer(mob));
            if (ongoing != null) {
                // Emit smoke particles to indicate if in process
                if (mob.tickCount % 5 == 0)
                    NFUParticleStatics.sendSmokeParticlesToEntityDefault(mob);
                // If the player is not in nausea effect, interrupt
                if (!ongoing.hasEffect(MobEffects.CONFUSION))
                    this.interrupt(ongoing, mob, true);
                // If the player is too far away (8 blocks), interrupt
                if (ongoing.distanceToSqr(mob) > 144d)
                    this.interrupt(ongoing, mob, true);
                else {
                    // Succeed if the player has been 100 blocks away from the starting point
                    Vec3 vec2Start = mob.position().subtract(this.getStartingPoint(mob));
                    double horizontalDistSqr = vec2Start.x * vec2Start.x + vec2Start.z * vec2Start.z;
                    if (horizontalDistSqr > 10000)
                        this.doTaming(ongoing, mob);
                }
            }
        });
    }

    @Override
    public void interrupt(Player player, Mob mob, boolean b) {
        if (this.getOngoingPlayer(mob) == player) {
            this.setOngoingPlayer(mob, null);
        }
    }

    @Override
    public boolean interruptAll(Mob mob, boolean b) {
        boolean isOngoing = this.isInAnyProcess(mob);
        this.setOngoingPlayer(mob, null);
        return isOngoing;
    }

    @Override
    public boolean isInProcess(Player player, Mob mob) {
        return this.getOngoingPlayer(mob) == player;
    }

    @Override
    public boolean isInAnyProcess(Mob mob) {
        return this.getOngoingPlayer(mob) != null;
    }

    private Vec3 getStartingPoint(Mob mob) {
        return NFFTamableComponent.getOptional(mob)
            .flatMap(c -> c.getDataComponent().getVariable("startingPoint", Vec3.class))
            .orElseGet(mob::position);
    }

    private void setStartingPoint(Mob mob, @Nonnull Vec3 v) {
        NFFTamableComponent.getOptional(mob)
            .ifPresent(c -> c.getDataComponent().putPermanentVariable("startingPoint", v, NFUDataSerializers.VEC3));
    }

    @Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {

        @SubscribeEvent
        public static void onAttacked(LivingHurtEvent event) {
            if (event.getEntity() instanceof Mob mob
                && event.getEntity().getType().equals(GaiaRegistry.MANDRAGORA.getEntityType())
                && event.getSource().getEntity() instanceof Player attackerPlayer
                && attackerPlayer.getMainHandItem().getItem() instanceof HoeItem)
            {
                NFFTamableComponent.getOptional(event.getEntity())
                    .map(c -> c.getTamingProcess() instanceof GaiaMandragoraFriendingProcess p && !p.isInAnyProcess(mob) ? p : null)
                    .ifPresent(p -> {
                        p.setOngoingPlayer(mob, attackerPlayer);
                        p.setStartingPoint(mob, mob.position());
                    });
            }
        }

        @SubscribeEvent
        public static void onMandragoraSpawn(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Mandragora mob
                && mob.getType().equals(GaiaRegistry.MANDRAGORA.getEntityType()))
            {
                // Find players with a hoe and standing close enough
                // Filter players first to reduce stack walking frequency
                List<? extends Player> startingCandidates = event.getLevel().players().stream()
                    .filter(p -> p.getMainHandItem().getItem() instanceof HoeItem && p.distanceToSqr(mob) < 64d)
                    .toList();
                if (startingCandidates.isEmpty()) return;
                // In case this mandragora is spawned from cyan flower
                if (NFUReflectionStatics.isRunningInMethod(CyanFlower.class, "spawnMandragora")) {
                    Player closest = startingCandidates.stream().min(Comparator.comparingDouble(p -> p.distanceToSqr(mob))).orElseThrow();
                    NFFTamableComponent.getOptional(mob)
                        .map(c -> c.getTamingProcess() instanceof GaiaMandragoraFriendingProcess p && !p.isInAnyProcess(mob) ? p : null)
                        .ifPresent(p -> {
                            p.setOngoingPlayer(mob, closest);
                            p.setStartingPoint(mob, mob.position());
                        });
                }
            }
        }
    }
}
