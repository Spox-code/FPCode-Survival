package fb.survival.api; // Możesz umieścić tę klasę w osobnym pakiecie, np. 'fb.survival.drop'

import fb.core.api.HexAPI; // Zakładam, że masz klasę HexAPI do kolorowania tekstu
import fb.survival.api.NPCAPI; // Musimy zaimportować NPCAPI, aby użyć jego metod

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DropManager implements Listener {

    private static JavaPlugin plugin;
    // Mapa przechowująca lokalizację skrzynki zrzutu i listę UUID jej hologramów (ArmorStandów)
    private static Map<Location, List<UUID>> activeDropHolograms = new HashMap<>();

    public DropManager(JavaPlugin mainPlugin) {
        plugin = mainPlugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("DropManager initialized and registered events.");
    }

    /**
     * Tworzy wieloliniowy hologram nad daną lokalizacją zrzutu.
     * Hologramy te są tymczasowe i nie są zapisywane w pliku konfiguracyjnym NPCAPI.
     *
     * @param loc Lokalizacja, nad którą ma być stworzony hologram (zwykle lokalizacja skrzynki).
     * @param lines Lista linii tekstu dla hologramu (automatycznie kolorowane przez HexAPI).
     * @param spacing Odległość między liniami hologramu.
     * @param initialYOffset Początkowe przesunięcie Y dla pierwszej linii hologramu.
     */
    public static void createDropHologram(Location loc, List<String> lines, double spacing, double initialYOffset) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded() || lines == null || lines.isEmpty()) {
            plugin.getLogger().warning("Attempted to create a null, unloaded, or empty drop hologram.");
            return;
        }

        // Usuń istniejący hologram, jeśli już jest dla tej lokalizacji
        removeDropHologram(loc);

        List<UUID> currentHologramUUIDs = new ArrayList<>();
        List<String> reversedLines = new ArrayList<>(lines);
        Collections.reverse(reversedLines); // Odwracamy, by ostatnia linia była na dole

        double currentY = initialYOffset + (reversedLines.size() - 1) * spacing;

        for (String line : reversedLines) {
            // Sprawdź, czy świat jest załadowany przed spawnem
            if (loc.getWorld() == null || !loc.isWorldLoaded()) {
                plugin.getLogger().warning("Cannot spawn hologram, world is not loaded for location: " + loc);
                return;
            }

            ArmorStand hologramLine = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, currentY, 0), EntityType.ARMOR_STAND);
            hologramLine.setCustomName(HexAPI.hex(line)); // Użyj HexAPI do kolorowania
            hologramLine.setCustomNameVisible(true);
            hologramLine.setInvisible(true);
            hologramLine.setGravity(false);
            hologramLine.setSmall(true);
            hologramLine.setInvulnerable(true);
            hologramLine.setPersistent(false); // Hologramy zrzutów NIE są trwałe - usuwają się po restarcie
            hologramLine.setMarker(true);

            currentHologramUUIDs.add(hologramLine.getUniqueId());
            currentY -= spacing;
        }
        activeDropHolograms.put(loc, currentHologramUUIDs);
        plugin.getLogger().info("Created temporary drop hologram at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + " with " + lines.size() + " lines.");
    }

    /**
     * Usuwa tymczasowy hologram zrzutu z danej lokalizacji.
     * Wykorzystuje metodę `NPCAPI.removeNearbyHolograms` do usunięcia encji.
     * @param loc Lokalizacja skrzynki, której hologram ma zostać usunięty.
     */
    public static void removeDropHologram(Location loc) {
        if (loc == null) return;

        List<UUID> uuidsToRemove = activeDropHolograms.remove(loc);
        if (uuidsToRemove != null) {
            // Używamy metody z NPCAPI do usunięcia hologramów
            // Metoda NPCAPI.removeNearbyHolograms działa na wszystkich ArmorStandach w promieniu,
            // które są śledzone przez NPCAPI.
            // Ponieważ nasze hologramy zrzutów NIE są dodawane do 'spawnedHolograms' w NPCAPI,
            // musimy usunąć je ręcznie, iterując po UUID.
            // Alternatywnie, jeśli NPCAPI.removeNearbyHolograms miałaby usuwać WSZYSTKIE ArmorStandy,
            // to mogłoby to usuwać też inne hologramy.
            // Najbezpieczniej jest iterować po UUIDs, które sami śledzimy.
            for (UUID uuid : uuidsToRemove) {
                Entity entity = Bukkit.getEntity(uuid);
                if (entity != null && entity instanceof ArmorStand) {
                    entity.remove();
                }
            }
            plugin.getLogger().info("Removed temporary drop hologram at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        }
    }

    /**
     * Usuwa wszystkie aktywne tymczasowe hologramy zrzutów.
     * Wywoływane np. podczas wyłączania pluginu.
     */
    public static void removeAllActiveDropHolograms() {
        plugin.getLogger().info("Removing all temporary drop holograms...");
        // Tworzymy kopię kluczy, aby uniknąć ConcurrentModificationException
        for (Location loc : new ArrayList<>(activeDropHolograms.keySet())) {
            removeDropHologram(loc);
        }
        plugin.getLogger().info("All temporary drop holograms removed.");
    }

    // --- Obsługa zdarzeń ---

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType() == Material.CHEST) { // Sprawdzamy, czy kliknięto skrzynkę
                Location chestLoc = clickedBlock.getLocation();

                // Sprawdzamy, czy ta skrzynka ma przypisany aktywny hologram zrzutu
                if (activeDropHolograms.containsKey(chestLoc)) {
                    removeDropHologram(chestLoc);
                    // Tutaj możesz dodać dodatkową logikę, np. otworzyć skrzynkę po usunięciu hologramu.
                    // event.setCancelled(true); // Opcjonalnie: Zablokuj domyślne otwarcie, jeśli chcesz własną logikę.
                }
            }
        }
    }
}