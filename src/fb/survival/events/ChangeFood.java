package fb.survival.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class ChangeFood implements Listener {

    @EventHandler
    public void ChangeFood(FoodLevelChangeEvent e){
        Player p = (Player) e.getEntity();
        if(p.getWorld().getName().equals("spawn")){
            e.setCancelled(true);
        }
    }
}
