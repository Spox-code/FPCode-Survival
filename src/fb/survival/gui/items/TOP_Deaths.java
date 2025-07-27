package fb.survival.gui.items;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TOP_Deaths {

    public static ItemStack getitem(Player p){
        ItemStack item = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(HexAPI.hex("§b★ #0096fc§lTopka Smierci §b★"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fTwoje smierci: #0096fc" + BanAPI.getPlayerStatDeaths(p.getName())));
        lore.add("");
        lore.add(HexAPI.hex("§8§m--------§r#0096fc§lTOPKI§8§m--------"));
        lore.add("");
        for(int i = 1; i<=5; i++){
            if(!BanAPI.getTopDeathsName(i).equals("-")) {
                lore.add(HexAPI.hex("§7" + i + ". §f" + BanAPI.getTopDeathsName(i) + "§8: #0096fc" + BanAPI.getTopDeathsAmount(i)));
            }else{
                lore.add(HexAPI.hex("§7" + i + ". §f"));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
