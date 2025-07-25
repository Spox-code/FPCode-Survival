package fb.survival.data;

import fb.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {
    static PlayerData instance = new PlayerData();
    static Plugin p;
    static FileConfiguration data;
    public static File rfile;

    public PlayerData() {
    }

    public static PlayerData getInstance() {
        return instance;
    }
    public static void initialize(Main plugin) {
        if (instance == null) {
            instance = new PlayerData();
        }
    }

    public void setup(Plugin p) {
        this.p = p; // Przypisz plugin do pola instancji

        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        File path = p.getDataFolder(); // Lepszy sposób na uzyskanie katalogu danych pluginu
        rfile = new File(path, "playerdata.yml");

        if (!rfile.exists()) {
            try {
                path.mkdirs(); // Upewnij się, że katalogi istnieją
                rfile.createNewFile();
                // Jeśli plik nie istnieje, ustaw domyślne wartości
                saveData();
            } catch (IOException var4) {
                p.getLogger().severe("Nie udalo sie stworzyc pliku playerdata.yml: " + var4.getMessage());
            }
        } else {
            // Jeśli plik istnieje, wczytaj go i uzupełnij brakujące sekcje
            this.data = YamlConfiguration.loadConfiguration(rfile);
            saveData(); // Zapisz ewentualne nowe wartości
        }

        this.data = YamlConfiguration.loadConfiguration(rfile); // Ponowne wczytanie po ewentualnych zmianach
    }

    public FileConfiguration getData() {
        return this.data;
    }

    public static void saveData() {
        try {
            data.save(rfile);
        } catch (IOException var2) {
            p.getLogger().severe("Nie udalo sie zapisac pliku playerdata.yml: " + var2.getMessage()); // Użyj loggera pluginu
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(rfile);
        saveData();
    }
    public void setKitLastClaimTime(UUID playerUUID, String kitName, long timestamp) {
        data.set(playerUUID.toString() + ".kits." + kitName + ".lastClaim", timestamp);
        saveData();
    }

    /**
     * Pobiera czas ostatniego odbioru kitu dla gracza.
     * @param playerUUID UUID gracza.
     * @param kitName Nazwa kitu.
     * @return Czas w milisekundach, kiedy kit został ostatnio odebrany, lub 0L jeśli nigdy.
     */
    public long getKitLastClaimTime(UUID playerUUID, String kitName) {
        return data.getLong(playerUUID.toString() + ".kits." + kitName + ".lastClaim", 0L);
    }
}