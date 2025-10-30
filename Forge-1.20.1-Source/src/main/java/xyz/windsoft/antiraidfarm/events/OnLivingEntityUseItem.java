package xyz.windsoft.antiraidfarm.events;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.windsoft.antiraidfarm.config.Config;

/*
 * This class have events to process when the entity Player is consuming a item, like a Milk Bucket, Potion, etc.
 * This is used to prevent that the Bad Omen effect, be removed.
 */

public class OnLivingEntityUseItem {

    //Public events

    @SubscribeEvent
    public void onLivingEntityUseItemStart(LivingEntityUseItemEvent.Start event){
        //If the entity is null, stop here
        if (event.getEntity() == null)
            return;

        //If not is the logical server, stop here
        if (event.getEntity().level().isClientSide() == true)
            return;

        //If the entity of this event, is not a Server Player, cancel
        if ((event.getEntity() instanceof ServerPlayer) == false)
            return;



        //If the "preventBadOmenRemotion" in Config, is disabled, cancel this
        if (Config.preventBadOmenRemotion == false)
            return;



        //If the Player is not drinking milk, cancel
        ResourceLocation resOfItemBeingUsed = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());
        String idOfItemBeingUsed = (resOfItemBeingUsed.getNamespace() + ":" + resOfItemBeingUsed.getPath());
        if (idOfItemBeingUsed.contains("milk") == false)
            return;
        //Get the ServerPlayer instance, of this player
        ServerPlayer serverPlayer = ((ServerPlayer) event.getEntity());
        //If the Player don't have a "Bad Omen" effect, stop here
        if (serverPlayer.hasEffect(MobEffects.BAD_OMEN) == false)
            return;
        //Get the information about remaining Bad Omen duration and amplifier, and store it
        MobEffectInstance badOmenEffect = serverPlayer.getEffect(MobEffects.BAD_OMEN);
        CompoundTag playerPersistentNbt = serverPlayer.getPersistentData();
        playerPersistentNbt.putBoolean("beforeDrinkMilkBadOmenIsOn", true);
        playerPersistentNbt.putLong("beforeDrinkMilkBadOmenTime", badOmenEffect.getDuration());
        playerPersistentNbt.putLong("beforeDrinkMilkBadOmenAmp", badOmenEffect.getAmplifier());
    }

    @SubscribeEvent
    public void onLivingEntityUseItemStop(LivingEntityUseItemEvent.Stop event){
        //If the entity is null, stop here
        if (event.getEntity() == null)
            return;

        //If not is the logical server, stop here
        if (event.getEntity().level().isClientSide() == true)
            return;

        //If the entity of this event, is not a Server Player, cancel
        if ((event.getEntity() instanceof ServerPlayer) == false)
            return;



        //If the "preventBadOmenRemotion" in Config, is disabled, cancel this
        if (Config.preventBadOmenRemotion == false)
            return;



        //Get the ServerPlayer instance, of this player
        ServerPlayer serverPlayer = ((ServerPlayer) event.getEntity());
        //Retrive Player data
        CompoundTag playerPersistentNbt = serverPlayer.getPersistentData();
        //If don't have data about a previous Bad Omen effect, before start drinking Milk, stop here
        if (playerPersistentNbt.contains("beforeDrinkMilkBadOmenIsOn", Tag.TAG_BYTE) == false)
            return;
        //Clear the temporary information about a previous Bad Omen effect
        playerPersistentNbt.remove("beforeDrinkMilkBadOmenIsOn");
        playerPersistentNbt.remove("beforeDrinkMilkBadOmenTime");
        playerPersistentNbt.remove("beforeDrinkMilkBadOmenAmp");
    }

    @SubscribeEvent
    public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event){
        //If the entity is null, stop here
        if (event.getEntity() == null)
            return;

        //If not is the logical server, stop here
        if (event.getEntity().level().isClientSide() == true)
            return;

        //If the entity of this event, is not a Server Player, cancel
        if ((event.getEntity() instanceof ServerPlayer) == false)
            return;



        //If the "preventBadOmenRemotion" in Config, is disabled, cancel this
        if (Config.preventBadOmenRemotion == false)
            return;



        //Get the ServerPlayer instance, of this player
        ServerPlayer serverPlayer = ((ServerPlayer) event.getEntity());
        //Retrive Player data
        CompoundTag playerPersistentNbt = serverPlayer.getPersistentData();
        //If don't have data about a previous Bad Omen effect, before start drinking Milk, stop here
        if (playerPersistentNbt.contains("beforeDrinkMilkBadOmenIsOn", Tag.TAG_BYTE) == false)
            return;
        //Re-apply the Bad Omen effect in the Player, with the same duration and amplifier
        serverPlayer.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, ((int) playerPersistentNbt.getLong("beforeDrinkMilkBadOmenTime")), ((int) playerPersistentNbt.getLong("beforeDrinkMilkBadOmenAmp")), false, false, true));
        //Clear the temporary information about a previous Bad Omen effect
        playerPersistentNbt.remove("beforeDrinkMilkBadOmenIsOn");
        playerPersistentNbt.remove("beforeDrinkMilkBadOmenTime");
        playerPersistentNbt.remove("beforeDrinkMilkBadOmenAmp");
    }
}
