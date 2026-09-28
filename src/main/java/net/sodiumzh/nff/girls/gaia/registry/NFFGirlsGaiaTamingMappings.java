package net.sodiumzh.nff.girls.gaia.registry;

import gaia.GrimoireOfGaia;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsTamingProcesses;
import net.sodiumzh.nff.services.event.setup.NFFTamingMappingRegisterEvent;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaTamingMappings
{
	@SubscribeEvent
	public static void registerTamingMapping(NFFTamingMappingRegisterEvent event)
	{
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "dryad"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_dryad"),
			NFFGirlsGaiaTamingProcesses.GAIA_DRYAD);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "spriggan"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_spriggan"),
			NFFGirlsTamingProcesses.HMAG_ALRAUNE);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "dullahan"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_dullahan"),
			NFFGirlsTamingProcesses.VANILLA_UNDEAD_B);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "harpy"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_harpy"),
			NFFGirlsGaiaTamingProcesses.GAIA_ANIMAL_A);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "banshee"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_banshee"),
			NFFGirlsTamingProcesses.HMAG_BANSHEE);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "succubus"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_succubus"),
			NFFGirlsTamingProcesses.HMAG_IMP);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "mummy"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_mummy"),
			NFFGirlsTamingProcesses.VANILLA_UNDEAD_A);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "bee"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_bee"),
			NFFGirlsTamingProcesses.HMAG_HORNET);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "valkyrie"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_valkyrie"),
			NFFGirlsGaiaTamingProcesses.GAIA_VALKYRIE);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "yuki_onna"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_yuki_onna"),
			NFFGirlsGaiaTamingProcesses.GAIA_YUKI_ONNA);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "cecaelia"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_cecaelia"),
			NFFGirlsGaiaTamingProcesses.GAIA_AQUATIC_A);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "mermaid"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_mermaid"),
			NFFGirlsGaiaTamingProcesses.GAIA_AQUATIC_A);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "werecat"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_werecat"),
			NFFGirlsGaiaTamingProcesses.GAIA_ANIMAL_A);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "witch"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_witch"),
			NFFGirlsGaiaTamingProcesses.GAIA_WITCH);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "shaman"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_shaman"),
			NFFGirlsGaiaTamingProcesses.GAIA_WITCH);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "ender_dragon_girl"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_ender_dragon_girl"),
			NFFGirlsGaiaTamingProcesses.GAIA_ENDERMAN_B);
        event.register(
            new ResourceLocation(GrimoireOfGaia.MOD_ID, "siren"),
            new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_siren"),
            NFFGirlsGaiaTamingProcesses.GAIA_SIREN
        );
        event.register(
            new ResourceLocation(GrimoireOfGaia.MOD_ID, "ant"),
            new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_ant_worker"),
            NFFGirlsGaiaTamingProcesses.GAIA_ANT_WORKER
        );
        event.register(
            new ResourceLocation(GrimoireOfGaia.MOD_ID, "arachne"),
            new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_arachne"),
            NFFGirlsGaiaTamingProcesses.GAIA_ARACHNE
        );
        event.register(
            new ResourceLocation(GrimoireOfGaia.MOD_ID, "sludge_girl"),
            new ResourceLocation(GrimoireOfGaia.MOD_ID, "gaia_sludge_girl"),
            NFFGirlsGaiaTamingProcesses.GAIA_SLUDGE_GIRL
        );
	}
}
