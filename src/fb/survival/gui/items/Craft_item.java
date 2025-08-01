package fb.survival.gui.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment; // Dodajemy import dla zaklęć
import org.bukkit.inventory.ItemFlag;     // Dodajemy import dla flag przedmiotów
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List; // Używamy List zamiast ArrayList w deklaracji

public class Craft_item {


    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = item.getItemMeta();

        // Nazwa z efektem pogrubienia i Twoimi kolorami
        meta.setDisplayName(HexAPI.hex("#0096fc§l✨ §b§lSCRAFTUJ PRZEDMIOT #0096fc§l✨"));

        List<String> lore = new ArrayList<>(); // Używamy List

        lore.add("");
        lore.add(HexAPI.hex("§7Upewnij się, że posiadasz §b§lwszystkie wymagane"));
        lore.add(HexAPI.hex("§7przedmioty w swoim §b§lekwipunku§7."));
        lore.add("");
        lore.add(HexAPI.hex("§a§lKLIKNIJ, ABY TWORZYĆ!")); // Jasna instrukcja w zielonym kolorze

        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}