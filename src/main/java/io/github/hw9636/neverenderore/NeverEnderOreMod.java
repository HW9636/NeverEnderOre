package io.github.hw9636.neverenderore;

import com.mojang.logging.LogUtils;
import io.github.hw9636.neverenderore.common.ModRegistration;
import io.github.hw9636.neverenderore.common.config.NEOConfig;
import io.github.hw9636.neverenderore.common.integration.TOPProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(NeverEnderOreMod.MODID)
public class NeverEnderOreMod
{
    public static final String MODID = "neverenderore";
    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean TOPLoaded;

    public NeverEnderOreMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueEMC);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NEOConfig.COMMON_SPEC);

        // Register Registries
        ModRegistration.BLOCKS.register(modEventBus);
        ModRegistration.ITEMS.register(modEventBus);
        ModRegistration.BLOCK_ENTITIES.register(modEventBus);
        ModRegistration.CONTAINERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        this.TOPLoaded = false;
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        this.TOPLoaded = ModList.get().isLoaded("theoneprobe");


    }

    private void enqueueEMC(final InterModEnqueueEvent event) {
        if (TOPLoaded) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
        }
    }

}
