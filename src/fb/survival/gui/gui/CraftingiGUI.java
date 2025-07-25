package fb.survival.gui.gui;

import fb.survival.api.ServerAPI;
import fb.survival.gui.items.BlueGlass;
import fb.survival.gui.items.GrayGlass;
import fb.survival.gui.items.TOP_Deaths;
import fb.survival.gui.items.TOP_Kills;
import fb.survival.items.NetherPrzepustka;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftingiGUI {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 27, "§8§lCraftingi");

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

        i.setItem(10, NetherPrzepustka.getNetherPass());

        p.openInventory(i);
    }
    private static void setItem(ItemStack item, Inventory i, int start, int ilosc){
        int end = start+ilosc;
        for(int x = start; x<end;x++){
            i.setItem(x, item);
        }
    }
}
