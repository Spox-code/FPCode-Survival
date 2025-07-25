package fb.survival.gui.items;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.survival.api.PlayerAPI;
import fb.survival.gui.gui.KitsGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Perk_Deaths {

    public static ItemStack getitem(Player p){
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§e★ §fZwiekszona kasa po zabojstwie §e★");

        ArrayList<String> lore = new ArrayList<>();

        int amount = PlayerAPI.getPerk(p, "deaths");
        int cost = (amount+1)*1500;

        lore.add("");
        lore.add(HexAPI.hex("  §8➡ §fAktualny poziom #0096fc" + amount + "★"));
        lore.add(HexAPI.hex("  §8➡ §fKoszt ulepszenia #0096fc" + cost + "$"));
        lore.add("");
        lore.add(HexAPI.hex("§8➡ #0096fcLista ulepszen"));
        lore.add("");
        int multiple = 0;
        for(int i = 1; i<=amount; i++){
            multiple = i*50;
            lore.add(HexAPI.hex("§8⚫ §f"+ i +"#0096fc★ §8- #0096fc+" + multiple + "$ §fdodatkowych pieniedzy"));
        }
        for(int i = amount+1; i<=5;i++){
            multiple= i*50;
            lore.add(HexAPI.hex("§8⚫ §f"+ i +"§c★ §8- §c+" + multiple + "$ §fdodatkowych pieniedzy"));
        }
        lore.add("");
        if(BanAPI.getPlayerStatMoney(p.getName()) >= cost){
            lore.add(HexAPI.hex("§8➡ #0096fcStac Ciebie na ulepszenie"));
        }else{
            int lost = cost - BanAPI.getPlayerStatMoney(p.getName());
            lore.add(HexAPI.hex("§8➡ §fBrakuje Ci jeszcze #0096FC" + lost + "$"));
        }
        lore.add(HexAPI.hex("§8➡ §fKliknij #0096fcLPM §faby ulepszyc perk"));
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
