package fb.survival.gui.gui;

import fb.survival.gui.items.BlueGlass;
import fb.survival.gui.items.GrayGlass;
import fb.survival.items.EmblematZycia;
import fb.survival.items.NetherPrzepustka;
import fb.survival.items.TotemUlaskawienia;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdminItemsGUI {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 45, "§8§lPrzedmioty");

        for(int x = 0; x<45;x++){
            i.setItem(x, GrayGlass.getitem());
        }
        setItem(new ItemStack(Material.AIR), i, 10, 7);
        setItem(new ItemStack(Material.AIR), i, 19, 7);
        setItem(new ItemStack(Material.AIR), i, 28, 7);
        i.setItem(1, BlueGlass.getitem());
        i.setItem(7, BlueGlass.getitem());
        i.setItem(9, BlueGlass.getitem());
        i.setItem(17, BlueGlass.getitem());
        i.setItem(27, BlueGlass.getitem());
        i.setItem(35, BlueGlass.getitem());
        i.setItem(37, BlueGlass.getitem());
        i.setItem(43, BlueGlass.getitem());
        i.setItem(10, TotemUlaskawienia.getitem());
        i.setItem(11, EmblematZycia.getitem());
        i.setItem(12, NetherPrzepustka.getNetherPass());

        p.openInventory(i);
    }
    private static void setItem(ItemStack item, Inventory i, int start, int ilosc){
        int end = start+ilosc;
        for(int x = start; x<end;x++){
            i.setItem(x, item);
        }
    }
}
