package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumzh.nff.girls.entity.hmag.HmagSkeletonGirlEntity;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.*;
import net.sodiumzh.nff.girls.registry.NFFGirlsBaubles;
import net.sodiumzh.nfu.item.bauble.RegisterBaubleEquippableMobsEvent;

@EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaBaubles
{
    @SubscribeEvent
    public static void baubleEquippableRegistration(RegisterBaubleEquippableMobsEvent event)
    {
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaDryadEntity.class, 0, 3);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaSprigganEntity.class, 0, 3);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaDullahanEntity.class, 2, 6);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaHarpyEntity.class, 0, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaBansheeEntity.class, 0, 3);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaSuccubusEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaMummyEntity.class, 2, 6);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaBeeEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaValkyrieEntity.class, 2, 6);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaYukiOnnaEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaCecaeliaEntity.class, 2, 6);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaMermaidEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaWerecatEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaWitchEntity.class, 2, 6);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaShamanEntity.class, 2, 6);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaEnderDragonGirlEntity.class, 0, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaSirenEntity.class, 2, 5);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaAntWorkerEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaArachneEntity.class, 2, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaSludgeGirlEntity.class, 0, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaMandragoraEntity.class, 0, 4);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaCentaurEntity.class, 2, 5);
        NFFGirlsBaubles.registerWithContinuousSlotSequence(event, GaiaSatyressEntity.class, 2, 6);
    }
}
