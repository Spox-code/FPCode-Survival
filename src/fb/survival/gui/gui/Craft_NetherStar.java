package fb.survival.gui.gui;

import fb.survival.gui.items.*;
import fb.survival.items.NetherPrzepustka;
import fb.survival.items.Zwoj;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Craft_NetherStar {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 45, "§8§lCrafting Gwiazdy Netheru");

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

        ItemStack diamond = new ItemStack(Material.DIAMOND_BLOCK, 1);
        ItemStack zwoj = Zwoj.getItem();

        i.setItem(11, zwoj);
        i.setItem(12, zwoj);
        i.setItem(13, zwoj);
        i.setItem(20, zwoj);
        i.setItem(21, diamond);
        i.setItem(22, zwoj);
        i.setItem(29, zwoj);
        i.setItem(30, zwoj);
        i.setItem(31, zwoj);

        i.setItem(24, new ItemStack(Material.NETHER_STAR));

        i.setItem(39, Craft_Prev.getitem());
        i.setItem(40, Craft_item.getitem());
        i.setItem(41, Craft_Next.getitem());

        p.openInventory(i);
    }
    private static void setItem(ItemStack item, Inventory i, int start, int ilosc){
        int end = start+ilosc;
        for(int x = start; x<end;x++){
            i.setItem(x, item);
        }
    }
}
