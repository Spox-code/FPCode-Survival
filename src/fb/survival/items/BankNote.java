package fb.survival.items;

import fb.core.api.HexAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BankNote {

    public static int iditem = 3204;

    public static ItemStack getitem(int amount){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(iditem);

        meta.setDisplayName(HexAPI.hex("#0096fc§lBanknot"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(HexAPI.hex("§fBanknot o wartosci #0096fc" + amount + "$"));
        lore.add("");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
