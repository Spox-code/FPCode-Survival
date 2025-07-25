package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class NetherPrzepustka {

    public static int CUSTOM_MODEL_DATA = 3205;

    public static ItemStack getNetherPass(){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(CUSTOM_MODEL_DATA);
        // Nazwa przedmiotu w kolorze #0096fc i pogrubieniu
        meta.setDisplayName(HexAPI.hex("#0096fc§lNETHEROWA PRZEPUSTKA"));

        List<String> lore = new ArrayList<>();

        // Lore w szarym, z wyróżnieniem słowa "Netheru" w kolorze #0096fc
        lore.add(HexAPI.hex("§7Pozwala na podróż do #0096fcNetheru§7."));
        lore.add(""); // Pusta linia dla lepszej czytelności
        // Ostrzeżenie w kolorze #0096fc
        lore.add(HexAPI.hex("#0096fc♦ §lOSTRZEŻENIE:"));
        lore.add(HexAPI.hex("  #0096fcTylko dla odważnych poszukiwaczy! §l!"));
        lore.add("");

        meta.setLore(lore);

        // Dodaje "magiczny blask" (glow) do przedmiotu
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        // Ukrywa tekst zaklęcia, żeby przedmiot wyglądał czysto
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }
}