package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment; // Import dla zaklęć
import org.bukkit.inventory.ItemFlag; // Import dla flag przedmiotów
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List; // Zmieniono import na List

public class Ekskalibur {

    public static int iditem = 3202;

    public static ItemStack getitem(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        // Nazwa z dominującym #0096fc i §b, dodany ozdobny znak
        meta.setDisplayName(HexAPI.hex("#0096fc§l❖ §b§lEkskalibur #0096fc§l❖"));

        List<String> lore = new ArrayList<>(); // Zmieniono ArrayList na List

        lore.add(HexAPI.hex(" §8» §7Legendarny miecz, wykuty w otchłani."));
        lore.add(HexAPI.hex(" §8» §bNiezwykła moc, nieokiełznana siła.")); // Opis w kolorze §b
        lore.add("");
        lore.add(HexAPI.hex(" §8[ Przedmiot Eventowy ]")); // Etykieta eventu
        // Specyficzny event w kolorze dominującym
        lore.add(HexAPI.hex(" §bEvent: #0096fcFerie 2025"));
        lore.add("");

        meta.setLore(lore);

        // Dodaje "magiczny blask"
        meta.addEnchant(Enchantment.UNBREAKING, 1, false); // Zaklęcie dla efektu
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Ukrywa zaklęcia
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // Ukrywa domyślne atrybuty (np. +Damage)

        item.setItemMeta(meta);
        return item;
    }
}