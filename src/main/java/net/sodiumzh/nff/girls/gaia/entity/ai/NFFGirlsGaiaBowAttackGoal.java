package net.sodiumzh.nff.girls.gaia.entity.ai;

import net.minecraft.world.item.BowItem;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsBowAttackGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

public class NFFGirlsGaiaBowAttackGoal extends NFFGirlsBowAttackGoal {

    public NFFGirlsGaiaBowAttackGoal(INFFTamed pMob, double pSpeedModifier, int pAttackIntervalMin, float pAttackRadius) {
        super(pMob, pSpeedModifier, pAttackIntervalMin, pAttackRadius);
    }

    public boolean checkCanUse() {
        if ((this.mob.getAdditionalInventory().orElseThrow()).getItem(0).isEmpty()) {
            return false;
        } else if ((this.mob.getAdditionalInventory().orElseThrow()).getItem(6).isEmpty()) {
            return false;
        } else {
            return (this.mob.getAdditionalInventory().orElseThrow()).getItem(0).getItem() instanceof BowItem && super.checkCanUse();
        }
    }
}
