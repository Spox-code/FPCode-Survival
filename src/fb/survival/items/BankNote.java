package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List; // Zmieniono import na List

public class BankNote {

    public static int iditem = 3204;

    public static ItemStack getitem(int amount){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        // Ulepszona nazwa: jaśniejsza, bardziej czytelna, z efektem gradientu/wyróżnienia
        meta.setDisplayName(HexAPI.hex("#0096fc§lBanknot §b§lPremium"));

        List<String> lore = new ArrayList<>(); // Zmieniono ArrayList na List

        lore.add("");
        // Wyraźniejsza informacja o wartości, z kolorem dominującym
        lore.add(HexAPI.hex("§7Banknot o wartości: §b§l" + amount + "$"));
        lore.add(HexAPI.hex("§7Idealny do szybkich transakcji.")); // Dodatkowy opis
        lore.add("");
        // Mały ozdobny element
        lore.add(HexAPI.hex("§8[ Kliknij PPM, aby zrealizować ]"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}