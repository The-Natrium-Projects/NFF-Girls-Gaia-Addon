package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GaiaAquaticTamingProcess extends TamingProcessItemGivingProgress {
    @Override
    public boolean additionalConditions(Player player, Mob mob) {
        return player.isInWaterOrRain() && mob.isInWaterOrRain();
    }

    @Override
    public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter) {
        super.onItemGiven(player, mob, itemGivenCopy, procBefore, procAfter);
        int existingVortexes = countVortexAround(mob);
        this.summonVortex(mob, Math.min(6, 12 - existingVortexes), existingVortexes < 12);
        mob.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void tamableInit(NFFTamableComponent cnffTamable) {
    }

    @Override
    public void serverTick(Mob mob) {
        if (this.getOngoingPlayer(mob).filter(mob::hasLineOfSight)
            .filter(p -> p.equals(mob.getTarget())).isPresent()) {
            if (mob.isInWaterOrRain() && mob.tickCount % 200 == 100)  {
                summonVortex(mob, Math.min(4, 12 - countVortexAround(mob)), false);
                mob.swing(InteractionHand.MAIN_HAND);
            }
        }
    }

    private int countVortexAround(Mob mob) {
        AABB countBB = mob.getBoundingBox().inflate(12d);
        return mob.level().getEntitiesOfClass(NFUEffectZoneEntity.class, countBB, ez ->
            ez.getIdentifier().equals(NFUResourceLocation.of(NFFGirlsGaia.MOD_ID, "aquatic_effect_vortex")))
            .size();
    }

    private void summonVortex(Mob mob, int amount, boolean shouldSummonAtSelf) {
        List<Vec3> positions = new ArrayList<>();
        AABB summonBB = mob.getBoundingBox().inflate(12d);
        if (shouldSummonAtSelf)
            positions.add(mob.getBoundingBox().getCenter());
        for (int i = 0; i < amount * 3; ++i) {
            Vec3 pos = NFUMathStatics.rndPosition(summonBB);
            if (mob.level().getBlockStates(new AABB(pos.subtract(4d, 4d, 4d), pos.add(3d, 3d, 3d)))
                .filter(bs -> bs.is(Blocks.WATER))
                .count() >= 108)   // Half are water
                positions.add(pos);
            if (positions.size() >= amount) break;
        }
        for (Vec3 pos: positions)
        {
            this.summonVortexAt(mob, pos);
        }
    }

    private void summonVortexAt(Mob mob, Vec3 pos) {
        NFUEffectZoneEntity vortex = NFFGirlsGaiaProjectileProviders.VORTEX.apply(mob);
        vortex.setOwner(mob);
        vortex.setPos(pos);
        vortex.setLifetime(15 * 20);
        vortex.setDeltaMovement(Vec3.ZERO);
        mob.level().addFreshEntity(vortex);
    }

}
