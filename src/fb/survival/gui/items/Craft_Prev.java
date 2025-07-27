package fb.survival.gui.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Craft_Prev {


    public static ItemStack getitem(){
        // Używamy strzałki w prawo (ARROW) jako ikony "Dalej"
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        // Nazwa z efektem pogrubienia i Twoimi kolorami
        meta.setDisplayName(HexAPI.hex("#0096fc§l» §b§lPoprzedni Crafting #0096fc§l«"));

        List<String> lore = new ArrayList<>(); // Używamy List

        lore.add("");
        // Wyraźna instrukcja
        lore.add(HexAPI.hex("§7Kliknij, aby zobaczyć §b§lpoprzednie receptury§7."));
        lore.add("");

        meta.setLore(lore);

        // Dodaje "magiczny blask"
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // Ukryj atrybuty, jeśli jakieś są

        item.setItemMeta(meta);
        return item;
    }
}