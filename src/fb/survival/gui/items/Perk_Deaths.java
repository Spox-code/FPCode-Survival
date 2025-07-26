package fb.survival.gui.items;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.survival.api.PlayerAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment; // Dodajemy import dla zaklęć
import org.bukkit.inventory.ItemFlag; // Dodajemy import dla flag przedmiotów
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List; // Zmieniono import na List

public class Perk_Deaths {

    public static ItemStack getitem(Player p){
        // Zmieniono materiał na coś, co lepiej oddaje "perk" lub "bonus"
        // Może to być np. ENCHANTED_BOOK, ENDER_PEARL (jako "nagroda"), lub BLUE_DYE dla koloru
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD); // Możesz zmienić na np. Material.ENCHANTED_BOOK lub Material.DIAMOND
        ItemMeta meta = item.getItemMeta();

        // Ulepszona nazwa: wykorzystanie kolorów #0096fc i §b, dodanie ikonki gwiazdy dla ulepszeń
        meta.setDisplayName(HexAPI.hex("#0096fc§l★ §b§lZwiększona Kasa za Zabójstwo §0096fc§l★"));

        List<String> lore = new ArrayList<>(); // Zmieniono ArrayList na List

        int currentLevel = PlayerAPI.getPerk(p, "deaths");
        int maxLevel = 5; // Maksymalny poziom perka
        int cost = (currentLevel + 1) * 1500;
        int nextBonus = (currentLevel + 1) * 50;
        int currentBonus = currentLevel * 50;

        lore.add(""); // Pusta linia dla lepszej czytelności

        // Aktualny poziom i bonus, z użyciem kolorów motywu
        lore.add(HexAPI.hex("  §8» §7Aktualny Poziom: §b§l" + currentLevel + "/" + maxLevel + " ★"));
        lore.add(HexAPI.hex("  §8» §7Obecny Bonus: §b§l+" + currentBonus + "$ §7za zabójstwo"));
        lore.add("");

        // Wyświetlanie listy ulepszeń
        lore.add(HexAPI.hex("#0096fc§l❱❱❱ §b§lKolejne Ulepszenia #0096fc§l❰❰❰"));
        lore.add("");

        for(int i = 1; i <= maxLevel; i++){
            int levelBonus = i * 50;
            if(i <= currentLevel){
                // Ulepszone poziomy w kolorze aktywacji
                lore.add(HexAPI.hex("§b✔ §bPoziom " + i + ": #0096fc+" + levelBonus + "$ §7(Odblokowano)"));
            } else {
                // Poziomy do odblokowania w szarości, z czerwoną gwiazdką
                lore.add(HexAPI.hex("§8✖ §7Poziom " + i + ": §c+" + levelBonus + "$"));
            }
        }
        lore.add("");

        // Sekcja kosztu i statusu ulepszenia
        if (currentLevel < maxLevel) { // Jeśli nie osiągnięto maksymalnego poziomu
            lore.add(HexAPI.hex("  §8» §7Koszt następnego Ulepszenia: §b§l" + cost + "$"));
            lore.add(HexAPI.hex("  §8» §7Następny Bonus: §b§l+" + nextBonus + "$ §7za zabójstwo"));
            lore.add("");

            if(BanAPI.getPlayerStatMoney(p.getName()) >= cost){
                lore.add(HexAPI.hex("#0096fc§l✔ §bStać Cię na ulepszenie! #0096fc§l✔"));
                lore.add(HexAPI.hex("§8[ §bKliknij LPM, aby ulepszyć perk §8]")); // Jasna instrukcja
            } else {
                int lost = cost - BanAPI.getPlayerStatMoney(p.getName());
                lore.add(HexAPI.hex("§c✖ Brakuje Ci jeszcze: §b§l" + lost + "$"));
                lore.add(HexAPI.hex("§8[ §7Zdobądź więcej pieniędzy, aby ulepszyć §8]"));
            }
        } else { // Jeśli osiągnięto maksymalny poziom
            lore.add(HexAPI.hex("§0096fc§l✅ Maksymalny Poziom Osiągnięty! ✅"));
            lore.add(HexAPI.hex("§7Nie możesz już ulepszyć tego perka."));
        }
        lore.add("");

        meta.setLore(lore);

        // Dodajemy blask do perka (jeśli aktywny lub jeśli chcesz zawsze)
        // Możesz dodać warunek: if(currentLevel > 0) meta.addEnchant(...)
        meta.addEnchant(Enchantment.UNBREAKING, 1, false); // Dodajemy dowolne zaklęcie dla efektu blasku
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Ukrywamy opis zaklęcia
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // Ukrywamy domyślne atrybuty

        item.setItemMeta(meta);
        return item;
    }
}