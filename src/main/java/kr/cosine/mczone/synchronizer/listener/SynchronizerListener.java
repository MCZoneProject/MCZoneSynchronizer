package kr.cosine.mczone.synchronizer.listener;

import kr.cosine.mczone.synchronizer.MCZoneSynchronizer;
import kr.cosine.mczone.synchronizer.database.DataSource;
import kr.cosine.mczone.synchronizer.database.dao.SynchronizerDao;
import kr.cosine.mczone.synchronizer.database.vo.SynchronizerVo;
import kr.cosine.mczone.synchronizer.service.SynchronizerService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCZoneSynchronizer.MOD_ID, value = Dist.DEDICATED_SERVER)
public class SynchronizerListener {
    private static boolean isServerStopping = false;

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        DataSource.connect();
        SynchronizerDao.createTable();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        isServerStopping = true;
        event.getServer().getPlayerList().getPlayers().forEach(SynchronizerDao::save);
        DataSource.disconnect();
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SynchronizerService.setMaxHealth(player, SynchronizerVo.DEFAULT_HEALTH);
            player.setHealth(SynchronizerVo.DEFAULT_HEALTH);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (isServerStopping) return;
        if (event.getEntity() instanceof ServerPlayer player) {
            SynchronizerDao.saveAsync(player);
        }
    }
}