package net.sodiumzh.nff.girls.gaia.registry;

import net.sodiumzh.nff.girls.entity.tamingprocess.HmagAlrauneTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.HmagEnderExecutorTamingProcess;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.*;
import net.sodiumzh.nff.girls.registry.NFFGirlsFriendingItems;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFRegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsGaiaTamingProcesses {

    public static final NFURegistryEntryCollection<NFFTamingProcess> TAMING_PROCESSES =
            NFURegistryEntryCollection.create(NFFRegistries.TAMING_PROCESSES, NFFGirlsGaia.MOD_ID);

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_DRYAD = TAMING_PROCESSES.register(
            "gaia_dryad",  () -> new HmagAlrauneTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.PLANT_B));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_ANIMAL_A = TAMING_PROCESSES.register(
        "gaia_animal_a",  () -> new GaiaAnimalTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.ANIMAL_A));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_VALKYRIE = TAMING_PROCESSES.register(
        "gaia_valkyrie", GaiaValkyrieTamingProcess::new);

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_YUKI_ONNA = TAMING_PROCESSES.register(
        "gaia_yuki_onna", () -> new GaiaYukiOnnaTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.SNOWMAN));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_AQUATIC_A = TAMING_PROCESSES.register(
        "gaia_aquatic_a", () -> new GaiaAquaticTamingProcess().setItemGivingTableOverride(NFFGirlsGaiaFriendingItems.AQUATIC_A));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_WITCH = TAMING_PROCESSES.register(
        "gaia_witch", () -> new GaiaWitchProcess().setItemGivingTableOverride(NFFGirlsGaiaFriendingItems.WITCH));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_ENDERMAN_B = TAMING_PROCESSES.register(
        "gaia_enderman_b", () -> new HmagEnderExecutorTamingProcess().setItemGivingTableOverride(NFFGirlsGaiaFriendingItems.ENDERMAN_B));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_ANT_WORKER = TAMING_PROCESSES.register(
        "gaia_ant_worker", () -> new GaiaAntWorkerFriendingProcess().setItemGivingTableOverride(NFFGirlsGaiaFriendingItems.ARTHROPOD));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_ARACHNE = TAMING_PROCESSES.register(
        "gaia_arachne", () -> new GaiaArachneFriendingProcess().setItemGivingTableOverride(NFFGirlsGaiaFriendingItems.ARTHROPOD));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_SIREN = TAMING_PROCESSES.register(
        "gaia_siren", () -> new GaiaSirenFriendingProcess().setItemGivingTableOverride(NFFGirlsGaiaFriendingItems.HUMANOID_A));
}
