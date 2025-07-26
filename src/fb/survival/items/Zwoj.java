package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Zwoj {

    public static int CUSTOM_MODEL_DATA = 3206;

    public static ItemStack getItem(){
        // Zmieniamy typ materiału na PAPER, co lepiej pasuje do "zwoju"
        ItemStack item = new ItemStack(Material.MOJANG_BANNER_PATTERN);
        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(CUSTOM_MODEL_DATA);
        meta.setDisplayName(HexAPI.hex("#0096fc§lMityczny Zwój Rzemieślniczy")); // Złoty kolor

        List<String> lore = new ArrayList<>();

        lore.add(HexAPI.hex("§7Starożytny pergamin, przesiąknięty magią."));
        lore.add(HexAPI.hex("§7Niezbędny do tworzenia §bunikalnych przedmiotów§7."));
        lore.add("");

        meta.setLore(lore);

        // Dodaje "magiczny blask" (glow) do przedmiotu
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        // Ukrywa tekst zaklęcia
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        // Ukrywa atrybuty, aby przedmiot wyglądał czyściej, jeśli jakieś byłyby dodane domyślnie
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }
}