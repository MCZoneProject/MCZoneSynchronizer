package kr.cosine.mczone.synchronizer.listener;

import kr.cosine.mczone.synchronizer.MCZoneSynchronizer;
import kr.cosine.mczone.synchronizer.database.DataSource;
import kr.cosine.mczone.synchronizer.database.dao.SynchronizerDao;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCZoneSynchronizer.MOD_ID, value = Dist.DEDICATED_SERVER)
public class SynchronizerListener {
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        DataSource.connect();
        SynchronizerDao.createTable();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        DataSource.disconnect();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();
        SynchronizerDao.setInventory(player);
    }
}