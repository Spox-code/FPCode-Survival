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

public class Perk_FastMining {

    // Ustaw unikalne CUSTOM_MODEL_DATA dla tego perka
    public static int CUSTOM_MODEL_DATA = 3209;

    public static ItemStack getitem(Player p){
        // Materiał pasujący do kopania, np. KILOF (DIAMOND_PICKAXE) lub rudy
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(CUSTOM_MODEL_DATA);
        meta.setDisplayName(HexAPI.hex("#0096fc§l⛏ §b§lZwiększona Szybkość Kopania #0096fc§l⛏"));

        List<String> lore = new ArrayList<>();

        int currentLevel = PlayerAPI.getPerk(p, "fast_mining"); // Nazwa perka: fast_mining
        int maxLevel = 5; // Maksymalny poziom
        int cost = (currentLevel + 1) * 1000; // Przykładowy koszt, możesz dostosować

        // Bonus szybkości kopania (np. co poziom +10% szybkości)
        int currentBonusPercentage = currentLevel * 10;
        int nextBonusPercentage = (currentLevel + 1) * 10;

        lore.add("");
        lore.add(HexAPI.hex("  §8» §7Aktualny Poziom: §b§l" + currentLevel + "/" + maxLevel + " ★"));
        lore.add(HexAPI.hex("  §8» §7Obecny Bonus: §b§l+" + currentBonusPercentage + "% §7szybkości kopania"));
        lore.add("");

        lore.add(HexAPI.hex("#0096fc§l❱❱❱ §b§lKolejne Ulepszenia #0096fc§l❰❰❰"));
        lore.add("");

        for(int i = 1; i <= maxLevel; i++){
            int levelBonusPercentage = i * 10;
            if(i <= currentLevel){
                lore.add(HexAPI.hex("§b✔ §bPoziom " + i + ": #0096fc+" + levelBonusPercentage + "% szybkości §7(Odblokowano)"));
            } else {
                lore.add(HexAPI.hex("§8✖ §7Poziom " + i + ": §c+" + levelBonusPercentage + "% szybkości"));
            }
        }
        lore.add("");

        if (currentLevel < maxLevel) {
            lore.add(HexAPI.hex("  §8» §7Koszt następnego Ulepszenia: §b§l" + cost + "$"));
            lore.add(HexAPI.hex("  §8» §7Następny Bonus: §b§l+" + nextBonusPercentage + "% §7szybkości kopania"));
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