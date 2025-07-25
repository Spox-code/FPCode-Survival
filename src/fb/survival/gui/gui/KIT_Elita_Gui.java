package fb.survival.gui.gui;

import fb.survival.gui.items.BlueGlass;
import fb.survival.gui.items.GrayGlass;
import fb.survival.gui.items.Kit_Odbierz;
import fb.survival.gui.items.Kit_Return;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KIT_Elita_Gui {

    public static void OpenGUI(Player p){
        Inventory i = Bukkit.createInventory(null, 45, "§8§lKit Elita");

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

        // Miecz
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        if (swordMeta != null) {
            swordMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            swordMeta.addEnchant(Enchantment.SHARPNESS, 2, true);  // Ostrość II (przykład)
            sword.setItemMeta(swordMeta);
        }

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();
        if (pickaxeMeta != null) {
            pickaxeMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            pickaxeMeta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Wydajność III (przykład)
            pickaxe.setItemMeta(pickaxeMeta);
        }

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta axeMeta = axe.getItemMeta();
        if (axeMeta != null) {
            axeMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            axeMeta.addEnchant(Enchantment.EFFICIENCY, 2, true); // Wydajność II (przykład)
            axe.setItemMeta(axeMeta);
        }

        ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta shovelMeta = shovel.getItemMeta();
        if (shovelMeta != null) {
            shovelMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            shovelMeta.addEnchant(Enchantment.EFFICIENCY, 2, true); // Wydajność II (przykład)
            shovel.setItemMeta(shovelMeta);
        }

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta helmetMeta = helmet.getItemMeta();
        if (helmetMeta != null) {
            helmetMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
            helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            helmet.setItemMeta(helmetMeta);
        }

        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta chestplateMeta = chestplate.getItemMeta();
        if (chestplateMeta != null) {
            chestplateMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
            chestplateMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            chestplate.setItemMeta(chestplateMeta);
        }

        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta leggingsMeta = leggings.getItemMeta();
        if (leggingsMeta != null) {
            leggingsMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
            leggingsMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            leggings.setItemMeta(leggingsMeta);
        }

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta bootsMeta = boots.getItemMeta();
        if (bootsMeta != null) {
            bootsMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
            bootsMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
            bootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 2, true); // Amortyzacja II (przykład)
            boots.setItemMeta(bootsMeta);
        }


        ItemStack bread = new ItemStack(Material.BREAD, 64);
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 48);

        i.setItem(10, sword);
        i.setItem(11, pickaxe);
        i.setItem(12, axe);
        i.setItem(13, shovel);
        i.setItem(14, helmet);
        i.setItem(15, chestplate);
        i.setItem(16, leggings);
        i.setItem(19, boots);
        i.setItem(20, bread);
        i.setItem(21, goldenApple);


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
