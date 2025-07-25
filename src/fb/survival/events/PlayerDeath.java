package fb.survival.events;

import fb.actionbar.api.ActionBar;
import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.TabListAPI;
import fb.survival.api.PlayerAPI;
import fb.survival.api.ServerAPI;
import fb.survival.items.TotemUlaskawienia;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        p.teleport(ServerAPI.getSpawn());
        ItemStack seocnditem = p.getInventory().getItemInOffHand();
        if(seocnditem.hasItemMeta() && seocnditem.getItemMeta().hasCustomModelData() && seocnditem.getItemMeta().getCustomModelData() == TotemUlaskawienia.iditem){
            e.setKeepInventory(true);
            seocnditem.setAmount(seocnditem.getAmount()-1);
            e.getDrops().clear();
        }
        p.getWorld().strikeLightningEffect(p.getLocation());
        p.sendTitle(HexAPI.hex("#0096fc§lSMIERC"), HexAPI.hex("§fZginales #0096fc☠"));
        BanAPI.addDeaths(p.getName(), 1);
        TabListAPI.pupdate(p);
        e.setDeathMessage(HexAPI.hex("§8[#0096fc☠§8] §fGracz #0096fc" + p.getName() + " §fumarl"));
        if(p.getKiller() instanceof Player){
            Player killer = p.getKiller();
            BanAPI.addKills(killer.getName(), 1);
            TabListAPI.pupdate(killer);
            killer.sendTitle(HexAPI.hex("#0096fc§lZABOJSTWO"), HexAPI.hex("§fZabiles gracza #0096fc" + p.getName() + "⚔"));
            EntityDamageByEntity.antylogoutstatus.put(killer, false);
            EntityDamageByEntity.antylogouttime.put(killer, 30);
            killer.sendMessage("§aNie jestes juz podczas walki");
            EntityDamageByEntity.antylogoutstatus.put(p, false);
            EntityDamageByEntity.antylogouttime.put(p, 30);
            p.sendMessage("§aNie jestes juz podczas walki");
            ActionBar.sendMessage(p, "§9§lANTYLOGOUT §fNie jestes juz podczas walki");
            ActionBar.sendMessage(killer, "§9§lANTYLOGOUT §fNie jestes juz podczas walki");
            int perk = PlayerAPI.getPerk(killer, "deaths");
            int amountperk = perk*50;
            BanAPI.addMoney(killer.getName(), amountperk);
        }
    }
}
