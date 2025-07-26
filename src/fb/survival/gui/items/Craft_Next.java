package fb.survival.gui.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment; // Dodajemy import dla zaklęć
import org.bukkit.inventory.ItemFlag;     // Dodajemy import dla flag przedmiotów
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List; // Używamy List zamiast ArrayList w deklaracji

public class Craft_Next {


    public static ItemStack getitem(){
        // Używamy strzałki w prawo (ARROW) jako ikony "Dalej"
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        // Nazwa z efektem pogrubienia i Twoimi kolorami
        meta.setDisplayName(HexAPI.hex("#0096fc§l» §b§lNastępny Crafting #0096fc§l«"));

        List<String> lore = new ArrayList<>(); // Używamy List

        lore.add("");
        // Wyraźna instrukcja
        lore.add(HexAPI.hex("§7Kliknij, aby zobaczyć §b§lkolejne receptury§7."));
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