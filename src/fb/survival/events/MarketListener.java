package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.survival.api.MarketManager; // Pozostawiamy fb.survival.api, bo tam jest MarketManager
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketListener implements Listener {

    private static JavaPlugin plugin;
    // private static Economy econ; // Jeśli używasz Vault

    public MarketListener(JavaPlugin mainPlugin /*, Economy economy */) {
        plugin = mainPlugin;
        // econ = economy; // Jeśli używasz Vault
        plugin.getLogger().info("MarketListener initialized.");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory == null || event.getView().getTitle() == null ||
                !event.getView().getTitle().startsWith(HexAPI.hex("#0096fc&lRynek Przedmiotów"))) {
            return;
        }

        event.setCancelled(true);

        if (clickedItem == null || clickedItem.getType().isAir()) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;

        String displayName = meta.hasDisplayName() ? meta.getDisplayName() : "";
        int currentPage = MarketManager.getPlayerMarketPage().getOrDefault(player.getUniqueId(), 0);

        // Zmieniono na §b, aby pasowało do MarketManager
        if (displayName.equals(HexAPI.hex("§bPoprzednia Strona"))) {
            if (currentPage > 0) {
                MarketManager.openMarketGUI(player, currentPage - 1);
            } else {
                player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cNie ma poprzedniej strony!"));
            }
            return;
        }

        // Zmieniono na §b, aby pasowało do MarketManager
        if (displayName.equals(HexAPI.hex("§bNastępna Strona"))) {
            // Sprawdź, czy istnieje następna strona zanim ją otworzysz
            List<MarketManager.MarketItem> allListings = new ArrayList<>();
            MarketManager.getAllListings().values().forEach(allListings::addAll);
            int totalListings = allListings.size();
            int maxPages = (int) Math.ceil((double) totalListings / MarketManager.ITEMS_PER_PAGE) - 1; // Maksymalny indeks strony (licząc od 0)

            if (currentPage < maxPages) {
                MarketManager.openMarketGUI(player, currentPage + 1);
            } else {
                player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cNie ma następnej strony!"));
            }
            return;
        }

        if (displayName.equals(HexAPI.hex("&cZamknij"))) {
            player.closeInventory();
            return;
        }

        NamespacedKey priceKey = MarketManager.getMarketPriceKey();
        NamespacedKey sellerKey = MarketManager.getMarketSellerKey();
        NamespacedKey listingIdKey = MarketManager.getMarketListingIdKey();

        if (meta.getPersistentDataContainer().has(priceKey, PersistentDataType.DOUBLE) &&
                meta.getPersistentDataContainer().has(sellerKey, PersistentDataType.STRING) &&
                meta.getPersistentDataContainer().has(listingIdKey, PersistentDataType.STRING)) {

            double price = meta.getPersistentDataContainer().get(priceKey, PersistentDataType.DOUBLE);
            UUID sellerUUID = UUID.fromString(meta.getPersistentDataContainer().get(sellerKey, PersistentDataType.STRING));
            UUID listingUUID = UUID.fromString(meta.getPersistentDataContainer().get(listingIdKey, PersistentDataType.STRING));

            if (player.getUniqueId().equals(sellerUUID)) {
                player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cNie możesz kupić własnego przedmiotu!"));
                return;
            }

            // --- Logika zakupu przedmiotu ---
            MarketManager.MarketItem purchasedItem = MarketManager.removeListing(listingUUID);
            if (purchasedItem != null) {
                int playerMoney = BanAPI.getPlayerStatMoney(player.getName());

                if (player.getInventory().firstEmpty() != -1 || player.getInventory().contains(purchasedItem.getItemStack().getType())) {
                    if (playerMoney >= price) {
                        int priceInt = (int) Math.round(price);

                        BanAPI.takeMoney(player.getName(), priceInt);
                        BanAPI.addMoney(Bukkit.getOfflinePlayer(sellerUUID).getName(), priceInt);

                        player.getInventory().addItem(purchasedItem.getItemStack());
                        player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fPomyślnie kupiłeś &b" + purchasedItem.getItemStack().getAmount() + "x " + purchasedItem.getItemStack().getType().name() + " &fza #0096fc" + String.format("%.2f", price) + "&a$. "));

                        if (Bukkit.getOfflinePlayer(sellerUUID).isOnline()) {
                            Bukkit.getOfflinePlayer(sellerUUID).getPlayer().sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §aTwój przedmiot &e" + purchasedItem.getItemStack().getAmount() + "x " + purchasedItem.getItemStack().getType().name() + " &azostał kupiony przez &b" + player.getName() + " &aza #0096fc" + String.format("%.2f", price) + "&a$."));
                        }
                        MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI po zakupie
                    } else {
                        player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cNie masz wystarczająco pieniędzy! Potrzeba: #0096fc" + String.format("%.2f", price) + "&c$."));
                        MarketManager.addListingWithoutRemovingFromPlayer(purchasedItem); // Przywróć na listę
                        MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI
                    }
                } else {
                    player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cNie masz miejsca w ekwipunku!"));
                    MarketManager.addListingWithoutRemovingFromPlayer(purchasedItem); // Przywróć na listę
                    MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI
                }
            } else {
                player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cTen przedmiot nie jest już dostępny."));
                MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle() != null && event.getView().getTitle().startsWith(HexAPI.hex("#0096fc&lRynek Przedmiotów"))) {
            MarketManager.getPlayerMarketPage().remove(player.getUniqueId());
        }
    }
}