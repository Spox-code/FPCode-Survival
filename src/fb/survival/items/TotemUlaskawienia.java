package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment; // Import dla zaklęć
import org.bukkit.inventory.ItemFlag; // Import dla flag przedmiotów
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List; // Zmieniono import na List

public class TotemUlaskawienia {

    public static int iditem = 3201;

    public static ItemStack getitem(){
        // Bardziej ikoniczny materiał, może Totem of Undying, jeśli nie koliduje z domyślnym
        // Jeśli Totem of Undying koliduje, to wróć do PURPLE_BANNER lub użyj np. BEACON.
        ItemStack item = new ItemStack(Material.PURPLE_BANNER);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        // Nazwa z dominującym #0096fc i §b, z ozdobnikiem
        meta.setDisplayName(HexAPI.hex("#0096fc§l✨ §b§lTotem Ułaskawienia #0096fc§l✨"));

        List<String> lore = new ArrayList<>(); // Zmieniono ArrayList na List

        lore.add(HexAPI.hex(" §8» §7Boska ochrona w obliczu niebezpieczeństwa."));
        lore.add(HexAPI.hex(" §8» §bUratuje Twój ekwipunek od utraty!")); // Kluczowa cecha w kolorze §b
        lore.add("");
        lore.add(HexAPI.hex(" §8[ Przedmiot Eventowy ]")); // Etykieta eventu
        // Specyficzny event w kolorze dominującym
        lore.add(HexAPI.hex(" §bEvent: #0096fcWakacyjny 2025"));
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