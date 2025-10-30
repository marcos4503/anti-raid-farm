package xyz.windsoft.antiraidfarm;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import xyz.windsoft.antiraidfarm.config.Config;
import xyz.windsoft.antiraidfarm.events.OnLivingEntityUseItem;
import xyz.windsoft.antiraidfarm.events.OnPlayerClone;

/*
 * This class is the Entry Point for this mod
 */

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    //Public classes
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        //Can use "@Mod.EventBusSubscriber" to automatically register all static methods in the class annotated with @SubscribeEvent...

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("Anti Raid Farm mod starting on client... >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    //Public static variables
    public static final String MODID = "antiraidfarm";
    private static final Logger LOGGER = LogUtils.getLogger();

    //Public methods

    public Main() {
        //Get the mod event bus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Register the mod needed events, in forge mod event bus
        MinecraftForge.EVENT_BUS.register(new OnPlayerClone());
        MinecraftForge.EVENT_BUS.register(new OnLivingEntityUseItem());

        //Register the "CommonSetup" method for modloading
        modEventBus.addListener(this::CommonSetup);

        //Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Register the mod ForgeConfigSpec, for Forge can create and load the config file
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        //Can use "@SubscribeEvent" and let the Event Bus discover methods to call...

        //Do something when the server starts
        LOGGER.info("Anti Raid Farm mod starting on server...");
    }

    //Private methods

    private void CommonSetup(final FMLCommonSetupEvent event) {
        //Some common setup code
        LOGGER.info("Anti Raid Farm mod starting!");
        LOGGER.info("Configs loaded...");
        LOGGER.info("raidCreationCooldown: " + Config.raidCreationCooldown);
        LOGGER.info("preventBadOmenRemotion: " + Config.preventBadOmenRemotion);
        LOGGER.info("raidCreationRandomCooldowns: " + Config.raidCreationRandomCooldowns.toString());
        LOGGER.info("cooldownDisplayBlockItems: " + Config.cooldownDisplayBlockItems.toString());
    }
}
