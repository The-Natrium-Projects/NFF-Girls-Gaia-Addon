package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.SludgeGirl;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaAiGoalGroups;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSixBaublesInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtOwnerGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GaiaSludgeGirlEntity extends SludgeGirl implements INFFGirlsTamed {

    public GaiaSludgeGirlEntity(EntityType<? extends GaiaSludgeGirlEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    @Override
    public @Nullable NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventory(4, this);
    }

    @Override
    public @Nullable NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsFourBaublesInventoryMenu(i, inventory, container, this);
    }

    @Override
    protected void registerGoals() {
        NFFGirlsGaiaAiGoalGroups.COMMON_MELEE.addTo(this, 0);
    }

}
