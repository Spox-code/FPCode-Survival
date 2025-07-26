package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment; // Import dla zaklęć
import org.bukkit.inventory.ItemFlag; // Import dla flag przedmiotów
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List; // Zmieniono import na List

public class EmblematZycia {

    public static int iditem = 3203;

    public static ItemStack getitem(){
        // Materiał bardziej pasujący do "emblematu życia" - np. Heart of the Sea
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        // Nazwa w głównych kolorach, z ozdobnikiem
        meta.setDisplayName(HexAPI.hex("#0096fc§l❤ §b§lEmblemat Życia #0096fc§l❤"));

        List<String> lore = new ArrayList<>(); // Zmieniono ArrayList na List

        lore.add("");
        lore.add(HexAPI.hex(" §8» §7Gdy znajdujesz się na skraju śmierci,"));
        // Wyróżnienie kluczowej akcji w kolorze dominującym
        lore.add(HexAPI.hex(" §8» §bUżyj, aby natychmiast uleczyć swe życie!"));
        lore.add(HexAPI.hex(" §8» §7Daje drugą szansę w walce o przetrwanie.")); // Dodatkowy opis
        lore.add("");

        meta.setLore(lore);

        // Magiczny blask
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }
}