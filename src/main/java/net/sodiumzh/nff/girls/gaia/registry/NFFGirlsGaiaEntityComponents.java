package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nfu.entity.component.EntityComponentSetupEvent;
import net.sodiumzh.nfu.registry.NFUEntityComponents;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NFFGirlsGaia.MOD_ID)
public class NFFGirlsGaiaEntityComponents {

    @SubscribeEvent
    public static void attachComponents(EntityComponentSetupEvent event) {
        if (event.getEntity().getType().is(NFFGirlsGaiaTags.ANT_PHEROMONE_AFFECTED)) {
            event.addComponent("/default_anger_handler", NFUEntityComponents.DEFAULT_ANGER_HANDLER.get());
        }
    }

}
