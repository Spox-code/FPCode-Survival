package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BossZombieEGG {

    public static int CUSTOM_MODEL_DATA = 3207; // You can change this if needed

    public static ItemStack getItem(){
        // Set the material to ZOMBIE_SPAWN_EGG
        ItemStack item = new ItemStack(Material.CANDLE);
        ItemMeta meta = item.getItemMeta();

        // Set the custom model data for custom texture
        meta.setCustomModelData(CUSTOM_MODEL_DATA);

        // Set the display name for the Boss Zombie Egg
        meta.setDisplayName(HexAPI.hex("#0096fcJajo Zombi Boss'a Obłąkanego")); // Orange-red color for a powerful boss

        List<String> lore = new ArrayList<>();

        // Add lore to describe the egg and the boss
        lore.add(HexAPI.hex("§7Tajemnicze jajo, tętniące mroczną energią."));
        lore.add(HexAPI.hex("§7Po użyciu, uwolni §b§lPotężnego Obłąkanego Zombi§7."));
        lore.add(HexAPI.hex("§7Przygotuj się na walkę z #0096fcPrawdziwym Koszmarem§7!"));
        lore.add(""); // Empty line for better readability

        meta.setLore(lore);

        // Add a "magic glow" (unbreaking enchantment)
        meta.addEnchant(Enchantment.UNBREAKING, 1, false);
        // Hide the enchantment text
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        // Hide attributes to keep the item cleaner
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }
}