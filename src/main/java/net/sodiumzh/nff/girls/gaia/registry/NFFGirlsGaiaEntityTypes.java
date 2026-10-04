package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.*;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;

import javax.annotation.Nullable;
import java.util.function.Function;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaEntityTypes extends NFFGirlsEntityTypes
{

	// To register a tamed mob, you need:
	// (1) register entity type; (2) register attributes; (3) register renderer;
	// (4) register bauble slots; (5) register taming mapping; (6) register healing items.

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NFFGirlsGaia.MOD_ID);

	// Mobs

	public static final RegistryObject<EntityType<GaiaDryadEntity>> GAIA_DRYAD =
			registerBM("gaia_dryad", GaiaDryadEntity::new);
	
	public static final RegistryObject<EntityType<GaiaSprigganEntity>> GAIA_SPRIGGAN =
			registerBM("gaia_spriggan", GaiaSprigganEntity::new, builder -> builder
			.sized(0.6F, 1.6F));

	public static final RegistryObject<EntityType<GaiaDullahanEntity>> GAIA_DULLAHAN =
			registerBM("gaia_dullahan", GaiaDullahanEntity::new, builder -> builder
			.sized(0.6F, 1.6F));

	public static final RegistryObject<EntityType<GaiaHarpyEntity>> GAIA_HARPY =
		registerBM("gaia_harpy", GaiaHarpyEntity::new);

	public static final RegistryObject<EntityType<GaiaBansheeEntity>> GAIA_BANSHEE =
		registerBM("gaia_banshee", GaiaBansheeEntity::new);

	public static final RegistryObject<EntityType<GaiaSuccubusEntity>> GAIA_SUCCUBUS =
		registerBM("gaia_succubus", GaiaSuccubusEntity::new);

	public static final RegistryObject<EntityType<GaiaMummyEntity>> GAIA_MUMMY =
		registerBM("gaia_mummy", GaiaMummyEntity::new);

	public static final RegistryObject<EntityType<GaiaBeeEntity>> GAIA_BEE =
		registerBM("gaia_bee", GaiaBeeEntity::new);

	public static final RegistryObject<EntityType<GaiaValkyrieEntity>> GAIA_VALKYRIE =
		registerBM("gaia_valkyrie", GaiaValkyrieEntity::new);

	public static final RegistryObject<EntityType<GaiaYukiOnnaEntity>> GAIA_YUKI_ONNA =
		registerBM("gaia_yuki_onna", GaiaYukiOnnaEntity::new);

	public static final RegistryObject<EntityType<GaiaCecaeliaEntity>> GAIA_CECAELIA =
		registerBM("gaia_cecaelia", GaiaCecaeliaEntity::new);

	public static final RegistryObject<EntityType<GaiaMermaidEntity>> GAIA_MERMAID =
		registerBM("gaia_mermaid", GaiaMermaidEntity::new, b -> b.sized(0.6f, 1.8f));

	public static final RegistryObject<EntityType<GaiaWerecatEntity>> GAIA_WERECAT =
		registerBM("gaia_werecat", GaiaWerecatEntity::new);

	public static final RegistryObject<EntityType<GaiaWitchEntity>> GAIA_WITCH =
		registerBM("gaia_witch", GaiaWitchEntity::new);

	public static final RegistryObject<EntityType<GaiaShamanEntity>> GAIA_SHAMAN =
		registerBM("gaia_shaman", GaiaShamanEntity::new);

	public static final RegistryObject<EntityType<GaiaEnderDragonGirlEntity>> GAIA_ENDER_DRAGON_GIRL =
		registerBM("gaia_ender_dragon_girl", GaiaEnderDragonGirlEntity::new, b -> b.sized(0.6F, 2.2F));

    public static final RegistryObject<EntityType<GaiaAntWorkerEntity>> GAIA_ANT_WORKER =
            registerBM("gaia_ant_worker", GaiaAntWorkerEntity::new);

    public static final RegistryObject<EntityType<GaiaArachneEntity>> GAIA_ARACHNE =
            registerBM("gaia_arachne", GaiaArachneEntity::new, b -> b.sized(1.4F, 1.6F));

    public static final RegistryObject<EntityType<GaiaSirenEntity>> GAIA_SIREN =
            registerBM("gaia_siren", GaiaSirenEntity::new);

    public static final RegistryObject<EntityType<GaiaSludgeGirlEntity>> GAIA_SLUDGE_GIRL =
            registerBM("gaia_sludge_girl", GaiaSludgeGirlEntity::new);

    public static final RegistryObject<EntityType<GaiaMandragoraEntity>> GAIA_MANDRAGORA =
        registerBM("gaia_mandragora", GaiaMandragoraEntity::new);

    // Technical entities

	// Register utilities

	private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerBM(
		String regName,
		EntityType.EntityFactory<T> creator,
		@Nullable Function<EntityType.Builder<T>, EntityType.Builder<T>> builderModifier)
	{
		return registerBM(ENTITY_TYPES, NFFGirlsGaia.MOD_ID, regName, creator, MobCategory.CREATURE, builder -> {
			builder.sized(0.6f, 1.99f).clientTrackingRange(8);
			if (builderModifier != null) builderModifier.apply(builder);
			return builder;
		});
	}

	private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerBM(
		String regName,
		EntityType.EntityFactory<T> creator,
		float width, float height)
	{
		return registerBM(regName, creator, builder -> builder.sized(width, height));
	}

	private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerBM(
		String regName,
		EntityType.EntityFactory<T> creator)
	{
		return registerBM(regName, creator, null);
	}
}
