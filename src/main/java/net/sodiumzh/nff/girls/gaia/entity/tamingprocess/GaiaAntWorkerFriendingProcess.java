package net.sodiumzh.nff.girls.gaia.entity.tamingprocess;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.entity.tamingprocess.NFFGirlsItemDroppingTamingProcess;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class GaiaAntWorkerFriendingProcess extends NFFGirlsItemDroppingTamingProcess {
    @Override
    public int getHoldingItemTime() {
        return NFFGirlsTamingRules.COOLDOWN_SHORT;
    }

    @Override
    public MobAngerRules getAngerRules() {
        return NFFGirlsAngerRules.ATTACKER_AND_MINOR_HIT.get();
    }

    @Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {

        /*@SubscribeEvent
        public static void notifyAnger() {

        }
*/
    }
}
