package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EmblematZycia {

    public static int iditem = 3203;

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        meta.setDisplayName("§c§l★ §fEmblemat Zycia §c§l★");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fGdy umierasz uzyj tego #0096fcprzedmioty"));
        lore.add(HexAPI.hex(" §8➡ §fto Twoje zycie zostanie #0096fculeczone"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
