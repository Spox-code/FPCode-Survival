package fb.survival.gui.gui;

import fb.survival.api.ServerAPI;
import fb.survival.gui.items.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TopkaGUI {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 27, "§8§lTopka");

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

        i.setItem(12, TOP_Kills.getitem(p));
        i.setItem(14, TOP_Deaths.getitem(p));

        p.openInventory(i);
    }
    private static void setItem(ItemStack item, Inventory i, int start, int ilosc){
        int end = start+ilosc;
        for(int x = start; x<end;x++){
            i.setItem(x, item);
        }
    }
    public static String isKits(){
        if(ServerAPI.isKits()){
            return "Tak";
        }else{
            return "Nie";
        }
    }
}
