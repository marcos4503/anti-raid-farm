package xyz.windsoft.antiraidfarm.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.windsoft.antiraidfarm.Main;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * This class handle the mod configuration using the Forge Configuration API
 */

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    //Private static constant variables
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    //Private static constant variables, that is the configs available to the user
    private static final ForgeConfigSpec.IntValue RAID_CREATION_COOLDOWN = BUILDER
            .comment("The cooldown for creation of Raids, in seconds. (Type of INT. Range of 1~99999999999.)")
            .defineInRange("raidCreationCooldown", 60, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.BooleanValue PREVENT_BAD_OMEN_REMOTION = BUILDER
            .comment("Prevent Bad Omen effect remotion, by drinking Milk Bucket or Milk Bottle. (Type of BOOL. true/false.)")
            .define("preventBadOmenRemotion", true);
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> RAID_CREATION_RANDOM_COOLDOWNS = BUILDER
            .comment("A list of random cooldowns for creation of Raids, in seconds. NOT IMPLEMENTED YET! (Type of INT_ARRAY. For each INT item: Range of 1~99999999999.)")
            .defineListAllowEmpty("raidCreationRandomCooldowns", List.of(60, 120), Config::ValidateCooldowns);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> COOLDOWN_DISPLAY_BLOCK_ITEMS = BUILDER
            .comment("A list of items that block cooldown display, if in hand of the player. NOT IMPLEMENTED YET! (Type of STRING_ARRAY. For each STRING item example: \"minecraft:iron_ingot\".)")
            .defineListAllowEmpty("cooldownDisplayBlockItems", List.of("minecraft:stick"), Config::ValidateItemNames);

    //Public static constant variables
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    //Public static variables
    public static int raidCreationCooldown = -1;
    public static boolean preventBadOmenRemotion = false;
    public static Set<Integer> raidCreationRandomCooldowns = null;
    public static Set<Item> cooldownDisplayBlockItems = null;

    //Private static methods

    private static boolean ValidateCooldowns(final Object obj) {
        //Return true if the item is INT
        return (obj instanceof final Integer itemName && true);
    }

    private static boolean ValidateItemNames(final Object obj) {
        //Return true if the item id exists
        return (obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName)));
    }

    //Public events

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        //Get the configs loaded from file
        raidCreationCooldown = RAID_CREATION_COOLDOWN.get();
        preventBadOmenRemotion = PREVENT_BAD_OMEN_REMOTION.get();
        raidCreationRandomCooldowns = RAID_CREATION_RANDOM_COOLDOWNS.get().stream().map(itemName -> ((int) itemName)).collect(Collectors.toSet());
        cooldownDisplayBlockItems = COOLDOWN_DISPLAY_BLOCK_ITEMS.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet());
    }
}
