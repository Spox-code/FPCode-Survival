package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.MarketManager;
import fb.survival.gui.gui.PerkiGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerClickPlayer implements Listener {

    static RanksAPI ra;

    public PlayerClickPlayer(RanksAPI ra){
        PlayerClickPlayer.ra = ra;
    }

    @EventHandler
    public void PlayerClickPlayer(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if(e.getRightClicked() instanceof Player p2){
            if(p.isSneaking()){
                p.sendMessage("");
                p.sendMessage(HexAPI.hex("§8§m--------§r#0096fc" + p2.getName() + "§8§m--------"));
                p.sendMessage("");
                p.sendMessage(HexAPI.hex("  §8➡ §fNick #0096fc" + p2.getName()));
                p.sendMessage(HexAPI.hex("  §8➡ §fRanga #0096fc" + ra.getRankPrefix(ra.getRank(p2.getName()))));
                p.sendMessage(HexAPI.hex("  §8➡ §fPieniadze #0096fc" + BanAPI.getPlayerStatMoney(p2.getName()) + "$"));
                p.sendMessage(HexAPI.hex("  §8➡ §fZabojstwa #0096fc" + BanAPI.getPlayerStatKills(p2.getName())));
                p.sendMessage(HexAPI.hex("  §8➡ §fSmierci #0096fc" + BanAPI.getPlayerStatDeaths(p2.getName())));
                p.sendMessage("");
            }
        }
        if(p.getWorld().getName().equalsIgnoreCase("spawn")){
            if(e.getRightClicked().getType() == EntityType.WITCH){
                e.setCancelled(true);
                PerkiGUI.OpenGUI(p);
            }else if(e.getRightClicked().getType() == EntityType.VINDICATOR){
                e.setCancelled(true);
                MarketManager.openMarketGUI(p, 0);
            }
        }
    }
}
