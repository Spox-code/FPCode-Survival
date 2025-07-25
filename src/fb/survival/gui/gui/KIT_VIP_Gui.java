package fb.survival.gui.gui;

import fb.survival.gui.items.BlueGlass;
import fb.survival.gui.items.GrayGlass;
import fb.survival.gui.items.Kit_Odbierz;
import fb.survival.gui.items.Kit_Return;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KIT_VIP_Gui {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 45, "§8§lKit VIP");

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

        i.setItem(10, new ItemStack(Material.IRON_SWORD));
        i.setItem(11, new ItemStack(Material.IRON_PICKAXE));
        i.setItem(12, new ItemStack(Material.IRON_AXE));
        i.setItem(13, new ItemStack(Material.IRON_SHOVEL));
        i.setItem(14, new ItemStack(Material.IRON_HELMET));
        i.setItem(15, new ItemStack(Material.IRON_CHESTPLATE));
        i.setItem(16, new ItemStack(Material.IRON_LEGGINGS));
        i.setItem(19, new ItemStack(Material.IRON_BOOTS));
        i.setItem(20, new ItemStack(Material.BREAD, 64));
        i.setItem(21, new ItemStack(Material.GOLDEN_APPLE, 4));


        i.setItem(41, Kit_Odbierz.getitem());
        i.setItem(39, Kit_Return.getitem());

        p.openInventory(i);
    }
    private static void setItem(ItemStack item, Inventory i, int start, int ilosc){
        int end = start+ilosc;
        for(int x = start; x<end;x++){
            i.setItem(x, item);
        }
    }
}
