package fb.survival.api; // Zmieniono pakiet z fb.survival.api na fb.survival.market

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Arrays;
import java.util.stream.Collectors;

import fb.core.api.HexAPI;

public class MarketManager {

    private static JavaPlugin plugin;
    private static Map<UUID, List<MarketItem>> marketListings = new HashMap<>();

    private static Map<UUID, Integer> playerMarketPage = new HashMap<>();

    private static final int GUI_SIZE = 54;
    private static final int ITEMS_PER_PAGE = 45;

    private static NamespacedKey marketPriceKey;
    private static NamespacedKey marketSellerKey;
    private static NamespacedKey marketListingIdKey;

    private static File listingsFile;
    private static FileConfiguration listingsConfig;

    public MarketManager(JavaPlugin mainPlugin) {
        plugin = mainPlugin;
        marketPriceKey = new NamespacedKey(plugin, "market_price");
        marketSellerKey = new NamespacedKey(plugin, "market_seller");
        marketListingIdKey = new NamespacedKey(plugin, "market_listing_id");

        listingsFile = new File(plugin.getDataFolder(), "market_listings.yml");
        listingsConfig = YamlConfiguration.loadConfiguration(listingsFile);

        loadListingsFromFile();
        plugin.getLogger().info("MarketManager initialized and listings loaded.");
    }

    /**
     * Reprezentuje przedmiot wystawiony na rynku.
     */
    public static class MarketItem {
        private UUID sellerUUID;
        private ItemStack itemStack;
        private double price;
        private UUID listingUUID;

        public MarketItem(UUID sellerUUID, ItemStack itemStack, double price) {
            this.sellerUUID = sellerUUID;
            this.itemStack = itemStack;
            this.price = price;
            this.listingUUID = UUID.randomUUID();
        }

        public MarketItem(UUID sellerUUID, ItemStack itemStack, double price, UUID listingUUID) {
            this.sellerUUID = sellerUUID;
            this.itemStack = itemStack;
            this.price = price;
            this.listingUUID = listingUUID;
        }

        public UUID getSellerUUID() {
            return sellerUUID;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public double getPrice() {
            return price;
        }

        public UUID getListingUUID() {
            return listingUUID;
        }

        // Metoda pomocnicza do tworzenia wyświetlanej wersji przedmiotu
        public ItemStack getDisplayItem() {
            ItemStack displayItem = itemStack.clone();
            ItemMeta meta = displayItem.getItemMeta();
            if (meta == null) return displayItem;

            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            lore.add(" ");
            // Ulepszona kolorystyka dla ceny i sprzedawcy w lore
            lore.add(HexAPI.hex("§fCena: #0096fc" + String.format("%.2f", price) + "$")); // Kolor niebieski dla ceny
            String sellerName = Bukkit.getOfflinePlayer(sellerUUID).getName();
            lore.add(HexAPI.hex("§fSprzedawca: §b" + (sellerName != null ? sellerName : "Nieznany"))); // Jasnoniebieski dla nazwy sprzedawcy
            lore.add(" ");
            lore.add(HexAPI.hex("§ePPM, aby kupić!")); // Żółty dla instrukcji
            meta.setLore(lore);

            meta.getPersistentDataContainer().set(marketPriceKey, PersistentDataType.DOUBLE, price);
            meta.getPersistentDataContainer().set(marketSellerKey, PersistentDataType.STRING, sellerUUID.toString());
            meta.getPersistentDataContainer().set(marketListingIdKey, PersistentDataType.STRING, listingUUID.toString());

            displayItem.setItemMeta(meta);
            return displayItem;
        }
    }

    /**
     * Dodaje przedmiot do listy ofert na rynku, usuwając go z ekwipunku gracza.
     * @param player Gracz wystawiający przedmiot.
     * @param item Przedmiot do wystawienia.
     * @param price Cena przedmiotu.
     */
    public static void addListing(Player player, ItemStack item, double price) {
        if (!player.getInventory().containsAtLeast(item, item.getAmount())) {
            player.sendMessage(HexAPI.hex("§cNie masz wystarczającej ilości tego przedmiotu!")); // Czerwony
            return;
        }
        player.getInventory().removeItem(item);

        MarketItem marketItem = new MarketItem(player.getUniqueId(), item, price);
        marketListings.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(marketItem);
        player.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fPomyślnie wystawiłeś §b" + item.getAmount() + "x " + item.getType().name() + " §fna rynku za #0096fc" + String.format("%.2f", price) + "$.")); // Zielony, niebieski, żółty
        plugin.getLogger().info(player.getName() + " listed " + item.getAmount() + "x " + item.getType().name() + " for " + price + "$");
        saveListingsToFile();
    }

    /**
     * Dodaje przedmiot z powrotem na listę ofert, bez usuwania go z ekwipunku gracza.
     * Jest to używane, gdy transakcja zakupu się nie powiodła (np. brak pieniędzy/miejsca).
     * @param item Przedmiot MarketItem do przywrócenia.
     */
    public static void addListingWithoutRemovingFromPlayer(MarketItem item) {
        marketListings.computeIfAbsent(item.getSellerUUID(), k -> new ArrayList<>()).add(item);
        plugin.getLogger().info("Restored listing " + item.getListingUUID() + " to market due to failed transaction.");
        saveListingsToFile();
    }

    /**
     * Usuwa ofertę z rynku na podstawie jej unikalnego UUID.
     * @param listingUUID UUID oferty do usunięcia.
     * @return Zwraca usunięty obiekt MarketItem, jeśli znaleziono i usunięto, w przeciwnym razie null.
     */
    public static MarketItem removeListing(UUID listingUUID) {
        for (Map.Entry<UUID, List<MarketItem>> entry : marketListings.entrySet()) {
            List<MarketItem> listingsOfSeller = entry.getValue();
            MarketItem foundItem = null;

            var iterator = listingsOfSeller.iterator();
            while (iterator.hasNext()) {
                MarketItem item = iterator.next();
                if (item.getListingUUID().equals(listingUUID)) {
                    foundItem = item;
                    iterator.remove();
                    break;
                }
            }

            if (foundItem != null) {
                if (listingsOfSeller.isEmpty()) {
                    marketListings.remove(entry.getKey());
                }
                plugin.getLogger().info("Removed listing " + listingUUID + " from market.");
                saveListingsToFile();
                return foundItem;
            }
        }
        plugin.getLogger().warning("Attempted to remove non-existent listing: " + listingUUID);
        return null;
    }

    /**
     * Generuje i otwiera GUI rynku dla gracza.
     * @param player Gracz, dla którego ma być otwarte GUI.
     * @param page Numer strony do wyświetlenia (zaczynając od 0).
     */
    public static void openMarketGUI(Player player, int page) {
        // Tytuł GUI z kolorem #0096fc
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, HexAPI.hex("#0096fc&lRynek Przedmiotów §7(Strona " + (page + 1) + ")"));

        List<MarketItem> allListings = new ArrayList<>();
        marketListings.values().forEach(allListings::addAll);
        Collections.sort(allListings, (item1, item2) -> Double.compare(item1.getPrice(), item2.getPrice()));

        int startIndex = page * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, allListings.size());

        if (startIndex > 0 && startIndex >= allListings.size()) {
            player.sendMessage(HexAPI.hex("§cCóż, nie ma już więcej przedmiotów na tej stronie!")); // Czerwony
            openMarketGUI(player, page - 1);
            return;
        }

        List<MarketItem> listingsOnPage = allListings.subList(startIndex, endIndex);

        for (int i = 0; i < listingsOnPage.size(); i++) {
            gui.setItem(i, listingsOnPage.get(i).getDisplayItem());
        }

        // Przyciski nawigacyjne z ulepszoną kolorystyką
        ItemStack prevPageButton = createGuiItem(Material.ARROW, "§bPoprzednia Strona", "§7Kliknij, aby przejść do poprzedniej strony."); // Jasnoniebieski
        ItemStack nextPageButton = createGuiItem(Material.ARROW, "§bNastępna Strona", "§7Kliknij, aby przejść do następnej strony."); // Jasnoniebieski
        ItemStack closeButton = createGuiItem(Material.BARRIER, "§cZamknij", "§7Kliknij, aby zamknąć rynek."); // Czerwony

        gui.setItem(GUI_SIZE - 9, prevPageButton);
        gui.setItem(GUI_SIZE - 5, closeButton);
        gui.setItem(GUI_SIZE - 1, nextPageButton);

        player.openInventory(gui);
        playerMarketPage.put(player.getUniqueId(), page);
    }

    /**
     * Metoda pomocnicza do tworzenia przedmiotów GUI.
     */
    private static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setDisplayName(HexAPI.hex(name));
        meta.setLore(Arrays.stream(lore).map(HexAPI::hex).collect(Collectors.toList()));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Zapisuje wszystkie aktywne oferty rynku do pliku market_listings.yml.
     */
    public static void saveListingsToFile() {
        if (listingsFile == null || listingsConfig == null) {
            plugin.getLogger().severe("Listings file or config not initialized!");
            return;
        }

        listingsConfig.set("listings", null);
        int listingIndex = 0;

        for (Map.Entry<UUID, List<MarketItem>> sellerEntry : marketListings.entrySet()) {
            UUID sellerUUID = sellerEntry.getKey();
            List<MarketItem> items = sellerEntry.getValue();

            for (MarketItem item : items) {
                String path = "listings." + listingIndex++;
                listingsConfig.set(path + ".seller", sellerUUID.toString());
                listingsConfig.set(path + ".item", item.getItemStack());
                listingsConfig.set(path + ".price", item.getPrice());
                listingsConfig.set(path + ".uuid", item.getListingUUID().toString());
            }
        }

        try {
            listingsConfig.save(listingsFile);
            plugin.getLogger().info("Market listings saved to " + listingsFile.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save market listings to " + listingsFile.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ładuje oferty rynku z pliku market_listings.yml.
     */
    public static void loadListingsFromFile() {
        if (!listingsFile.exists()) {
            plugin.getLogger().info("Market listings file not found, creating new one.");
            plugin.getDataFolder().mkdirs();
            try {
                listingsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create market_listings.yml: " + e.getMessage());
                return;
            }
        }

        listingsConfig = YamlConfiguration.loadConfiguration(listingsFile);

        if (listingsConfig.contains("listings")) {
            marketListings.clear();

            for (String key : listingsConfig.getConfigurationSection("listings").getKeys(false)) {
                try {
                    UUID sellerUUID = UUID.fromString(listingsConfig.getString("listings." + key + ".seller"));
                    ItemStack itemStack = listingsConfig.getItemStack("listings." + key + ".item");
                    double price = listingsConfig.getDouble("listings." + key + ".price");
                    UUID listingUUID = UUID.fromString(listingsConfig.getString("listings." + key + ".uuid"));

                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        MarketItem marketItem = new MarketItem(sellerUUID, itemStack, price, listingUUID);
                        marketListings.computeIfAbsent(sellerUUID, k -> new ArrayList<>()).add(marketItem);
                    } else {
                        plugin.getLogger().warning("Skipping invalid market listing (null or AIR item): " + key);
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to load market listing '" + key + "': " + e.getMessage());
                    e.printStackTrace();
                }
            }
            plugin.getLogger().info("Loaded " + marketListings.values().stream().mapToLong(List::size).sum() + " market listings from file.");
        } else {
            plugin.getLogger().info("No existing market listings found in file.");
        }
    }

    // --- Gettery dla NamespacedKey (potrzebne do listenera) ---
    public static NamespacedKey getMarketPriceKey() {
        return marketPriceKey;
    }

    public static NamespacedKey getMarketSellerKey() {
        return marketSellerKey;
    }

    public static NamespacedKey getMarketListingIdKey() {
        return marketListingIdKey;
    }

    public static Map<UUID, Integer> getPlayerMarketPage() {
        return playerMarketPage;
    }
}