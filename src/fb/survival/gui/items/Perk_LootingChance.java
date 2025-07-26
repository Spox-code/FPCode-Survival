package fb.survival.gui.items;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.survival.api.PlayerAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Perk_LootingChance {

    // Unikalne CUSTOM_MODEL_DATA dla tego perka
    public static int CUSTOM_MODEL_DATA = 3208; // Wcześniej było 3208 dla HealthRegen. Sprawdz, czy nie ma kolizji.

    public static ItemStack getitem(Player p){
        // Materiał pasujący do łupów/szczęścia, np. EMERALD lub DIAMOND
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(CUSTOM_MODEL_DATA);
        meta.setDisplayName(HexAPI.hex("#0096fc§l♦ §b§lSzansa na Dodatkowy Łup #0096fc§l♦")); // Nazwa w kolorach motywu

        List<String> lore = new ArrayList<>();

        int currentLevel = PlayerAPI.getPerk(p, "looting_chance"); // Nazwa perka: looting_chance
        int maxLevel = 5; // Maksymalny poziom
        int cost = (currentLevel + 1) * 1750; // Przykładowy koszt, możesz dostosować

        // Bonus procentowy na dodatkowy łup (np. co poziom +2% szansy)
        int currentBonusPercentage = currentLevel * 2;
        int nextBonusPercentage = (currentLevel + 1) * 2;


        lore.add(""); // Pusta linia dla lepszej czytelności
        lore.add(HexAPI.hex("  §8» §7Aktualny Poziom: §b§l" + currentLevel + "/" + maxLevel + " ★"));
        lore.add(HexAPI.hex("  §8» §7Obecny Bonus: §b§l+" + currentBonusPercentage + "% §7szansy na dodatkowy łup"));
        lore.add("");

        lore.add(HexAPI.hex("#0096fc§l❱❱❱ §b§lKolejne Ulepszenia #0096fc§l❰❰❰"));
        lore.add("");

        for(int i = 1; i <= maxLevel; i++){
            int levelBonusPercentage = i * 2;
            if(i <= currentLevel){
                lore.add(HexAPI.hex("§b✔ §bPoziom " + i + ": #0096fc+" + levelBonusPercentage + "% szansy §7(Odblokowano)"));
            } else {
                lore.add(HexAPI.hex("§8✖ §7Poziom " + i + ": §c+" + levelBonusPercentage + "% szansy"));
            }
        }
        lore.add("");

        if (currentLevel < maxLevel) {
            lore.add(HexAPI.hex("  §8» §7Koszt następnego Ulepszenia: §b§l" + cost + "$"));
            lore.add(HexAPI.hex("  §8» §7Następny Bonus: §b§l+" + nextBonusPercentage + "% §7szansy na dodatkowy łup"));
            lore.add("");

            if(BanAPI.getPlayerStatMoney(p.getName()) >= cost){
                lore.add(HexAPI.hex("#0096fc§l✔ §bStać Cię na ulepszenie! #0096fc§l✔"));
                lore.add(HexAPI.hex("§8[ §bKliknij LPM, aby ulepszyć perk §8]"));
            } else {
                int lost = cost - BanAPI.getPlayerStatMoney(p.getName());
                lore.add(HexAPI.hex("§c✖ Brakuje Ci jeszcze: §b§l" + lost + "$"));
                lore.add(HexAPI.hex("§8[ §7Zdobądź więcej pieniędzy, aby ulepszyć §8]"));
            }
        } else {
            lore.add(HexAPI.hex("#0096fc§l✅ Maksymalny Poziom Osiągnięty! ✅"));
            lore.add(HexAPI.hex("§7Nie możesz już ulepszyć tego perka."));
        }
        lore.add("");

        meta.setLore(lore);
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }
}