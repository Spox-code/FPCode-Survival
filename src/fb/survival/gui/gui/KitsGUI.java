package fb.survival.gui.gui;

import fb.survival.api.ServerAPI;
import fb.survival.gui.items.*;
import fb.survival.items.TotemUlaskawienia;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitsGUI {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 27, "§8§lKity");

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

        i.setItem(10, Kit_Gracz.getitem());
        i.setItem(11, Kit_VIP.getitem());
        i.setItem(12, Kit_SVIP.getitem());
        i.setItem(14, Kit_MVIP.getitem());
        i.setItem(15, Kit_Elita.getitem());
        i.setItem(16, Kit_Legenda.getitem());

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
