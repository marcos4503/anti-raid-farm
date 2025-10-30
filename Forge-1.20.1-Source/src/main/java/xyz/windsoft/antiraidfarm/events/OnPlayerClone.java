package xyz.windsoft.antiraidfarm.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.windsoft.antiraidfarm.utils.RaidData;

/*
 * This class have events to process when the entity Player is cloned typically caused by the impl sending a RESPAWN_PLAYER event.
 * Either caused by death, or by traveling from the End to the overworld.
 */

public class OnPlayerClone {

    //Public events

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        //If the entity is null, stop here
        if (event.getEntity() == null)
            return;

        //If not is the logical server, stop here
        if (event.getEntity().level().isClientSide() == true)
            return;

        //If the entity of this event, is not a Server Player, cancel
        if ((event.getEntity() instanceof ServerPlayer) == false)
            return;



        //Get the Server Player data
        boolean thisCloningIsByDeath = event.isWasDeath();
        ServerPlayer oldServerPlayer = ((ServerPlayer) event.getOriginal());
        ServerPlayer newServerPlayer = ((ServerPlayer) event.getEntity());

        //Get last raid created, data, in this Server Player
        Long lastRaidCreated = RaidData.GetData(oldServerPlayer);
        //Copy the last raid created data, to the new Server Player entity
        if (lastRaidCreated != null && lastRaidCreated != -1L)
            RaidData.SetData(newServerPlayer, lastRaidCreated);
    }
}
