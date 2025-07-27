package fb.survival.gui.gui;

import fb.survival.gui.items.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HomeGUI {

    public static Map<Player, Boolean> isteleport = new HashMap<>();
    public static Map<Player, Integer> teleporttime = new HashMap<>();
    public static Map<Player, Integer> teleporthome = new HashMap<>();

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 27, "§8§lDomy");

        for(int x = 0; x<27;x++){
            i.setItem(x, GrayGlass.getitem());
        }
        setItem(new ItemStack(Material.AIR), i, 10, 7);
        i.setItem(1, BlueGlass.getitem());
        i.setItem(7, BlueGlass.getitem());
        i.setItem(9, BlueGlass.getitem());
        i.setItem(17, BlueGlass.getitem());
        i.setItem(19, BlueGlass.getitem());
        i.setItem(25, BlueGlass.getitem());

        i.setItem(11, HomeItem.getitem(p, 1));
        i.setItem(13, HomeItem.getitem(p, 2));
        i.setItem(15, HomeItem.getitem(p, 3));

        p.openInventory(i);
    }
    private static void setItem(ItemStack item, Inventory i, int start, int ilosc){
        int end = start+ilosc;
        for(int x = start; x<end;x++){
            i.setItem(x, item);
        }
    }
}
