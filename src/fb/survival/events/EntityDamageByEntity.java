package fb.survival.events;

import fb.actionbar.api.ActionBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityDamageByEntity implements Listener {

    public static Map<Player, Boolean> antylogoutstatus = new HashMap<>();
    public static Map<Player, Integer> antylogouttime = new HashMap<>();
    public static Map<Player, Player> antylogoutwithplayer = new HashMap<>();

    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player p && e.getDamager() instanceof Player p2) {
            if (!p.getWorld().getName().equals("spawn")) {
                antylogouttime.put(p, 30);
                antylogoutstatus.put(p, true);
                antylogouttime.put(p2, 30);
                antylogoutstatus.put(p2, true);
                antylogoutwithplayer.put(p, p2);
                antylogoutwithplayer.put(p2, p);
                ActionBar.sendMessage(p, "§9§lANTYLOGOUT §f" + EntityDamageByEntity.antylogouttime.get(p) + "sek");
                ActionBar.sendMessage(p2, "§9§lANTYLOGOUT §f" + EntityDamageByEntity.antylogouttime.get(p2) + "sek");
            }
        }
    }
}
