package fb.survival.events;

import fb.core.api.HexAPI;
import fb.survival.Systems;
import fb.survival.api.ServerAPI;
import fb.survival.cmds.Spawn;
import fb.survival.gui.gui.HomeGUI;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location loc = p.getLocation();
        if(loc.getWorld().getName().equalsIgnoreCase("spawn")){
            if(Systems.isNearWorldBorder(loc, loc.getWorld().getWorldBorder(), 3)){
                ServerAPI.randomTeleport(p);
            }
        }
        if(Spawn.teleportstatus.get(p)){
            Spawn.teleportstatus.put(p, false);
            Spawn.teleport.put(p, 5);
            p.sendTitle(HexAPI.hex("#0096fc§lTELEPORT"), HexAPI.hex("§fTeleportacja #0096fcanulowana"));
        }
        if(HomeGUI.isteleport.get(p)){
            HomeGUI.isteleport.put(p, false);
            HomeGUI.teleporttime.put(p, 5);
            p.sendTitle(HexAPI.hex("#0096fc§lTELEPORT"), HexAPI.hex("§fTeleportacja #0096fcanulowana"));
        }
    }
}
