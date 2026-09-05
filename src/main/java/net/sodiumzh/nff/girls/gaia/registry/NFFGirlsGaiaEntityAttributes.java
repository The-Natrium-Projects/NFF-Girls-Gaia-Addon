package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributeProviders;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributes;
import net.sodiumzh.nfu.entity.EntityAttributeProvider;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaEntityAttributes extends NFFGirlsEntityAttributes
{

	public static final NFURegistryEntryCollection<EntityAttributeProvider> ENTITY_ATTRIBUTE_PROVIDERS =
		NFURegistryEntryCollection.create(NFURegistries.ENTITY_ATTRIBUTE_PROVIDERS, NFFGirlsGaia.MOD_ID);

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_DRYAD = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_dryad", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(Attributes.FLYING_SPEED, 0.6)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_SPRIGGAN = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_spriggan", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 6.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add((Attribute)ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_DULLAHAN = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_dullahan", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_HARPY = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_harpy", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_BANSHEE = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_banshee", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 6.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.FLYING_SPEED, 0.6)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_SUCCUBUS = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_succubus", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 30.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_MUMMY = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_mummy", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 20.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_BEE = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_bee", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 20.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.FLYING_SPEED, 0.5)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_VALKYRIE = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_valkyrie", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 100.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.3)
			.add(Attributes.ATTACK_DAMAGE, 8.0)
			.add(Attributes.ARMOR, 12.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.2)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_YUKI_ONNA = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_yuki_onna", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 6.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_CECAELIA = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_cecaelia", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0)
			.add(NFFGirlsEntityAttributes.ANTI_AQUATIC.get(), 0.2d));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_MERMAID = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_mermaid", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 6.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.25)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_WERECAT = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_werecat", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_WITCH = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_witch", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 20.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 6.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add(Attributes.FLYING_SPEED, 0.6)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_SHAMAN = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_shaman", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 26.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 8.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_ENDER_DRAGON_GIRL = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_ender_dragon_girl", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
			.add(Attributes.MAX_HEALTH, 70.0)
			.add(Attributes.FOLLOW_RANGE, 40.0)
			.add(Attributes.MOVEMENT_SPEED, 0.275)
			.add(Attributes.ATTACK_DAMAGE, 6.0)
			.add(Attributes.ARMOR, 8.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.25)
			.add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

    public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_ANT_WORKER = ENTITY_ATTRIBUTE_PROVIDERS.register(
        "gaia_ant_worker", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
            .add(Attributes.MAX_HEALTH, 40.0)
            .add(Attributes.FOLLOW_RANGE, 40.0)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.ATTACK_DAMAGE, 4.0)
            .add(Attributes.ARMOR, 4.0)
            .add(Attributes.ATTACK_KNOCKBACK, 0.3)
            .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

    public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_ARACHNE = ENTITY_ATTRIBUTE_PROVIDERS.register(
        "gaia_arachne", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
            .add(Attributes.MAX_HEALTH, 40.0)
            .add(Attributes.FOLLOW_RANGE, 20.0)
            .add(Attributes.MOVEMENT_SPEED, 0.25)
            .add(Attributes.ATTACK_DAMAGE, 4.0)
            .add(Attributes.ARMOR, 4.0)
            .add(Attributes.ATTACK_KNOCKBACK, 0.3)
            .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

    public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_SIREN = ENTITY_ATTRIBUTE_PROVIDERS.register(
        "gaia_siren", () -> EntityAttributeProvider.from(NFFGirlsEntityAttributeProviders.NFFGIRLS_DEFAULT_ATTRIBUTES.get())
            .add(Attributes.MAX_HEALTH, 40.0d)
            .add(Attributes.FOLLOW_RANGE, 26.0d)
            .add(Attributes.MOVEMENT_SPEED, 0.25d)
            .add(Attributes.ATTACK_DAMAGE, 4.0d)
            .add(Attributes.ARMOR, 4.0d)
            .add(Attributes.ATTACK_KNOCKBACK, 0.3d)
            .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0d));

    @SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(NFFGirlsGaiaEntityTypes.GAIA_DRYAD.get(), NFFGirlsGaiaEntityAttributes.GAIA_DRYAD.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_DULLAHAN.get(), NFFGirlsGaiaEntityAttributes.GAIA_DULLAHAN.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_BANSHEE.get(), NFFGirlsGaiaEntityAttributes.GAIA_BANSHEE.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_HARPY.get(), NFFGirlsGaiaEntityAttributes.GAIA_HARPY.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_SPRIGGAN.get(), NFFGirlsGaiaEntityAttributes.GAIA_SPRIGGAN.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_MUMMY.get(), NFFGirlsGaiaEntityAttributes.GAIA_MUMMY.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_SUCCUBUS.get(), NFFGirlsGaiaEntityAttributes.GAIA_SUCCUBUS.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_BEE.get(), NFFGirlsGaiaEntityAttributes.GAIA_BEE.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_VALKYRIE.get(), NFFGirlsGaiaEntityAttributes.GAIA_VALKYRIE.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_YUKI_ONNA.get(), NFFGirlsGaiaEntityAttributes.GAIA_YUKI_ONNA.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_CECAELIA.get(), NFFGirlsGaiaEntityAttributes.GAIA_CECAELIA.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_MERMAID.get(), NFFGirlsGaiaEntityAttributes.GAIA_MERMAID.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_WERECAT.get(), NFFGirlsGaiaEntityAttributes.GAIA_WERECAT.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_WITCH.get(), NFFGirlsGaiaEntityAttributes.GAIA_WITCH.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_SHAMAN.get(), NFFGirlsGaiaEntityAttributes.GAIA_SHAMAN.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_ENDER_DRAGON_GIRL.get(), NFFGirlsGaiaEntityAttributes.GAIA_ENDER_DRAGON_GIRL.get().get().build());
        event.put(NFFGirlsGaiaEntityTypes.GAIA_ANT_WORKER.get(), NFFGirlsGaiaEntityAttributes.GAIA_ANT_WORKER.get().get().build());
        event.put(NFFGirlsGaiaEntityTypes.GAIA_ARACHNE.get(), NFFGirlsGaiaEntityAttributes.GAIA_ARACHNE.get().get().build());
        event.put(NFFGirlsGaiaEntityTypes.GAIA_SIREN.get(), NFFGirlsGaiaEntityAttributes.GAIA_SIREN.get().get().build());
    }

}
