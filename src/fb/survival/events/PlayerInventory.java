package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.BungeeAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.PlayerAPI;
import fb.survival.api.ServerAPI;
import fb.survival.gui.gui.*;
import fb.survival.items.EmblematZycia;
import fb.survival.items.NetherPrzepustka;
import fb.survival.items.TotemUlaskawienia;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class PlayerInventory implements Listener {

    static RanksAPI ranksAPI;

    public PlayerInventory(RanksAPI ranksAPI){
        PlayerInventory.ranksAPI = ranksAPI;
    }

    @EventHandler
    public void PlayerInventory(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();


        if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Przedmioty") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 10:
                    p.getInventory().addItem(TotemUlaskawienia.getitem());
                    break;
                case 11:
                    p.getInventory().addItem(EmblematZycia.getitem());
                    break;
                case 12:
                    p.getInventory().addItem(NetherPrzepustka.getNetherPass());
                    break;
                default:
                    AdminItemsGUI.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kity") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 10:
                    KIT_Player_Gui.OpenGUI(p);
                    break;
                case 11:
                    KIT_VIP_Gui.OpenGUI(p);
                    break;
                case 12:
                    KIT_SVIP_Gui.OpenGUI(p);
                    break;
                case 14:
                    KIT_MVIP_Gui.OpenGUI(p);
                    break;
                case 15:
                    KIT_Elita_Gui.OpenGUI(p);
                    break;
                case 16:
                    KIT_Legenda_Gui.OpenGUI(p);
                    break;
                default:
                    KitsGUI.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit Gracz") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 41:
                    PlayerAPI.claimKit(p, "gracz");
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    KIT_Player_Gui.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit VIP") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 41:
                    if(ranksAPI.hasPermission(p, "fb.kits.vip")){
                        if(ServerAPI.isKits()) {
                            PlayerAPI.claimKit(p, "vip");
                        }else{
                            p.closeInventory();
                            p.sendTitle(HexAPI.hex("#0096fc§lFPCode"), HexAPI.hex("§fAktualnie kity sa #0096fcwylaczone"));
                        }
                    }else{
                        System.out.println("Brak uprawien");
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    KIT_VIP_Gui.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit SVIP") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 41:
                    if(ranksAPI.hasPermission(p, "fb.kits.svip")){
                        if(ServerAPI.isKits()) {
                            PlayerAPI.claimKit(p, "svip");
                        }else{
                            p.closeInventory();
                            p.sendTitle(HexAPI.hex("#0096fc§lFPCode"), HexAPI.hex("§fAktualnie kity sa #0096fcwylaczone"));
                        }
                    }else{
                        System.out.println("Brak uprawien");
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    KIT_VIP_Gui.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit MVIP") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 41:
                    if(ranksAPI.hasPermission(p, "fb.kits.mvip")){
                        if(ServerAPI.isKits()) {
                            PlayerAPI.claimKit(p, "mvip");
                        }else{
                            p.closeInventory();
                            p.sendTitle(HexAPI.hex("#0096fc§lFPCode"), HexAPI.hex("§fAktualnie kity sa #0096fcwylaczone"));
                        }
                    }else{
                        System.out.println("Brak uprawien");
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    KIT_VIP_Gui.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit Elita") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 41:
                    if(ranksAPI.hasPermission(p, "fb.kits.elita")){
                        if(ServerAPI.isKits()) {
                            PlayerAPI.claimKit(p, "elita");
                        }else{
                            p.closeInventory();
                            p.sendTitle(HexAPI.hex("#0096fc§lFPCode"), HexAPI.hex("§fAktualnie kity sa #0096fcwylaczone"));
                        }
                    }else{
                        System.out.println("Brak uprawien");
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    KIT_VIP_Gui.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit Legenda") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                case 41:
                    if(ranksAPI.hasPermission(p, "fb.kits.legenda")){
                        if(ServerAPI.isKits()) {
                            PlayerAPI.claimKit(p, "legenda");
                        }else{
                            p.closeInventory();
                            p.sendTitle(HexAPI.hex("#0096fc§lFPCode"), HexAPI.hex("§fAktualnie kity sa #0096fcwylaczone"));
                        }
                    }else{
                        System.out.println("Brak uprawien");
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    KIT_VIP_Gui.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Topka") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();

            switch (clickedSlot){
                default:
                    TopkaGUI.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Perki") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();
            int money = BanAPI.getPlayerStatMoney(p.getName());
            switch (clickedSlot){
                case 13:
                    int amount = PlayerAPI.getPerk(p, "deaths");
                    int cost = (amount+1)*1500;
                    if(money >= cost){
                        BanAPI.setSurvivalMoney(p.getName(), money-cost);
                        PlayerAPI.setPerk(p, "deaths", amount+1);
                        p.closeInventory();
                        p.sendTitle(HexAPI.hex("#0096fc§lPERK"), HexAPI.hex("§fUlepszyles perk #0096fcZwiekszona kasa"));
                    }
                    break;
                default:
                    PerkiGUI.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Craftingi") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();
            switch (clickedSlot){
                case 10:
                    Craft_NetherPass.OpenGUI(p);
                    break;
                default:
                    CraftingiGUI.OpenGUI(p);
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Crafting Przepustki Nether") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();
            switch (clickedSlot){
                case 40:
                    if(p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND_BLOCK), 1) && p.getInventory().containsAtLeast(new ItemStack(Material.GOLD_BLOCK), 1) && p.getInventory().containsAtLeast(new ItemStack(Material.NETHER_STAR), 1)){
                        p.getInventory().removeItem(new ItemStack(Material.DIAMOND_BLOCK, 1));
                        p.getInventory().removeItem(new ItemStack(Material.GOLD_BLOCK, 1));
                        p.getInventory().removeItem(new ItemStack(Material.NETHER_STAR));

                        p.getInventory().addItem(NetherPrzepustka.getNetherPass());

                        p.sendTitle(HexAPI.hex("#0096fc§lCRAFTING"), HexAPI.hex("§fStworzyles przedmiot #0096fcPrzepusta Netheru"));
                        p.closeInventory();
                    }
                    break;
                default:
                    Craft_NetherPass.OpenGUI(p);
                    break;
            }
        }else{
            return;
        }
    }
}
