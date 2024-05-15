package cn.vusv.plugin;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.entity.Entity;
import cn.nukkit.Player;
import cn.vusv.plugin.config.PlayerData;

public class Events implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (NavMarkPlugin.playerDataMap.containsKey(playerName)) {
            PlayerData data = NavMarkPlugin.playerDataMap.get(playerName);
            Entity npc = player.getServer().getLevelByName(data.levelName).getEntity(data.entityId);
            if (npc != null) {
                npc.close();
            }
            NavMarkPlugin.navMarkNpcData.remove(data.entityId);
            NavMarkPlugin.playerDataMap.remove(playerName);
        }
    }
}
