package fb.survival.gui.items;

import fb.core.api.HexAPI; // Potrzebne do kolorowania tekstu
import fb.core.api.RanksAPI;
import fb.survival.api.PlayerAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List; // Zamiast ArrayList, uzywaj List dla lepszej praktyki

public class HomeItem {

    static RanksAPI ra;

    public HomeItem(RanksAPI ra){
        HomeItem.ra = ra;
    }

    public static ItemStack getitem(Player p, int number){
        ItemStack item;
        ItemMeta meta;
        List<String> lore = new ArrayList<>();

        // Sprawdź, czy gracz ma uprawnienia do tego domu (np. fb.home.1, fb.home.2 itd.)
        if(ra.hasPermission(p, "fb.home." + number)) {
            // Gracz MA uprawnienia
            if (PlayerAPI.hasHome(p, number)) {
                // Gracz MA już ustawiony ten dom
                item = new ItemStack(Material.LIME_BED); // Zielone łóżko symbolizujące ustawiony dom
                meta = item.getItemMeta();

                meta.setDisplayName(HexAPI.hex("§a§lDOM #" + number)); // Zielona, pogrubiona nazwa
                lore.add(HexAPI.hex("§7Status: §aUstawiony"));
                lore.add(" "); // Pusta linia dla lepszej czytelności
                lore.add(HexAPI.hex("§8[ §bKliknij SHIFT+PPM, aby usunac dom §8]")); // Jasnoniebieski
                lore.add(HexAPI.hex("§8[ §bKliknij PPM, aby sie teleportowac §8]")); // Jasnoniebieski

            }else{
                // Gracz NIE MA ustawionego tego domu, ale MA uprawnienia, aby go ustawić
                item = new ItemStack(Material.GRAY_BED); // Szare łóżko symbolizujące pusty slot na dom
                meta = item.getItemMeta();

                meta.setDisplayName(HexAPI.hex("#0096fc§lDOM #" + number)); // Żółta, pogrubiona nazwa
                lore.add(HexAPI.hex("§7Status: §cNie ustawiony"));
                lore.add(HexAPI.hex("§7Wolne sloty: §aTak"));
                lore.add(" ");
                lore.add(HexAPI.hex("§8[ §bKliknij PPM, aby ustawic tutaj dom §8]"));

            }
        }else{
            // Gracz NIE MA uprawnień na ten dom
            item = new ItemStack(Material.BARRIER); // Bariera symbolizująca brak dostępu
            meta = item.getItemMeta();

            meta.setDisplayName(HexAPI.hex("§c§lDOM #" + number)); // Czerwona, pogrubiona nazwa
            lore.add(HexAPI.hex("§7Status: §cNiedostępny"));
            lore.add(HexAPI.hex("§7Wymagane uprawnienia: §f" + "fb.home." + number)); // Pokaż wymagane uprawnienie
            lore.add(" ");
            lore.add(HexAPI.hex("§8[ §cBrak dostepu §8]"));
        }

        // Ustaw lore i ItemMeta dla wszystkich przypadków
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}