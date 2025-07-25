package fb.survival.api;

import fb.core.Main; // Dodaj import dla Main, aby przekazać go do PlayerData
import fb.core.api.HexAPI;
import fb.survival.data.PlayerData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerAPI {

    // Zmieniamy na niestatyczne pole, aby mieć konkretną instancję PlayerData
    // (choć PlayerData jest singletonem, lepiej mieć referencję do niego)
    static PlayerData pd;

    // Mapa przechowująca czasy odnowienia dla każdego kitu w milisekundach
    private static final Map<String, Long> KIT_COOLDOWNS = new HashMap<>();

    static {
        KIT_COOLDOWNS.put("gracz", TimeUnit.HOURS.toMillis(1));    // 1 godzina
        KIT_COOLDOWNS.put("vip", TimeUnit.HOURS.toMillis(24));     // 24 godziny
        KIT_COOLDOWNS.put("svip", TimeUnit.HOURS.toMillis(24));    // 24 godziny
        KIT_COOLDOWNS.put("mvip", TimeUnit.HOURS.toMillis(24));    // 24 godziny
        KIT_COOLDOWNS.put("elita", TimeUnit.HOURS.toMillis(24));   // 24 godziny
        KIT_COOLDOWNS.put("legenda", TimeUnit.HOURS.toMillis(24)); // 24 godziny
    }

    // Konstruktor powinien przyjmować instancję Main, aby PlayerData mogło być poprawnie zainicjowane
    public PlayerAPI() {
        this.pd = PlayerData.getInstance(); // Poprawne inicjalizowanie PlayerData z instancją pluginu
    }

    public static void setup(Player p){
        // Używamy instancji 'pd' zamiast statycznego dostępu do PlayerData
        if(pd.getData().getString(p.getUniqueId().toString() + ".nick") == null) {
            pd.getData().set(p.getUniqueId().toString() + ".nick", p.getName());
            pd.getData().set(p.getUniqueId().toString() + ".skull", false);
            pd.getData().set(p.getUniqueId().toString() + ".perki.deaths", 0);
            pd.saveData(); // Zapisujemy zmiany od razu po ustawieniu nicku
        }
    }
    public static int getPerk(Player p, String perk){
        return pd.getData().getInt(p.getUniqueId() + ".perki." + perk);
    }
    public static void setPerk(Player p, String perk, int amount){
        pd.getData().set(p.getUniqueId() + ".perki." + perk, amount);
        pd.saveData();
    }
    public static boolean getSkull(Player p){
        return pd.getData().getBoolean(p.getUniqueId() + ".skull");
    }
    public static void setSkull(Player p, boolean status){
        pd.getData().set(p.getUniqueId() + ".skull", status);
        pd.saveData();
    }

    /**
     * Sprawdza, czy gracz może odebrać określony zestaw (kit).
     * @param player Gracz, dla którego sprawdzamy.
     * @param kitName Nazwa kitu (np. "vip", "gracz").
     * @return True, jeśli gracz może odebrać kit; false w przeciwnym razie.
     */
    public static boolean canClaimKit(Player player, String kitName) { // Zmieniono na niestatyczne
        if (!KIT_COOLDOWNS.containsKey(kitName.toLowerCase())) {
            player.sendMessage("§cNieznany zestaw (kit): " + kitName);
            return false;
        }

        long lastClaimTime = pd.getKitLastClaimTime(player.getUniqueId(), kitName.toLowerCase());
        long requiredCooldown = KIT_COOLDOWNS.get(kitName.toLowerCase());
        long currentTime = System.currentTimeMillis();

        if (lastClaimTime == 0L) {
            return true;
        }

        return (currentTime - lastClaimTime) >= requiredCooldown;
    }

    /**
     * Odbiera zestaw (kit) dla gracza i zapisuje czas odbioru.
     * @param player Gracz, który odbiera kit.
     * @param kitName Nazwa kitu.
     * @return True, jeśli kit został pomyślnie odebrany i czas zapisany; false w przeciwnym razie (np. na cooldownie).
     */
    public static boolean claimKit(Player player, String kitName) { // Zmieniono na niestatyczne
        String lowerCaseKitName = kitName.toLowerCase();
        if (!canClaimKit(player, lowerCaseKitName)) {
            return false;
        }

        // --- TUTAJ DODAJESZ LOGIKĘ DAWANIA ITEMÓW GRACZOWI ---
        switch (lowerCaseKitName){
            case "gracz":
                player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, 1));
                player.getInventory().addItem(new ItemStack(Material.BREAD, 16));
                break;
            case "vip":
                player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
                player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
                player.getInventory().addItem(new ItemStack(Material.IRON_SHOVEL));
                player.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
                player.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
                player.getInventory().addItem(new ItemStack(Material.BREAD, 64));
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 4));
                break;
            case "svip":
                ItemStack sword = new ItemStack(Material.IRON_SWORD);
                ItemMeta swordMeta = sword.getItemMeta();
                if (swordMeta != null) {
                    swordMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    swordMeta.addEnchant(Enchantment.SHARPNESS, 2, true);  // Ostrość II (przykład)
                    sword.setItemMeta(swordMeta);
                }

                ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
                ItemMeta pickaxeMeta = pickaxe.getItemMeta();
                if (pickaxeMeta != null) {
                    pickaxeMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    pickaxeMeta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Wydajność III (przykład)
                    pickaxe.setItemMeta(pickaxeMeta);
                }

                ItemStack axe = new ItemStack(Material.IRON_AXE);
                ItemMeta axeMeta = axe.getItemMeta();
                if (axeMeta != null) {
                    axeMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    axeMeta.addEnchant(Enchantment.EFFICIENCY, 2, true); // Wydajność II (przykład)
                    axe.setItemMeta(axeMeta);
                }

                ItemStack shovel = new ItemStack(Material.IRON_SHOVEL);
                ItemMeta shovelMeta = shovel.getItemMeta();
                if (shovelMeta != null) {
                    shovelMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    shovelMeta.addEnchant(Enchantment.EFFICIENCY, 2, true); // Wydajność II (przykład)
                    shovel.setItemMeta(shovelMeta);
                }

                ItemStack helmet = new ItemStack(Material.IRON_HELMET);
                ItemMeta helmetMeta = helmet.getItemMeta();
                if (helmetMeta != null) {
                    helmetMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    helmet.setItemMeta(helmetMeta);
                }

                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                ItemMeta chestplateMeta = chestplate.getItemMeta();
                if (chestplateMeta != null) {
                    chestplateMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    chestplateMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    chestplate.setItemMeta(chestplateMeta);
                }

                ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
                ItemMeta leggingsMeta = leggings.getItemMeta();
                if (leggingsMeta != null) {
                    leggingsMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    leggingsMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    leggings.setItemMeta(leggingsMeta);
                }

                ItemStack boots = new ItemStack(Material.IRON_BOOTS);
                ItemMeta bootsMeta = boots.getItemMeta();
                if (bootsMeta != null) {
                    bootsMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    bootsMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    bootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 2, true); // Amortyzacja II (przykład)
                    boots.setItemMeta(bootsMeta);
                }


                ItemStack bread = new ItemStack(Material.BREAD, 64);
                ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 16);
                player.getInventory().addItem(sword);
                player.getInventory().addItem(pickaxe);
                player.getInventory().addItem(axe);
                player.getInventory().addItem(shovel);
                player.getInventory().addItem(helmet);
                player.getInventory().addItem(chestplate);
                player.getInventory().addItem(leggings);
                player.getInventory().addItem(boots);
                player.getInventory().addItem(bread);
                player.getInventory().addItem(goldenApple);
                break;
            case "mvip":
                ItemStack msword = new ItemStack(Material.DIAMOND_SWORD);
                ItemStack mpickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
                ItemStack maxe = new ItemStack(Material.DIAMOND_AXE);
                ItemStack mshovel = new ItemStack(Material.DIAMOND_SHOVEL);
                ItemStack mhelmet = new ItemStack(Material.DIAMOND_HELMET);
                ItemStack mchestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemStack mleggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemStack mboots = new ItemStack(Material.DIAMOND_BOOTS);
                ItemStack mbread = new ItemStack(Material.BREAD, 64);
                ItemStack mgoldenApple = new ItemStack(Material.GOLDEN_APPLE, 32);

                player.getInventory().addItem(msword);
                player.getInventory().addItem(mpickaxe);
                player.getInventory().addItem(maxe);
                player.getInventory().addItem(mshovel);
                player.getInventory().addItem(mhelmet);
                player.getInventory().addItem(mchestplate);
                player.getInventory().addItem(mleggings);
                player.getInventory().addItem(mboots);
                player.getInventory().addItem(mbread);
                player.getInventory().addItem(mgoldenApple);
                break;
            case "elita":
                ItemStack esword = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta eswordMeta = esword.getItemMeta();
                if (eswordMeta != null) {
                    eswordMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    eswordMeta.addEnchant(Enchantment.SHARPNESS, 2, true);  // Ostrość II (przykład)
                    esword.setItemMeta(eswordMeta);
                }

                ItemStack epickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
                ItemMeta epickaxeMeta = epickaxe.getItemMeta();
                if (epickaxeMeta != null) {
                    epickaxeMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    epickaxeMeta.addEnchant(Enchantment.EFFICIENCY, 3, true); // Wydajność III (przykład)
                    epickaxe.setItemMeta(epickaxeMeta);
                }

                ItemStack eaxe = new ItemStack(Material.DIAMOND_AXE);
                ItemMeta eaxeMeta = eaxe.getItemMeta();
                if (eaxeMeta != null) {
                    eaxeMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    eaxeMeta.addEnchant(Enchantment.EFFICIENCY, 2, true); // Wydajność II (przykład)
                    eaxe.setItemMeta(eaxeMeta);
                }

                ItemStack eshovel = new ItemStack(Material.DIAMOND_SHOVEL);
                ItemMeta eshovelMeta = eshovel.getItemMeta();
                if (eshovelMeta != null) {
                    eshovelMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    eshovelMeta.addEnchant(Enchantment.EFFICIENCY, 2, true); // Wydajność II (przykład)
                    eshovel.setItemMeta(eshovelMeta);
                }

                ItemStack ehelmet = new ItemStack(Material.DIAMOND_HELMET);
                ItemMeta ehelmetMeta = ehelmet.getItemMeta();
                if (ehelmetMeta != null) {
                    ehelmetMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    ehelmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    ehelmet.setItemMeta(ehelmetMeta);
                }

                ItemStack echestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemMeta echestplateMeta = echestplate.getItemMeta();
                if (echestplateMeta != null) {
                    echestplateMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    echestplateMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    echestplate.setItemMeta(echestplateMeta);
                }

                ItemStack eleggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemMeta eleggingsMeta = eleggings.getItemMeta();
                if (eleggingsMeta != null) {
                    eleggingsMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    eleggingsMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    eleggings.setItemMeta(eleggingsMeta);
                }

                ItemStack eboots = new ItemStack(Material.DIAMOND_BOOTS);
                ItemMeta ebootsMeta = eboots.getItemMeta();
                if (ebootsMeta != null) {
                    ebootsMeta.addEnchant(Enchantment.PROTECTION, 2, true); // Ochrona II
                    ebootsMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Niezniszczalność III
                    ebootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 2, true); // Amortyzacja II (przykład)
                    eboots.setItemMeta(ebootsMeta);
                }


                ItemStack ebread = new ItemStack(Material.BREAD, 64);
                ItemStack egoldenApple = new ItemStack(Material.GOLDEN_APPLE, 48);

                player.getInventory().addItem(esword);
                player.getInventory().addItem(epickaxe);
                player.getInventory().addItem(eaxe);
                player.getInventory().addItem(eshovel);
                player.getInventory().addItem(ehelmet);
                player.getInventory().addItem(echestplate);
                player.getInventory().addItem(eleggings);
                player.getInventory().addItem(eboots);
                player.getInventory().addItem(ebread);
                player.getInventory().addItem(egoldenApple);
                break;
            case "legenda":
                ItemStack lsword = new ItemStack(Material.NETHERITE_SWORD);
                ItemStack lpickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
                ItemStack laxe = new ItemStack(Material.NETHERITE_AXE);
                ItemStack lshovel = new ItemStack(Material.NETHERITE_SHOVEL);
                ItemStack lhelmet = new ItemStack(Material.NETHERITE_HELMET);
                ItemStack lchestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
                ItemStack lleggings = new ItemStack(Material.NETHERITE_LEGGINGS);
                ItemStack lboots = new ItemStack(Material.NETHERITE_BOOTS);
                ItemStack lbread = new ItemStack(Material.BREAD, 64);
                ItemStack lgoldenApple = new ItemStack(Material.GOLDEN_APPLE, 64);

                player.getInventory().addItem(lsword);
                player.getInventory().addItem(lpickaxe);
                player.getInventory().addItem(laxe);
                player.getInventory().addItem(lshovel);
                player.getInventory().addItem(lhelmet);
                player.getInventory().addItem(lchestplate);
                player.getInventory().addItem(lleggings);
                player.getInventory().addItem(lboots);
                player.getInventory().addItem(lbread);
                player.getInventory().addItem(lgoldenApple);
                break;
        }
        player.closeInventory();
        player.sendTitle(HexAPI.hex("#0096FC§lZestawy"), HexAPI.hex("§fOdebrales zestaw #0096fc" + lowerCaseKitName));

        // Zapisujemy czas ostatniego odebrania kitu w PlayerData
        pd.setKitLastClaimTime(player.getUniqueId(), lowerCaseKitName, System.currentTimeMillis());
        // Metoda setKitLastClaimTime w PlayerData już wywołuje saveData()

        return true;
    }

    /**
     * Zwraca pozostały czas do odbioru kitu dla gracza w milisekundach.
     * @param player Gracz.
     * @param kitName Nazwa kitu.
     * @return Pozostały czas w milisekundach, 0L jeśli kit jest dostępny. Zwraca -1L jeśli kit nie istnieje.
     */
    public long getRemainingCooldown(Player player, String kitName) { // Zmieniono na niestatyczne
        String lowerCaseKitName = kitName.toLowerCase();
        if (!KIT_COOLDOWNS.containsKey(lowerCaseKitName)) {
            return -1L; // Nieznany kit
        }

        long lastClaimTime = this.pd.getKitLastClaimTime(player.getUniqueId(), lowerCaseKitName);
        long requiredCooldown = KIT_COOLDOWNS.get(lowerCaseKitName);
        long currentTime = System.currentTimeMillis();

        if (lastClaimTime == 0L) {
            return 0L; // Nigdy nie odebrano, więc jest dostępny
        }

        long timeElapsed = currentTime - lastClaimTime;
        if (timeElapsed >= requiredCooldown) {
            return 0L; // Cooldown minął
        } else {
            return requiredCooldown - timeElapsed; // Pozostały czas
        }
    }

    /**
     * Formatuje czas w milisekundach na czytelny format (np. 1d 5h 30m).
     * @param millis Czas w milisekundach.
     * @return Sformatowany String.
     */
    public String formatTime(long millis) { // Zmieniono na niestatyczne
        if (millis <= 0) return "Dostępny!";

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long days = TimeUnit.MILLISECONDS.toDays(millis);

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days).append("d ");
        }
        // Używamy modulo (%) aby wyświetlać tylko "resztę" godzin/minut/sekund po odjęciu większych jednostek
        if (hours % 24 > 0) { // Zmieniono hours na hours % 24
            sb.append(hours % 24).append("h ");
        }
        if (minutes % 60 > 0) { // Zmieniono minutes na minutes % 60
            sb.append(minutes % 60).append("m ");
        }
        if (seconds % 60 > 0 || sb.length() == 0) { // Zmieniono seconds na seconds % 60, dodano warunek dla 0s
            sb.append(seconds % 60).append("s");
        }

        String formatted = sb.toString().trim();
        return formatted.isEmpty() ? "1s" : formatted; // Upewnij się, że nie zwracamy pustego stringa
    }
}