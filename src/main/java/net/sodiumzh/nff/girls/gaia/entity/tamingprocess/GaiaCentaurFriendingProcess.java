package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class GaiaCentaurFriendingProcess extends TamingProcessItemGivingProgress {

    @Override
    public boolean additionalConditions(Player player, Mob mob) {
        return mob.hasEffect(MobEffects.DIG_SPEED)
            || (mob.hasEffect(MobEffects.MOVEMENT_SPEED)
            && mob.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() >= 2);
    }

    @Override
    public void tamableInit(NFFTamableComponent c) {
    }

}
