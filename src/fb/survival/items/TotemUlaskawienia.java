package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TotemUlaskawienia {

    public static int iditem = 3201;

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.PURPLE_BANNER);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        meta.setDisplayName("§e§l★ §fTotem Ulaskawienia §e§l★");

        ArrayList<String> lore = new ArrayList<>();

        lore.add(" §8➡ §fTo jest przedmiot eventowy");
        lore.add(HexAPI.hex(" §8➡ §fEvent #0096fc(Wakacyjny 2025)"));
        lore.add("");
        lore.add(" §8➡ §fGdy umrzesz to twoje przedmioty");
        lore.add(" §8➡ §fzostana wraz z toba");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
