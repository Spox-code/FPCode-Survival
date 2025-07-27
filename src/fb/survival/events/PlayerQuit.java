package fb.survival.events;

import fb.actionbar.api.ActionBar;
import fb.core.api.BanAPI;
import fb.survival.api.ServerAPI;
import fb.survival.cmds.Spawn;
import fb.survival.cmds.Sprawdzanie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(EntityDamageByEntity.antylogoutstatus.get(p)){
            Player killer = EntityDamageByEntity.antylogoutwithplayer.get(p);
            p.damage(10000000, killer);
            for(Player ps : Bukkit.getOnlinePlayers()){
                ps.sendMessage("§cGracz " + p.getName() + " wylogowal sie podczas walki");
            }
            EntityDamageByEntity.antylogouttime.put(killer, 30);
            EntityDamageByEntity.antylogoutstatus.put(killer, false);
            killer.sendMessage("§aNie jestes juz podczas walki");
            ActionBar.sendMessage(killer, "§9§lANTYLOGOUT §fNie jestes juz podczas walki");
        }
        if(Sprawdzanie.playerisCheck.get(p)){
            p.teleport(ServerAPI.getSpawn());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tempban " + p.getName() + " 30d Wylogowanie sie podczas cheatow");
        }
    }
}
