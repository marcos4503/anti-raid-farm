package xyz.windsoft.antiraidfarm.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.windsoft.antiraidfarm.config.Config;
import xyz.windsoft.antiraidfarm.utils.RaidData;
import xyz.windsoft.antiraidfarm.utils.TimeConverter;

/*
 * This class have the Mixins for inject code into "Raids" mechanic
 */

//All code changes by Injections, Overwrites, Redirections, etc. in this class, will apply to the "Raids.class" of the game code...
@Mixin(Raids.class)
public abstract class RaidsMixin {

    //Private methods
    private Raids self(){
        //Mixin can't use "this" keyword inside methods code, so, calling this method is the same of use "this" keyword, to be used inside of Mixin methods code...
        return (Raids)(Object) this;
    }

    //Public Injetions

    @Inject(method = "createOrExtendRaid", at = @At("HEAD"), cancellable = true)
    public void createOrExtendRaid(ServerPlayer pServerPlayer, CallbackInfoReturnable<Raid> cir){
        //The method "createOrExtendRaid()" of "Raids" class, is responsible by the creation of Raids during game.

        //If not is the logical server, stop the injection here
        if (pServerPlayer.level().isClientSide() == true)
            return;



        //Get data about the last raid created by this player...
        Long lastRaidCreated = RaidData.GetData(pServerPlayer);

        //If never has started a raid before...
        if (lastRaidCreated == null || lastRaidCreated == -1L) {
            //Proceed to raid creation...
            WarnAboutRaidCreationAndStoreDataAboutRaidCreatedByThePlayer(pServerPlayer);
            //Stop the injection here
            return;
        }

        //Calculate the time elapsed since the last raid creation, by this player...
        Long timeElapsedSinceLastRaidCreated = ((pServerPlayer.serverLevel().getGameTime()) - lastRaidCreated);

        //Get the raid creation cooldown config
        int raidCreationCooldown = (Config.raidCreationCooldown * 20);

        //If the elapsed time since last raid creation, is not greather than the raid creation cooldown...
        if (timeElapsedSinceLastRaidCreated < raidCreationCooldown){
            //Convert cooldown left time, to readable by humans
            String timeLeft = TimeConverter.ConvertTicksToTime((raidCreationCooldown - timeElapsedSinceLastRaidCreated));

            //Send a message to the player
            pServerPlayer.displayClientMessage(Component.translatable("clientMessage.antiraidfarm.raid_start_cooldown", timeLeft).withStyle(ChatFormatting.GOLD), true);

            //Force the interruption of the raid creation
            cir.setReturnValue(null);
            cir.cancel();
        }
        //If the elapsed time since last raid creation, is greather than the raid creation cooldown...
        if (timeElapsedSinceLastRaidCreated >= raidCreationCooldown){
            //Proceed to raid creation...
            WarnAboutRaidCreationAndStoreDataAboutRaidCreatedByThePlayer(pServerPlayer);
        }
    }

    //Private static methods

    private static void WarnAboutRaidCreationAndStoreDataAboutRaidCreatedByThePlayer(ServerPlayer pServerPlayer){
        //Send a message to the player
        pServerPlayer.displayClientMessage(Component.translatable("clientMessage.antiraidfarm.raid_start_warning").withStyle(ChatFormatting.DARK_RED), true);

        //Reset the raid information
        RaidData.ResetData(pServerPlayer);

        //Get the current world time and store data about this new raid created by this player...
        Long currentWorldTime = pServerPlayer.serverLevel().getGameTime();
        RaidData.SetData(pServerPlayer, currentWorldTime);
    }
}
