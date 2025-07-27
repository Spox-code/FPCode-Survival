package fb.survival.events;

import fb.survival.cmds.Sprawdzanie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerSendCommand implements Listener {

    @EventHandler
    public void PlayerSendCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        String msg = e.getMessage();
        if(EntityDamageByEntity.antylogoutstatus.get(p)){
            if(msg.startsWith("/")){
                e.setCancelled(true);
                p.sendMessage("§cNie mozesz wpisywac komend podczas walki!");
            }
        }
        if(Sprawdzanie.playerisCheck.get(p)){
            if(msg.startsWith("/")){
                e.setCancelled(true);
                p.sendMessage("§cNie mozesz wykonywac komend podczas sprawdzania");
            }
        }
    }
}
