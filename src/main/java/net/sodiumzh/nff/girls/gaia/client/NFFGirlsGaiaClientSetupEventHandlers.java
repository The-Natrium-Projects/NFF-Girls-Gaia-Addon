package net.sodiumzh.nff.girls.gaia.client;

import gaia.client.renderer.*;
import gaia.entity.SludgeGirl;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.client.gui.NFFGirlsGaiaBowShootingGUI;
import net.sodiumzh.nff.girls.gaia.client.renderer.NFFGirlsGaiaSirenRenderer;
import net.sodiumzh.nff.girls.gaia.client.renderer.NFFGirlsGaiaSuccubusRenderer;
import net.sodiumzh.nff.girls.gaia.client.renderer.NFFGirlsGaiaWerecatRenderer;
import net.sodiumzh.nff.girls.gaia.inventory.NFFGirlsGaiaBowShootingInventoryMenu;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEntityAttributes;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEntityTypes;
import net.sodiumzh.nff.services.event.client.RegisterGUIScreenEvent;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaClientSetupEventHandlers
{
	
	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_DRYAD.get(), DryadRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SPRIGGAN.get(), SprigganRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_DULLAHAN.get(), DullahanRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_HARPY.get(), HarpyRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_BANSHEE.get(), BansheeRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SUCCUBUS.get(), NFFGirlsGaiaSuccubusRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_MUMMY.get(), MummyRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_BEE.get(), BeeRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_VALKYRIE.get(), ValkyrieRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_YUKI_ONNA.get(), YukiOnnaRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_CECAELIA.get(), CecaeliaRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_MERMAID.get(), MermaidRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_WERECAT.get(), NFFGirlsGaiaWerecatRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_WITCH.get(), WitchRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SHAMAN.get(), ShamanRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_ENDER_DRAGON_GIRL.get(), EnderDragonGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SIREN.get(), NFFGirlsGaiaSirenRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_ANT_WORKER.get(), AntWorkerRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_ARACHNE.get(), ArachneRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SLUDGE_GIRL.get(), SludgeGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_MANDRAGORA.get(), MandragoraRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_CENTAUR.get(), CentaurRenderer::new);
        event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SATYRESS.get(), SatyressRenderer::new);
	}

    @SubscribeEvent
    public static void registerGuiScreen(RegisterGUIScreenEvent event) {
        event.registerDefault(NFFGirlsGaiaBowShootingInventoryMenu.class, NFFGirlsGaiaBowShootingGUI::new);
    }

}