package fb.survival.gui.items;

import fb.core.api.HexAPI;
import fb.survival.gui.gui.KitsGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Kit_Gracz {

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§e§l★ §fZestaw §7Gracza §e§l★");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fDostepny: #0096fcTak"));
        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fKliknij #0096fcLPM§f, aby podejrzec"));
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
