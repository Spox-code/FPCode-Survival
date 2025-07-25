package fb.survival.events;

import fb.core.api.RanksAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    static RanksAPI ra;

    public BlockPlace(RanksAPI ra){
        BlockPlace.ra = ra;
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Location loc = p.getLocation();
        if(loc.getWorld().getName().equalsIgnoreCase("spawn")){
            if(!ra.hasPermission(p, "fb.spawn.bypass")){
                e.setCancelled(true);
                p.sendMessage("§cNie mozesz tego robic na spawnie");
            }
        }
    }
}
