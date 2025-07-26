package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.BungeeAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.PlayerAPI;
import fb.survival.api.ServerAPI;
import fb.survival.gui.gui.*;
import fb.survival.items.*;
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
        ItemStack clickedItem = e.getCurrentItem(); // Pobieramy kliknięty przedmiot

        // Ważne: Sprawdź, czy kliknięty przedmiot nie jest nullem (np. kliknięcie poza ikoną)
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        // Używamy instanceof, aby sprawdzić, czy kliknięto w dolną część ekwipunku gracza
        // lub paska szybkiego dostępu, co często chcemy zablokować w GUI
        if (clickedInventory != null && !clickedInventory.equals(e.getInventory())) {
            // Jeśli kliknięto w ekwipunek gracza, a nie w GUI, nie anulujemy eventu domyślnie
            // Chyba że masz konkretny powód, aby blokować interakcje z ekwipunkiem gracza podczas otwartego GUI.
            // W przypadku menu typu GUI, często chcemy blokować wszystkie interakcje poza ikonami GUI.
            // Dla uproszczenia, będziemy anulować wszystkie kliknięcia w nasze GUI, jeśli nie jest to konkretny slot.
        }


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
                case 13:
                    p.getInventory().addItem(Zwoj.getItem());
                    break;
                case 14:
                    p.getInventory().addItem(Ekskalibur.getitem());
                    break;
                default:
                    // Jeśli kliknięto na szare tło lub inny niezdefiniowany slot w "Przedmioty" GUI
                    // To można po prostu odświeżyć GUI lub zignorować.
                    // AdminItemsGUI.OpenGUI(p); // Ta linia spowodowałaby otworzenie GUI ponownie
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
                    // KitsGUI.OpenGUI(p); // Usuwamy, aby uniknąć ponownego otwierania GUI
                    break;
            }
        }
        // ... (pozostałe sekcje Kit Gracz, Kit VIP, itd. bez zmian) ...
        else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Kit Gracz") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
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
                    // KIT_Player_Gui.OpenGUI(p); // Usuwamy
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
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    // KIT_VIP_Gui.OpenGUI(p); // Usuwamy
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
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    // KIT_VIP_Gui.OpenGUI(p); // Usuwamy
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
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    // KIT_VIP_Gui.OpenGUI(p); // Usuwamy
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
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    // KIT_VIP_Gui.OpenGUI(p); // Usuwamy
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
                    }
                    break;
                case 39:
                    KitsGUI.OpenGUI(p);
                    break;
                default:
                    // KIT_VIP_Gui.OpenGUI(p); // Usuwamy
                    break;
            }
        }else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Topka") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            // int clickedSlot = e.getSlot(); // Niewykorzystana zmienna
            // switch (clickedSlot){
            //     default:
            //         TopkaGUI.OpenGUI(p); // Usuwamy
            //         break;
            // }
            // Po prostu anulujemy i nie otwieramy ponownie GUI, jesli default
        }
        // ---------- SEKCJA PERKÓW ----------
        else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Perki") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true); // Anulujemy event, aby gracz nie mógł zabrać perka
            int clickedSlot = e.getSlot();
            int playerMoney = BanAPI.getPlayerStatMoney(p.getName()); // Pobieramy pieniądze gracza raz

            String perkName = null;
            int maxLevel = 5; // Domyślny maksymalny poziom dla perków
            int baseCost = 0; // Bazowy koszt dla perka
            String perkDisplayName = ""; // Nazwa perka do wyświetlenia w wiadomości

            switch (clickedSlot){
                case 11: // Perk_LootingChance
                    perkName = "looting_chance";
                    baseCost = 1750; // Koszt dla Perk_LootingChance
                    perkDisplayName = "Szansa na Dodatkowy Łup";
                    break;
                case 13: // Perk_Deaths
                    perkName = "deaths";
                    baseCost = 1500; // Koszt dla Perk_Deaths
                    perkDisplayName = "Zwiększona Kasa za Zabójstwo";
                    break;
                case 15: // Perk_FastMining
                    perkName = "fast_mining";
                    baseCost = 1000; // Koszt dla Perk_FastMining
                    perkDisplayName = "Szybkie Kopanie";
                    break;
                default:
                    // Jeśli kliknięto w tło lub nieznany slot w GUI perków, po prostu odświeżamy GUI
                    PerkiGUI.OpenGUI(p);
                    return; // Zakończ, aby uniknąć dalszej logiki ulepszania
            }

            // Wspólna logika ulepszania dla wszystkich perków
            int currentLevel = PlayerAPI.getPerk(p, perkName);
            int cost = (currentLevel + 1) * baseCost;

            if (currentLevel >= maxLevel) {
                p.sendTitle(HexAPI.hex("#0096fc§lPERK"), HexAPI.hex("§cJuż masz maksymalny poziom tego perka!"));
                p.closeInventory();
                return;
            }

            if (playerMoney >= cost) {
                BanAPI.setSurvivalMoney(p.getName(), playerMoney - cost);
                PlayerAPI.setPerk(p, perkName, currentLevel + 1);
                p.sendTitle(HexAPI.hex("#0096fc§lPERK"), HexAPI.hex("§fUlepszyłeś perk #0096fc" + perkDisplayName));
                p.closeInventory();
            }
        }
        // ... (pozostałe sekcje Craftingi, Crafting Przepustki Nether bez zmian) ...
        else if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Craftingi") && clickedInventory != null && clickedInventory.equals(e.getInventory())){
            e.setCancelled(true);
            int clickedSlot = e.getSlot();
            switch (clickedSlot){
                case 10:
                    Craft_NetherPass.OpenGUI(p);
                    break;
                default:
                    // CraftingiGUI.OpenGUI(p); // Usuwamy
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
                    // Craft_NetherPass.OpenGUI(p); // Usuwamy
                    break;
            }
        }
        // Domyślne zachowanie, jeśli żaden z tytułów nie pasuje (np. interakcja z własnym ekwipunkiem gracza, jeśli nie jest to menu)
        // Jeśli chcesz całkowicie zablokować interakcje w ekwipunku gracza, gdy otwarte jest GUI, musiałbyś to zaimplementować tutaj
        // poprzez sprawdzenie, czy kliknięto w Inventory typu PLAYER.
    }
}