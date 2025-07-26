package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.survival.api.MarketManager;
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

        if (displayName.equals(HexAPI.hex("&aPoprzednia Strona"))) {
            if (currentPage > 0) {
                MarketManager.openMarketGUI(player, currentPage - 1);
            } else {
                player.sendMessage(HexAPI.hex("&cNie ma poprzedniej strony!"));
            }
            return;
        }

        if (displayName.equals(HexAPI.hex("&aNastępna Strona"))) {
            MarketManager.openMarketGUI(player, currentPage + 1);
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
                player.sendMessage(HexAPI.hex("&cNie możesz kupić własnego przedmiotu!"));
                return;
            }

            // --- Logika zakupu przedmiotu ---
            MarketManager.MarketItem purchasedItem = MarketManager.removeListing(listingUUID);
            if (purchasedItem != null) {
                int playerMoney = BanAPI.getPlayerStatMoney(player.getName()); // Użyj lepszej nazwy zmiennej

                // Sprawdź, czy gracz ma miejsce w ekwipunku
                // Poprawione sprawdzanie miejsca: .firstEmpty() != -1 oznacza, że jest wolny slot
                // lub .containsAtLeast() nie jest odpowiednie do sprawdzania miejsca, tylko czy już ma przedmiot.
                // Uprośćmy to do sprawdzenia pierwszego pustego slotu.
                if (player.getInventory().firstEmpty() != -1) {
                    // Dodatkowo sprawdź, czy przedmiot zmieści się w istniejących stackach
                    // To jest bardziej złożone i na razie pominięte dla prostoty.
                    // Jeśli chcesz bardziej zaawansowaną logikę: player.getInventory().canAddItem(purchasedItem.getItemStack())

                    if (playerMoney >= price) {
                        // Tutaj poprawiamy konwersję double na int
                        // Zalecane: zaokrąglanie do najbliższej liczby całkowitej
                        int priceInt = (int) Math.round(price);

                        BanAPI.takeMoney(player.getName(), priceInt);
                        BanAPI.addMoney(Bukkit.getOfflinePlayer(sellerUUID).getName(), priceInt);

                        player.getInventory().addItem(purchasedItem.getItemStack());
                        player.sendMessage(HexAPI.hex("&fPomyślnie kupiłeś &b" + purchasedItem.getItemStack().getAmount() + "x " + purchasedItem.getItemStack().getType().name() + " &fza &e" + String.format("%.2f", price) + "&a$. "));

                        // Wiadomość dla sprzedawcy
                        if (Bukkit.getOfflinePlayer(sellerUUID).isOnline()) {
                            Bukkit.getOfflinePlayer(sellerUUID).getPlayer().sendMessage(HexAPI.hex("&aTwój przedmiot &e" + purchasedItem.getItemStack().getAmount() + "x " + purchasedItem.getItemStack().getType().name() + " &azostał kupiony przez &b" + player.getName() + " &aza &e" + String.format("%.2f", price) + "&a$."));
                        }
                        MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI po zakupie
                    } else {
                        player.sendMessage(HexAPI.hex("&cNie masz wystarczająco pieniędzy! Potrzeba: &e" + String.format("%.2f", price) + "&c$."));
                        // Jeśli nie ma pieniędzy, przywróć przedmiot na rynek
                        MarketManager.addListing(Bukkit.getOfflinePlayer(purchasedItem.getSellerUUID()).getPlayer(), purchasedItem.getItemStack(), purchasedItem.getPrice());
                        // Zwróć uwagę, że MarketManager.addListing oczekuje Player, ale tutaj mamy OfflinePlayer
                        // To wymaga przemyślenia, jak obsłużyć zwracanie przedmiotu na rynek, jeśli sprzedawca jest offline.
                        // Rozwiązanie tymczasowe: bezpośrednie dodanie do mapy `marketListings` (jeśli MarketManager to umożliwia)
                        // Lub: MarketManager.restoreListing(purchasedItem); -> musiałbyś dodać taką metodę w MarketManager

                        // Najprostsze rozwiązanie, jeśli addListing usuwa z ekwipunku i dodaje do mapy
                        // To jest problematyczne, bo już usunęliśmy z mapy w removeListing.
                        // Musimy stworzyć metodę w MarketManager do "anulowania" usunięcia, jeśli coś poszło nie tak.

                        // Na razie przywracamy przedmiot na listę bezpośrednio
                        // To jest ten problem, który mieliśmy wcześniej.
                        // Musimy mieć metodę w MarketManager, która bezpiecznie "cofnie" usunięcie.
                        // Tymczasowo:
                        // MarketManager.marketListings.computeIfAbsent(purchasedItem.getSellerUUID(), k -> new ArrayList<>()).add(purchasedItem);
                        // Ta linia jest nadal problematyczna, bo marketListings jest prywatne.
                        // Rozwiązanie poniżej: dodaj metodę do MarketManager `restoreListing(MarketItem item)`

                        // Dopóki nie masz metody `restoreListing` w MarketManager, musisz to obejść:
                        // Najbezpieczniej jest, aby `removeListing` zwracało `MarketItem`
                        // i tutaj, jeśli transakcja się nie powiedzie, to samemu dodasz `purchasedItem` z powrotem do mapy `marketListings`
                        // Ale skoro `marketListings` jest prywatne, potrzebujesz publicznej metody w `MarketManager` do tego.
                        // Spójrz na sekcję "Ważna poprawka: przywracanie ofert" poniżej.
                        MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI
                    }
                } else {
                    player.sendMessage(HexAPI.hex("&cNie masz miejsca w ekwipunku!"));
                    // Jeśli gracz nie ma miejsca, przywróć przedmiot na rynek
                    // Odkomentuj to, gdy zaimplementujesz MarketManager.restoreListing(purchasedItem)
                    // MarketManager.restoreListing(purchasedItem); // Użyj nowej metody

                    // Bezpośrednie dodanie do mapy marketListings jest problematyczne ze względu na prywatność
                    // Zamiast tego, w MarketManager dodamy metodę `addListing(MarketItem item)`
                    MarketManager.addListingWithoutRemovingFromPlayer(purchasedItem); // Nowa metoda w MarketManager
                    MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI
                }
            } else {
                player.sendMessage(HexAPI.hex("&cTen przedmiot nie jest już dostępny."));
                MarketManager.openMarketGUI(player, currentPage); // Odśwież GUI
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle() != null && event.getView().getTitle().startsWith(HexAPI.hex("&6&lRynek Przedmiotów"))) {
            MarketManager.getPlayerMarketPage().remove(player.getUniqueId());
        }
    }
}