package fb.survival.gui.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Kit_Return {

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.OAK_FENCE_GATE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§c§lWroc");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(HexAPI.hex(" §8➡ §fKliknij #0096fcLPM§f, aby wrocic"));
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
