package kr.cosine.mczone.synchronizer;

import kr.cosine.mczone.synchronizer.config.SynchronizerDatabaseConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(MCZoneSynchronizer.MOD_ID)
public class MCZoneSynchronizer {
    public static final String MOD_ID = "mczonesynchronizer";

    public MCZoneSynchronizer() {
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, SynchronizerDatabaseConfig.spec, MOD_ID + "-database.toml");
    }
}
