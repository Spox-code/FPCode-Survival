package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.core.api.TabListAPI;
import fb.survival.items.BankNote;
import fb.survival.items.EmblematZycia;
import fb.survival.items.TotemUlaskawienia;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClickEvent implements Listener {

    static RanksAPI ra;

    public ClickEvent(RanksAPI ra){
        ClickEvent.ra = ra;
    }

    @EventHandler
    public void ClickEvent(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack mainitem = p.getInventory().getItemInMainHand();
        ItemStack handitem = p.getInventory().getItemInOffHand();
        if(a == Action.RIGHT_CLICK_BLOCK){
            if(mainitem.hasItemMeta() && mainitem.getItemMeta().hasCustomModelData() && mainitem.getItemMeta().getCustomModelData() == TotemUlaskawienia.iditem){
                e.setCancelled(true);
            }
            if(p.getLocation().getWorld().getName().equalsIgnoreCase("spawn")){
                if(!ra.hasPermission(p, "fb.spawn")) {
                    e.setCancelled(true);
                }
                if(e.getClickedBlock().getType() == Material.ANVIL){
                    e.setCancelled(true);
                    if(p.getLevel() >= 5){
                        if(mainitem.getType() != Material.AIR) {
                            mainitem.setDurability((short) 0);
                            p.setLevel(p.getLevel() - 5);
                            p.sendTitle(HexAPI.hex("#0096fc§lKOWADLO"), HexAPI.hex("§fNaprawiles przedmiot #0096fc" + mainitem.getType().toString().toUpperCase()));
                        }
                    }
                }
            }
        }
        if(a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR){
            if(mainitem.hasItemMeta() && mainitem.getItemMeta().hasCustomModelData() && mainitem.getItemMeta().getCustomModelData() == EmblematZycia.iditem){
                mainitem.setAmount(mainitem.getAmount()-1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 254));
                p.sendTitle(HexAPI.hex("§c§l★ §fEmblemat Zycia §c§l★"), HexAPI.hex("§fTwoje zycie zostalo #0096fculeczone"));
            }else if(mainitem.hasItemMeta() && mainitem.getItemMeta().hasCustomModelData() && mainitem.getItemMeta().getCustomModelData() == BankNote.iditem){
                String lore = mainitem.getItemMeta().getLore().get(1).replace("§fBanknot o wartosci §x§0§0§9§6§F§C", "").replace("$", "");
                try{
                    int amount = Integer.parseInt(lore);
                    BanAPI.addMoney(p.getName(), amount);
                    mainitem.setAmount(mainitem.getAmount()-1);
                    p.sendTitle(HexAPI.hex("#0096fc§lBANK"), HexAPI.hex("§fWplaciles #0096fc" + amount + "$"));
                    TabListAPI.pupdate(p);
                } catch (NumberFormatException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
