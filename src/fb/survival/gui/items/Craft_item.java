package fb.survival.gui.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Craft_item {

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(HexAPI.hex("#0096fc§lCrafting"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add("§fJezeli posidasz wszystkie przedmioty");
        lore.add(HexAPI.hex("§fKliknij przycisk aby scraftowac ten #0096fcprzedmiot"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
