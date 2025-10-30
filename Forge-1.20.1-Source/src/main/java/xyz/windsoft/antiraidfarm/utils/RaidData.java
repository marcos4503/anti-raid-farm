package xyz.windsoft.antiraidfarm.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

/*
 * This class handle data about a Raid that a Player can be started some time. The data is saved in NBT of the Player, on "ForgeData" path
 */

public class RaidData {

    //Public static methods

    public static void SetData(ServerPlayer serverPlayer, Long data){
        //Save the data into player NBT persistent data
        CompoundTag playerPersistentNbt = serverPlayer.getPersistentData();
        playerPersistentNbt.putLong("lastRaidCreated", data);
    }

    public static Long GetData(ServerPlayer serverPlayer){
        //Try to get the data from player NBT persistent data
        CompoundTag playerPersistentNbt = serverPlayer.getPersistentData();

        //If exists, return the data
        if (playerPersistentNbt.contains("lastRaidCreated", Tag.TAG_LONG) == true)
            return playerPersistentNbt.getLong("lastRaidCreated");

        //If not, return a empty data
        return -1L;
    }

    public static void ResetData(ServerPlayer serverPlayer){
        //Reset the data into player NBT persistent data
        CompoundTag playerPersistentNbt = serverPlayer.getPersistentData();
        playerPersistentNbt.putLong("lastRaidCreated", -1L);
    }
}