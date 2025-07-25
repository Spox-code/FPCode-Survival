package fb.survival.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {



    @EventHandler
    public void PlayerDamage(EntityDamageEvent e){
        Entity en = e.getEntity();
        if(en.getWorld().getName().equalsIgnoreCase("spawn")){
            e.setCancelled(true);
        }
    }
}
