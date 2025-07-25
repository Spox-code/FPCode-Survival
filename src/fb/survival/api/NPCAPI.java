package fb.survival.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Mob; // Dodaj ten import
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPCAPI {

    private static JavaPlugin plugin;

    private static File dataFile;
    private static FileConfiguration dataConfig;

    private static List<UUID> spawnedNPCs = new ArrayList<>();
    private static List<UUID> spawnedHolograms = new ArrayList<>();

    // Zmieniamy na mapę Location -> TypNPC (String)
    private static Map<Location, String> savedNpcData = new HashMap<>(); // Zmieniono List<Location> na Map<Location, String>
    private static Map<Location, String> savedSingleLineHolograms = new HashMap<>();
    private static Map<Location, List<String>> savedMultiLineHolograms = new HashMap<>();

    /**
     * Inicjalizuje NPCAPI. Musi zostać wywołane raz, np. w metodzie onEnable() głównego pluginu.
     * @param mainPlugin Główna instancja pluginu.
     */
    public static void initialize(JavaPlugin mainPlugin) {
        plugin = mainPlugin;
        dataFile = new File(plugin.getDataFolder(), "npc_hologram_data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        plugin.getLogger().info("NPCAPI Data File Path: " + dataFile.getAbsolutePath());
        loadDataFromFile(); // Ładuj dane przy inicjalizacji
    }

    // --- Metody do bezpośredniego zarządzania danymi (z automatycznym zapisem) ---

    // Zmieniono sygnaturę metody, aby przyjmowała typ NPC
    public static void addNpcDataToData(Location loc, String npcType) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded() || npcType == null || npcType.isEmpty()) {
            plugin.getLogger().warning("Attempted to add null, unloaded, or empty NPC data.");
            return;
        }
        String oldType = savedNpcData.put(loc, npcType); // Zapisujemy lokalizację i typ
        if (oldType == null || !oldType.equals(npcType)) {
            plugin.getLogger().info("Added/Updated NPC location: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + " Type: " + npcType + ". Total NPCs: " + savedNpcData.size());
            saveDataToFile();
        }
    }

    // Usunięcie NPC-a z danych
    public static void removeNpcDataFromData(Location loc) {
        if (loc == null) return;
        if (savedNpcData.remove(loc) != null) {
            plugin.getLogger().info("Removed NPC data for: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ". Total NPCs: " + savedNpcData.size());
            saveDataToFile();
        }
    }

    public static void addSingleLineHologramDataToData(Location loc, String line) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded() || line == null || line.isEmpty()) {
            plugin.getLogger().warning("Attempted to add null, unloaded, or empty single line hologram data.");
            return;
        }
        String oldLine = savedSingleLineHolograms.put(loc, line); // Teraz kluczem jest Location
        if (oldLine == null || !oldLine.equals(line)) {
            plugin.getLogger().info("Added/Updated Single Line Hologram at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ". Line: " + line);
            saveDataToFile();
        }
    }

    public static void removeSingleLineHologramDataFromData(Location loc) {
        if (loc == null) return;
        if (savedSingleLineHolograms.remove(loc) != null) { // Teraz porównuje obiekty Location
            plugin.getLogger().info("Removed single line hologram data for: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            saveDataToFile();
        }
    }

    public static void addMultiLineHologramDataToData(Location loc, List<String> lines) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded() || lines == null || lines.isEmpty()) {
            plugin.getLogger().warning("Attempted to add null, unloaded, or empty multi-line hologram data.");
            return;
        }
        List<String> oldLines = savedMultiLineHolograms.put(loc, new ArrayList<>(lines)); // Teraz kluczem jest Location
        if (oldLines == null || !oldLines.equals(lines)) {
            plugin.getLogger().info("Added/Updated Multi-Line Hologram at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ". Total Holograms: " + savedMultiLineHolograms.size());
            saveDataToFile();
        }
    }

    public static void removeMultiLineHologramDataFromData(Location loc) {
        if (loc == null) return;
        if (savedMultiLineHolograms.remove(loc) != null) { // Teraz porównuje obiekty Location
            plugin.getLogger().info("Removed multi-line hologram data for: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            saveDataToFile();
        }
    }

    public static Map<Location, String> getSavedNpcData() {
        return new HashMap<>(savedNpcData); // Zwraca kopię mapy Location -> Typ NPC
    }

    public static Map<Location, String> getSavedSingleLineHolograms() {
        return new HashMap<>(savedSingleLineHolograms); // Zwraca kopię mapy
    }

    public static Map<Location, List<String>> getSavedMultiLineHolograms() {
        Map<Location, List<String>> copy = new HashMap<>();
        savedMultiLineHolograms.forEach((loc, lines) -> copy.put(loc, new ArrayList<>(lines)));
        return copy;
    }

    // --- Metody do spawnowania bytów (niezależnie od trwałości) ---

    public static void createHologram(Location loc, String name, double yOffset) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded()) return;

        ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, yOffset, 0), EntityType.ARMOR_STAND);
        hologram.setCustomName(name);
        hologram.setCustomNameVisible(true);
        hologram.setInvisible(true);
        hologram.setGravity(false);
        hologram.setSmall(true);
        hologram.setInvulnerable(true);
        hologram.setPersistent(true);
        hologram.setMarker(true);

        spawnedHolograms.add(hologram.getUniqueId());
        plugin.getLogger().info("Spawned single line hologram at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
    }

    public static void createMultiLineHologram(Location loc, List<String> lines, double spacing, double initialYOffset) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded() || lines == null || lines.isEmpty()) return;

        List<String> reversedLines = new ArrayList<>(lines);
        Collections.reverse(reversedLines);

        double currentY = initialYOffset + (reversedLines.size() - 1) * spacing;

        for (int i = reversedLines.size() - 1; i >= 0; i--) {
            String line = reversedLines.get(i);
            ArmorStand hologramLine = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, currentY, 0), EntityType.ARMOR_STAND);
            hologramLine.setCustomName(line);
            hologramLine.setCustomNameVisible(true);
            hologramLine.setInvisible(true);
            hologramLine.setGravity(false);
            hologramLine.setSmall(true);
            hologramLine.setInvulnerable(true);
            hologramLine.setPersistent(true);
            hologramLine.setMarker(true);

            spawnedHolograms.add(hologramLine.getUniqueId());
            plugin.getLogger().info("Spawned multi-line hologram part at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            currentY -= spacing;
        }
    }

    // Nowa metoda do spawnowania NPC-a na podstawie typu (stringa)
    public static void spawnNPC(Location loc, String npcType){
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded() || npcType == null || npcType.isEmpty()) return;

        EntityType type;
        try {
            type = EntityType.valueOf(npcType.toUpperCase()); // Próbujemy przekonwertować string na EntityType
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Unknown NPC type: " + npcType + ". Spawning a default WITCH.");
            type = EntityType.WITCH; // Domyślny typ, jeśli podany jest nieznany
        }

        Entity npc = loc.getWorld().spawnEntity(loc.clone().add(0, 0, 0), type);

        // Ustawienia wspólne dla wszystkich NPC
        npc.setCustomName("");
        npc.setCustomNameVisible(false);
        npc.setGravity(false); // Wyłączenie grawitacji (nie spada)
        npc.setInvulnerable(true); // Ustawienie niewrażliwości na obrażenia
        npc.setPersistent(true); // Zapewnia, że encja nie zostanie usunięta przez serwer

        // **** WAŻNA ZMIANA DLA PERSISTENTNOŚCI ****
        // Upewniamy się, że encja nie zostanie usunięta przez serwer, nawet jeśli chunk się wyładuje.
        // Jeśli byty mają być permanentne, muszą mieć ustawioną flagę `isPersistent()` na true.
        // Zazwyczaj `setPersistent(true)` wystarcza, ale jeśli nadal masz problemy,
        // możesz rozważyć utrzymywanie chunków wczytanych za pomocą `Chunk.addPluginChunkTicket(plugin)`.
        // Jednak `setPersistent(true)` jest preferowane dla bytów, które mają trwać.
        // Możemy również dodać logikę, która zapobiega usuwaniu tych bytów przez "garbage collector" Bukkit.
        // Dla starszych wersji Bukkit/Spigot `setPersistent(true)` może nie być wystarczające dla wszystkich typów bytów.
        // W nowszych wersjach jest to standardowe zachowanie.

        // Jeśli NPC jest mobem, wyłączamy jego AI i upewniamy się, że nie znika
        if (npc instanceof Mob) { // Użyj 'Mob' zamiast 'org.bukkit.entity.Mob' jeśli masz import org.bukkit.entity.Mob
            Mob mob = (Mob) npc;
            mob.setAI(false);
            // Dodatkowo, aby zapobiec despawnieniu mobów z powodu odległości:
            mob.setRemoveWhenFarAway(false); // <--- TO JEST KLUCZOWE
            plugin.getLogger().info("Disabled AI and set removeWhenFarAway(false) for NPC of type " + npcType + ".");
        }

        spawnedNPCs.add(npc.getUniqueId());
        plugin.getLogger().info("Spawned NPC of type " + npcType + " at: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
    }

    // Usunięto starą spawnNPCMagic, teraz używamy ogólnej spawnNPC

    public static void removeAllSpawnedNPCs() {
        for (UUID uuid : new ArrayList<>(spawnedNPCs)) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                entity.remove();
                plugin.getLogger().info("Removed spawned NPC: " + uuid);
            }
        }
        spawnedNPCs.clear();
    }

    public static void removeAllSpawnedHolograms() {
        for (UUID uuid : new ArrayList<>(spawnedHolograms)) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                entity.remove();
                plugin.getLogger().info("Removed spawned Hologram: " + uuid);
            }
        }
        spawnedHolograms.clear();
    }


    // --- Metody do zarządzania stanem NPC i hologramów (spawnowanie/despawnowanie + zapis/odczyt) ---

    // Zmieniono sygnaturę, aby przyjmowała typ NPC
    public static void saveAndSpawnNPC(Location loc, String npcType) {
        addNpcDataToData(loc, npcType); // Użyj nowej metody
        spawnNPC(loc, npcType); // Użyj ogólnej metody spawnNPC
    }

    // Zmieniono sygnaturę, aby używała removeNpcDataFromData
    public static void removeAndDespawnNPC(Location loc) {
        removeNpcDataFromData(loc);
        removeNearbyNPCs(loc, 0.5); // Usuń bliskie NPC-e z świata
    }

    public static void saveAndCreateHologram(Location loc, String name, double yOffset) {
        addSingleLineHologramDataToData(loc, name);
        removeNearbyHolograms(loc, 1.0);
        createHologram(loc, name, yOffset);
    }

    public static void removeAndDespawnHologram(Location loc) {
        removeSingleLineHologramDataFromData(loc);
        removeNearbyHolograms(loc, 1.0);
    }

    public static void saveAndCreateMultiLineHologram(Location loc, List<String> lines, double spacing, double initialYOffset) {
        addMultiLineHologramDataToData(loc, lines);
        removeMultiLineHologramAtLocation(loc, spacing, initialYOffset, lines.size());
        createMultiLineHologram(loc, lines, spacing, initialYOffset);
    }

    public static void removeAndDespawnMultiLineHologram(Location loc) {
        removeMultiLineHologramDataFromData(loc);
        // Zwiększamy promień i liczbę linii do usuwania, aby mieć pewność, że wszystkie części hologramu zostaną usunięte
        // Nawet jeśli domyślne wartości w metodzie tworzącej hologram są inne.
        // 0.5 to domyślne spacjowanie w niektórych tutorialach, 3.0 to rozsądny offset początkowy, a 20 to maksymalna liczba linii
        // którą można by chcieć usunąć w jednym hologramie.
        removeMultiLineHologramAtLocation(loc, 0.5, 3.0, 20);
    }

    public static void loadAndSpawnSavedNPCs() {
        plugin.getLogger().info("Spawning saved NPCs...");
        // Iterujemy po mapie, aby uzyskać zarówno lokalizację, jak i typ NPC
        for (Map.Entry<Location, String> entry : new HashMap<>(savedNpcData).entrySet()) {
            Location loc = entry.getKey();
            String npcType = entry.getValue();
            if (loc != null && loc.isWorldLoaded()) {
                spawnNPC(loc, npcType); // Używamy ogólnej metody spawnNPC
            } else if (loc != null) {
                plugin.getLogger().warning("Skipping NPC of type '" + npcType + "' at unloaded world: " + loc.getWorld().getName() + " - " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            } else {
                plugin.getLogger().warning("Skipping NPC: could not get location from map entry.");
            }
        }
        plugin.getLogger().info("Finished spawning saved NPCs. Total spawned: " + spawnedNPCs.size());
    }

    public static void loadAndSpawnSavedHolograms() {
        plugin.getLogger().info("Spawning saved Holograms...");
        double defaultSingleHologramYOffset = 2.0;
        for (Map.Entry<Location, String> entry : new HashMap<>(savedSingleLineHolograms).entrySet()) {
            Location loc = entry.getKey();
            if (loc != null && loc.isWorldLoaded()) {
                createHologram(loc, entry.getValue(), defaultSingleHologramYOffset);
            } else if (loc != null) {
                plugin.getLogger().warning("Skipping single line hologram at unloaded world: " + loc.getWorld().getName() + " - " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            } else {
                plugin.getLogger().warning("Skipping single line hologram: could not get location from map entry.");
            }
        }

        double defaultMultiLineSpacing = 0.25;
        double defaultMultiLineInitialYOffset = 1.0; // PAMIĘTAJ o tej wartości z HologramCMD!
        for (Map.Entry<Location, List<String>> entry : new HashMap<>(savedMultiLineHolograms).entrySet()) {
            Location loc = entry.getKey();
            if (loc != null && loc.isWorldLoaded()) {
                createMultiLineHologram(loc, entry.getValue(), defaultMultiLineSpacing, defaultMultiLineInitialYOffset);
            } else if (loc != null) {
                plugin.getLogger().warning("Skipping multi-line hologram at unloaded world: " + loc.getWorld().getName() + " - " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            } else {
                plugin.getLogger().warning("Skipping multi-line hologram: could not get location from map entry.");
            }
        }
        plugin.getLogger().info("Finished spawning saved Holograms. Total spawned: " + spawnedHolograms.size());
    }

    public static void reloadNpcAndHologramData() {
        plugin.getLogger().info("--- Starting full reload of NPC and Hologram data ---");
        removeAllSpawnedNPCs();
        removeAllSpawnedHolograms();
        plugin.getLogger().info("Step 1/4: Removed all currently spawned NPCs and Holograms from world.");

        savedNpcData.clear(); // Zmieniono na nową mapę
        savedSingleLineHolograms.clear();
        savedMultiLineHolograms.clear();
        plugin.getLogger().info("Step 2/4: Cleared in-memory NPC and Hologram data structures.");

        loadDataFromFile();
        plugin.getLogger().info("Step 3/4: Data reloaded from file.");

        loadAndSpawnSavedNPCs();
        loadAndSpawnSavedHolograms();
        plugin.getLogger().info("Step 4/4: NPCs and Holograms re-spawned based on reloaded data.");
        plugin.getLogger().info("--- Full reload complete ---");
    }

    // --- Metody do zarządzania plikami i serializacji/deserializacji ---

    public static void saveDataToFile() {
        plugin.getLogger().info("Attempting to save NPC and Hologram data...");
        plugin.getLogger().info("Current NPC count for save: " + savedNpcData.size()); // Zmieniono na nową mapę
        plugin.getLogger().info("Current Single Line Hologram count for save: " + savedSingleLineHolograms.size());
        plugin.getLogger().info("Current Multi-Line Hologram count for save: " + savedMultiLineHolograms.size());

        try {
            // Zapisz NPC-e
            ConfigurationSection npcSection = dataConfig.createSection("npcs");
            int npcIndex = 0; // Używamy indeksu, aby mieć unikalne klucze
            for (Map.Entry<Location, String> entry : savedNpcData.entrySet()) {
                Location loc = entry.getKey();
                String npcType = entry.getValue();
                // Sprawdzamy czy świat jest załadowany, zanim spróbujemy zapisać lokalizację
                if (loc != null && loc.getWorld() != null && loc.isWorldLoaded()) {
                    ConfigurationSection npcEntry = npcSection.createSection("npc_" + npcIndex++);
                    npcEntry.set("location", loc.serialize()); // Zapisz lokalizację jako mapę
                    npcEntry.set("type", npcType); // Zapisz typ NPC
                } else {
                    plugin.getLogger().warning("Skipping NPC location for saving (not valid/loaded): " + (loc != null ? (loc.getWorld() != null ? loc.getWorld().getName() + " - " : "") + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() : "null"));
                }
            }

            // Zapisz pojedyncze hologramy (bez zmian)
            ConfigurationSection singleHologramSection = dataConfig.createSection("single_holograms");
            int singleIndex = 0;
            for (Map.Entry<Location, String> entry : savedSingleLineHolograms.entrySet()) {
                Location loc = entry.getKey();
                if (loc != null && loc.getWorld() != null && loc.isWorldLoaded()) {
                    ConfigurationSection hologramEntry = singleHologramSection.createSection("hologram_" + singleIndex++);
                    hologramEntry.set("location", loc.serialize());
                    hologramEntry.set("text", entry.getValue());
                } else {
                    plugin.getLogger().warning("Skipping single line hologram data for saving (location not valid/loaded): " + (loc != null ? (loc.getWorld() != null ? loc.getWorld().getName() + " - " : "") + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() : "null"));
                }
            }

            // Zapisz wieloliniowe hologramy (bez zmian)
            ConfigurationSection multiHologramSection = dataConfig.createSection("multi_holograms");
            int multiIndex = 0;
            for (Map.Entry<Location, List<String>> entry : savedMultiLineHolograms.entrySet()) {
                Location loc = entry.getKey();
                if (loc != null && loc.getWorld() != null && loc.isWorldLoaded()) {
                    ConfigurationSection hologramEntry = multiHologramSection.createSection("hologram_" + multiIndex++);
                    hologramEntry.set("location", loc.serialize());
                    hologramEntry.set("lines", entry.getValue());
                } else {
                    plugin.getLogger().warning("Skipping multi-line hologram data for saving (location not valid/loaded): " + (loc != null ? (loc.getWorld() != null ? loc.getWorld().getName() + " - " : "") + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() : "null"));
                }
            }

            dataConfig.save(dataFile);
            plugin.getLogger().info("NPC and Hologram data saved successfully to: " + dataFile.getAbsolutePath());
            plugin.getLogger().info("File size after save: " + dataFile.length() + " bytes");
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save NPC and Hologram data to " + dataFile.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadDataFromFile() {
        if (!dataFile.exists()) {
            plugin.getLogger().info("NPC and Hologram data file does not exist, creating new one.");
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create NPC and Hologram data file: " + e.getMessage());
                return;
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        // Ładowanie NPC-ów (ZMIANY TUTAJ!)
        if (dataConfig.contains("npcs")) {
            ConfigurationSection npcSection = dataConfig.getConfigurationSection("npcs");
            if (npcSection != null) {
                savedNpcData.clear(); // Czyścimy nową mapę
                for (String key : npcSection.getKeys(false)) {
                    ConfigurationSection npcEntry = npcSection.getConfigurationSection(key);
                    if (npcEntry != null && npcEntry.contains("location") && npcEntry.contains("type")) {
                        ConfigurationSection locationSection = npcEntry.getConfigurationSection("location");
                        String npcType = npcEntry.getString("type");

                        if (locationSection != null && npcType != null) {
                            Map<String, Object> serializedLoc = (Map<String, Object>) locationSection.getValues(false);
                            Location loc = Location.deserialize(serializedLoc);
                            // Dodatkowe sprawdzenie, czy świat jest załadowany PO deserializacji
                            if (loc != null && loc.getWorld() != null && loc.isWorldLoaded()) {
                                savedNpcData.put(loc, npcType); // Zapisujemy typ NPC
                            } else if (loc != null && loc.getWorld() != null) {
                                plugin.getLogger().warning("Skipping NPC '" + key + "' (type: " + npcType + "): world '" + loc.getWorld().getName() + "' is not loaded.");
                            } else {
                                plugin.getLogger().warning("Skipping NPC '" + key + "': could not deserialize location or world is null.");
                            }
                        } else {
                            plugin.getLogger().warning("Skipping malformed NPC entry: " + key + " - missing location or type.");
                        }
                    } else {
                        plugin.getLogger().warning("Skipping malformed NPC entry: " + key);
                    }
                }
            }
        }

        // Ładowanie pojedynczych hologramów (bez zmian)
        if (dataConfig.contains("single_holograms")) {
            ConfigurationSection singleHologramSection = dataConfig.getConfigurationSection("single_holograms");
            if (singleHologramSection != null) {
                savedSingleLineHolograms.clear();
                for (String key : singleHologramSection.getKeys(false)) {
                    ConfigurationSection hologramEntry = singleHologramSection.getConfigurationSection(key);
                    if (hologramEntry != null && hologramEntry.contains("location") && hologramEntry.contains("text")) {
                        ConfigurationSection locationSection = hologramEntry.getConfigurationSection("location");
                        String line = hologramEntry.getString("text");
                        if (locationSection != null && line != null) {
                            Map<String, Object> serializedLoc = (Map<String, Object>) locationSection.getValues(false);
                            Location loc = Location.deserialize(serializedLoc);
                            if (loc != null && loc.getWorld() != null && loc.isWorldLoaded()) {
                                savedSingleLineHolograms.put(loc, line);
                            } else if (loc != null && loc.getWorld() != null) {
                                plugin.getLogger().warning("Skipping single line hologram '" + key + "': world '" + loc.getWorld().getName() + "' is not loaded.");
                            } else {
                                plugin.getLogger().warning("Skipping single line hologram '" + key + "': could not deserialize location or world is null.");
                            }
                        }
                    } else {
                        plugin.getLogger().warning("Skipping malformed single line hologram entry: " + key);
                    }
                }
            }
        }

        // Ładowanie wieloliniowych hologramów (bez zmian)
        if (dataConfig.contains("multi_holograms")) {
            ConfigurationSection multiHologramSection = dataConfig.getConfigurationSection("multi_holograms");
            if (multiHologramSection != null) {
                savedMultiLineHolograms.clear();
                for (String key : multiHologramSection.getKeys(false)) {
                    ConfigurationSection hologramEntry = multiHologramSection.getConfigurationSection(key);
                    if (hologramEntry != null && hologramEntry.contains("location") && hologramEntry.contains("lines")) {
                        ConfigurationSection locationSection = hologramEntry.getConfigurationSection("location");
                        List<String> lines = hologramEntry.getStringList("lines");
                        if (locationSection != null && lines != null) {
                            Map<String, Object> serializedLoc = (Map<String, Object>) locationSection.getValues(false);
                            Location loc = Location.deserialize(serializedLoc);
                            if (loc != null && loc.getWorld() != null && loc.isWorldLoaded()) {
                                savedMultiLineHolograms.put(loc, lines);
                            } else if (loc != null && loc.getWorld() != null) {
                                plugin.getLogger().warning("Skipping multi-line hologram '" + key + "': world '" + loc.getWorld().getName() + "' is not loaded.");
                            } else {
                                plugin.getLogger().warning("Skipping multi-line hologram '" + key + "': could not deserialize location or world is null.");
                            }
                        }
                    } else {
                        plugin.getLogger().warning("Skipping malformed multi-line hologram entry: " + key);
                    }
                }
            }
        }
        plugin.getLogger().info("NPC and Hologram data loaded successfully.");
    }

    // --- Usunięte metody serializeLocation i deserializeLocation (nie są już potrzebne) ---

    // --- Metody pomocnicze do usuwania bytów ze świata (używane wewnętrznie) ---

    public static void removeNearbyHolograms(Location loc, double radius){
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded()) return;

        World world = loc.getWorld();
        List<Entity> nearbyEntities = (List<Entity>) world.getNearbyEntities(loc, radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof ArmorStand) {
                ArmorStand armorStand = (ArmorStand) entity;
                if (spawnedHolograms.contains(armorStand.getUniqueId())) {
                    armorStand.remove();
                    spawnedHolograms.remove(armorStand.getUniqueId());
                    plugin.getLogger().info("Removed nearby tracked hologram: " + armorStand.getUniqueId());
                }
            }
        }
    }

    private static void removeMultiLineHologramAtLocation(Location loc, double spacing, double initialYOffset, int numberOfLines) {
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded()) return;

        World world = loc.getWorld();
        // Zwiększamy promień wyszukiwania, aby objąć wszystkie potencjalne linie hologramu
        double searchRadius = Math.max(initialYOffset, (numberOfLines * spacing)) + 1.5; // Dodatkowe 1.5 dla bezpieczeństwa
        Location searchCenter = loc.clone().add(0, initialYOffset + (numberOfLines * spacing) / 2, 0);

        List<Entity> nearbyEntities = (List<Entity>) world.getNearbyEntities(searchCenter, searchRadius, searchRadius, searchRadius);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof ArmorStand) {
                ArmorStand armorStand = (ArmorStand) entity;
                // Sprawdzamy, czy to nasz hologram i czy jest blisko zadanej lokalizacji
                if (spawnedHolograms.contains(armorStand.getUniqueId()) &&
                        armorStand.getLocation().distanceSquared(loc) < (searchRadius * searchRadius)) { // Użyj distanceSquared dla lepszej wydajności
                    armorStand.remove();
                    spawnedHolograms.remove(armorStand.getUniqueId());
                    plugin.getLogger().info("Removed multi-line hologram part at: " + armorStand.getLocation().getBlockX() + "," + armorStand.getLocation().getBlockY() + "," + armorStand.getLocation().getBlockZ());
                }
            }
        }
    }

    public static void removeNearbyNPCs(Location loc, double radius){
        if (loc == null || loc.getWorld() == null || !loc.isWorldLoaded()) return;

        World world = loc.getWorld();
        List<Entity> nearbyEntities = (List<Entity>) world.getNearbyEntities(loc, radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            // Upewnij się, że nie usuwamy graczy ani ArmorStandów (hologramów)
            // i że encja jest śledzonym przez nas NPC-em.
            if (!(entity instanceof Player) && !(entity instanceof ArmorStand) && spawnedNPCs.contains(entity.getUniqueId())) {
                entity.remove();
                spawnedNPCs.remove(entity.getUniqueId());
                plugin.getLogger().info("Removed nearby tracked NPC: " + entity.getUniqueId());
            }
        }
    }
}