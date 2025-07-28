package fb.survival;

import fb.core.api.BanAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.*; // Pozostałe API, które masz
import fb.survival.cmds.*;
import fb.survival.data.PlayerData; // Nadal zakłada, że PlayerData jest potrzebne
import fb.survival.data.ServerData; // Nadal zakłada, że ServerData jest potrzebne do innych rzeczy
import fb.survival.events.*;
import fb.survival.gui.gui.AdminItemsGUI;
import fb.survival.gui.items.HomeItem;
import fb.survival.items.NetherPrzepustka;
import fb.survival.items.Zwoj;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main mainInstance;
    private RanksAPI ranksApi;
    // Skoro NPCAPI teraz zarządza swoimi danymi, nie potrzebujesz już tych pól dla nich.
    // Jeśli ServerData i PlayerData są używane do innych celów, możesz je zostawić.
    // statyczne pola 'sd' i 'pd' powinny być używane tylko jeśli jest jeden konkretny przypadek ich uzycia
    // Inaczej, lepiej używać instancji ServerData.getInstance() i PlayerData.getInstance()
    // lub przekazywać je jako parametry do innych klas.
    // W tej edycji pozostawię je, ale pamiętaj o tej uwadze.
    static ServerData sd;
    static PlayerData pd;


    // Konstruktor powinien inicjalizować pola, ale pamiętaj o tym,
    // że getInstance() może zależeć od poprawnej inicjalizacji pluginu.
    // Najbezpieczniej jest inicjalizować instancje w onEnable().
    public Main(){
        // Te inicjalizacje są zazwyczaj bezpieczniejsze w onEnable() po 'mainInstance = this;'
        // lub w metodach setup() jeśli ServerData/PlayerData też jest JavaPlugin.
        // Jeśli getInstance() po prostu zwraca nową instancję, to jest ok.
        // Jeśli Singleton polega na Bukkit API, to lepiej robić to w onEnable().
        // Na potrzeby tej edycji zostawiam tak jak jest, zakładając, że działają.
        pd = PlayerData.getInstance();
        sd = ServerData.getInstance();
    }


    @Override
    public void onEnable() {
        mainInstance = this; // Ustawienie instancji głównego pluginu

        // Upewnij się, że foldery danych istnieją
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Setup dla PlayerData i ServerData (jeśli nadal ich używasz do innych celów)
        pd.setup(this);
        sd.setup(this);
        sd.loadconfigdefault();

        Plugin essentialsPlugin = Bukkit.getPluginManager().getPlugin("FPCode-Essentials");

        if (essentialsPlugin == null || !(essentialsPlugin instanceof fb.core.Main)) {
            getLogger().log(Level.SEVERE, "FPCode-Essentials nie został znaleziony lub nie jest prawidłowo włączony!");
            getLogger().log(Level.SEVERE, "Upewnij się, że FPCode-Essentials jest w folderze plugins i w plugin.yml Survival jest 'depend: [FPCode-Essentials]'.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        fb.core.Main essentialsMain = (fb.core.Main) essentialsPlugin;
        this.ranksApi = essentialsMain.getRanksAPI();

        if (this.ranksApi == null) {
            getLogger().log(Level.SEVERE, "Nie udało się pobrać RanksAPI z FPCode-Essentials! RanksAPI może nie być prawidłowo zainicjalizowane w Essentials.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Pomyślnie pobrano RanksAPI z FPCode-Essentials.");

        // Inicjalizacja Twoich API (jeśli nie mają zależności od innych rzeczy)
        new ServerAPI();
        new PlayerAPI();
        new Systems(this);
        Systems.start();
        new WorldAPI();
        WorldAPI.loadWorlds();
        registerNetherPassRecipe();
        registerNetherStarRecipe();
        new DropManager(this);
        new MarketManager(this);
        new HomeItem(ranksApi);
        new BossAPI();

        // --- Zmiany dla NPCAPI ---
        // Inicjalizuj NPCAPI, przekazując instancję głównego pluginu
        NPCAPI.initialize(this);
        // Po zainicjalizowaniu, załaduj i zespawnuj zapisane NPC-e i hologramy
        NPCAPI.loadAndSpawnSavedNPCs();
        NPCAPI.loadAndSpawnSavedHolograms();
        // --- Koniec zmian dla NPCAPI ---

        // Rejestracja komend
        getCommand("aitems").setExecutor(new AdminItems(ranksApi));
        getCommand("eco").setExecutor(new ECO(ranksApi));
        getCommand("kity").setExecutor(new KitsCMD());
        getCommand("topka").setExecutor(new TopkaCMD());
        getCommand("settings").setExecutor(new Settings(ranksApi));
        getCommand("vanish").setExecutor(new Vanish(ranksApi, this));
        getCommand("world").setExecutor(new WorldCMD(ranksApi));
        getCommand("hologram").setExecutor(new HologramCMD(ranksApi));
        getCommand("setspawn").setExecutor(new SetSpawn(ranksApi));
        getCommand("spawn").setExecutor(new Spawn(ranksApi));
        getCommand("npc").setExecutor(new NPCCMD(ranksApi));
        getCommand("withdraw").setExecutor(new Withdraw(ranksApi));
        getCommand("npcapireload").setExecutor(new NPCAPIReloadCMD(ranksApi));
        getCommand("craftingi").setExecutor(new Craftingi());
        getCommand("zrzut").setExecutor(new Zrzut(ranksApi));
        getCommand("rynek").setExecutor(new MarketCommand(ranksApi));
        getCommand("sprawdzanie").setExecutor(new Sprawdzanie(ranksApi));
        getCommand("misje").setExecutor(new MisjeCMD());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("boss").setExecutor(new Boss(ranksApi));
        getCommand("hat").setExecutor(new HatCommand());

        // Rejestracja eventów
        getServer().getPluginManager().registerEvents(new PlayerInventory(ranksApi), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(ranksApi, this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this, ranksApi), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(ranksApi), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(ranksApi), this);
        getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        getServer().getPluginManager().registerEvents(new PlayerClickPlayer(ranksApi), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerSendCommand(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new NetherJoin(), this);
        getServer().getPluginManager().registerEvents(new MarketListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(ranksApi), this);
        getServer().getPluginManager().registerEvents(new ChangeFood(), this);
        getServer().getPluginManager().registerEvents(new BossDeath(), this);
        //getServer().getPluginManager().registerEvents(new SingChange(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        /*
            TODO
            Sklep
            Misje
            Kowal
            Eventy
            Skrzynki
            Custom Enchanty
            /discord
            Auto MSG
            /pay <gracz> <kwota>
            Nadanie uprawien
            /Rangi
            /Pomoc
            Perki zrobic
        */

        // Zarejestruj rozszerzenie PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SurvivalExpansion(this, this.ranksApi).register();
            getLogger().info("Zarejestrowano rozszerzenie PlaceholderAPI dla FPCode-Survival.");
        } else {
            getLogger().warning("PlaceholderAPI nie znaleziono! Niektóre funkcje mogą być niedostępne.");
        }

        getLogger().info("FPCode-Survival został włączony!");
    }

    public static double getMoney(String playername){
        return BanAPI.getPlayerStatMoney(playername);
    }

    @Override
    public void onDisable() {
        // --- Zmiany dla NPCAPI ---
        // Usuń wszystkie zespawnione NPC-e i hologramy
        NPCAPI.removeAllSpawnedNPCs();
        NPCAPI.removeAllSpawnedHolograms();

        // Zapisz aktualny stan danych do pliku
        NPCAPI.saveDataToFile();
        // --- Koniec zmian dla NPCAPI ---

        getLogger().info("FPCode-Survival został wyłączony!");
    }

    public static Main getMain() {
        return mainInstance;
    }

    public RanksAPI getRanksAPI() {
        return ranksApi;
    }

    private void registerNetherPassRecipe() {
        NamespacedKey key = new NamespacedKey(this, "nether_pass");

        ItemStack result = NetherPrzepustka.getNetherPass();

        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("DGD", "GSG", "DGD");

        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('S', Material.NETHER_STAR);

        // 6. Zarejestruj recepturę w Bukkit
        Bukkit.addRecipe(recipe);
    }
    private void registerNetherStarRecipe() {
        NamespacedKey key = new NamespacedKey(this, "nether_star_custom"); // Zmień klucz, aby uniknąć konfliktów

        ItemStack result = NetherPrzepustka.getNetherPass();

        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape("ZZZ", "ZDZ", "ZZZ");

        ItemStack zwoj = Zwoj.getItem();
        recipe.setIngredient('Z', new RecipeChoice.ExactChoice(zwoj));

        // Jeśli 'G' i 'S' mają pozostać jako zwykłe materiały, to zostawiasz je bez zmian:
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);

        // ***** ZMIANY KOŃCZĄ SIĘ TUTAJ *****

        // 3. Zarejestruj recepturę w Bukkit
        Bukkit.addRecipe(recipe);
        getLogger().info("Custom Nether Star recipe registered successfully!");
    }
}