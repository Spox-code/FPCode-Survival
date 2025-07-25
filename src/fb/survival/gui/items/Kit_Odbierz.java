package fb.survival.gui.items;

import fb.core.api.HexAPI;
import fb.survival.gui.gui.KitsGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Kit_Odbierz {

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§a§lOdbierz zestaw");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fKliknij #0096fcLPM§f, aby odberac"));
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
