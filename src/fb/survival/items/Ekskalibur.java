package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Ekskalibur {

    public static int iditem = 3202;

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        meta.setDisplayName("§e§l★ §eEkskalibur §e§l★");

        ArrayList<String> lore = new ArrayList<>();

        lore.add(" §8➡ §fTo jest przedmiot eventowy");
        lore.add(HexAPI.hex(" §8➡ §fEvent #0096fc(Ferie 2025)"));
        lore.add("");
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
