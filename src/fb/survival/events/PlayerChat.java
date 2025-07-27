package fb.survival.events;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.cmds.Sprawdzanie;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChat implements Listener {

    static RanksAPI ra;

    public PlayerChat(RanksAPI ra){
        PlayerChat.ra = ra;
    }

    @EventHandler
    public void PlayerChat(PlayerChatEvent e){
        Player p = e.getPlayer();
        if(Sprawdzanie.playerisCheck.get(p)){
            e.setFormat(HexAPI.hex("§8[#0096fcSPRAWDZANY§8] " + ra.getRankPrefix(ra.getRank(p.getName())) + " %s§8: " + ra.getRankSuffix(ra.getRank(p.getName())) + "%s"));
        }
    }
}
