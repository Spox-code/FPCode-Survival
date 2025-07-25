package fb.survival.gui.items;

import fb.core.api.HexAPI;
import fb.survival.api.ServerAPI;
import fb.survival.gui.gui.KitsGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Kit_Legenda {

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§e§l★ §fZestaw §6§lLegenda §e§l★");

        ArrayList<String> lore = new ArrayList<>();


        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fDostepny: #0096fc" + KitsGUI.isKits()));
        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fKliknij #0096fcLPM§f, aby podejrzec"));
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
