package fb.survival.data;

import fb.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerData {
    static ServerData instance = new ServerData();
    static Plugin p;
    static FileConfiguration data;
    public static File rfile;

    public ServerData() {
    }

    public static ServerData getInstance() {
        return instance;
    }
    public static void initialize(Main plugin) {
        if (instance == null) {
            instance = new ServerData();
        }
    }

    public void setup(Plugin p) {
        this.p = p; // Przypisz plugin do pola instancji

        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        File path = p.getDataFolder(); // Lepszy sposób na uzyskanie katalogu danych pluginu
        rfile = new File(path, "server.yml");

        if (!rfile.exists()) {
            try {
                path.mkdirs(); // Upewnij się, że katalogi istnieją
                rfile.createNewFile();
                // Jeśli plik nie istnieje, ustaw domyślne wartości
                saveData();
            } catch (IOException var4) {
                p.getLogger().severe("Nie udalo sie stworzyc pliku server.yml: " + var4.getMessage());
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
    private static void loaddefualt(){
        if(data.getString("options.nether") == null){
            data.set("options.nether", false);
            data.set("options.kits", false);
            saveData();
            System.out.println("Zapisano");
        }
    }
    public static void loadconfigdefault(){
        loaddefualt();
    }

    public static void saveData() {
        try {
            data.save(rfile);
        } catch (IOException var2) {
            p.getLogger().severe("Nie udalo sie zapisac pliku server.yml: " + var2.getMessage()); // Użyj loggera pluginu
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(rfile);
        saveData();
    }
}