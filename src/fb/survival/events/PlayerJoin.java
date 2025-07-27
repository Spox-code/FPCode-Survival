package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.Main;
import fb.survival.api.PlayerAPI;
import fb.survival.cmds.Spawn;
import fb.survival.cmds.Sprawdzanie;
import fb.survival.cmds.Vanish;
import fb.survival.gui.gui.HomeGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    static Main plugin;
    private static RanksAPI ra;

    public PlayerJoin(Main m, RanksAPI ra){
        plugin = m;
        PlayerJoin.ra = ra;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.hasPlayedBefore()){
            PlayerAPI.setup(p);
            BanAPI.setupPlayerSurvival(p.getName());
        }
        Vanish.vanishlist.put(p, false);
        for(Player ps : Bukkit.getOnlinePlayers()){
            if(Vanish.vanishlist.get(ps)){
                if(!ra.hasPermission(p, "fb.vanish")) {
                    p.hidePlayer(ps);
                }
            }
        }
        Sprawdzanie.playerisCheck.put(p, false);
        Spawn.teleport.put(p, 5);
        Spawn.teleportstatus.put(p, false);
        HomeGUI.isteleport.put(p, false);
        HomeGUI.teleporttime.put(p, 5);
        EntityDamageByEntity.antylogoutstatus.put(p, false);
        EntityDamageByEntity.antylogouttime.put(p, 30);
        p.sendTitle(HexAPI.hex("#0096fc§lFPCode"), HexAPI.hex("§fWitaj na trybie #0096fcSurvival"));
    }
}
