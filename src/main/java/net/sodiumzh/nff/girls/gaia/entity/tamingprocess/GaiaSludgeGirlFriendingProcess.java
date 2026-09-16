package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;
import gaia.entity.SludgeGirl;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.entity.tamingprocess.HmagSlimeGirlTamingProcess;
import net.sodiumzh.nff.girls.item.MagicalGelBottleItem;
import net.sodiumzh.nff.girls.item.MagicalGelColorUtils;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nfu.math.LinearColor;
import net.sodiumzh.nfu.util.NFUMathStatics;

public class GaiaSludgeGirlFriendingProcess extends HmagSlimeGirlTamingProcess {

    @Override
    protected double getProgressToAdd(ItemStack item, Player player, Mob mob, double oldProc) {
        if (item.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get())) {
            Item var8 = item.getItem();
            if (var8 instanceof MagicalGelBottleItem bottle) {
                return this.getColorDeltaProgress(bottle.getColor(item), LinearColor.fromNormalized(0.5d, 1.0d, 0.5d));
            }
        }
        return item.is(NFFGirlsItems.MAGICAL_GEL_BALL.get()) ? NFUMathStatics.rndRangedDouble(0.04, 0.08) : (double)0.0F;
    }

}
