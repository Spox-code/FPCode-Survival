package fb.survival.events;

import fb.survival.api.ServerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        ServerAPI.randomTeleport(p);
    }
}
