package fb.survival;

import fb.core.api.BanAPI;
import fb.core.api.BungeeAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.PlayerAPI;
import fb.survival.cmds.Vanish;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
// Ważne: Zostaw tylko te importy, które są faktycznie używane i potrzebne do poprawnej sygnatury!
import org.bukkit.OfflinePlayer; // Użyj OfflinePlayer jeśli to jest w sygnaturze
// import org.bukkit.entity.Player; // Odkomentuj i użyj, jeśli w sygnaturze jest Player

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable; // Użyj @Nullable tylko jeśli jest w sygnaturze!


public class SurvivalExpansion extends PlaceholderExpansion {

    private final Main plugin;
    private final RanksAPI ranksAPI;

    public SurvivalExpansion(Main plugin, RanksAPI ranksApi) {
        this.plugin = plugin;
        this.ranksAPI = ranksApi;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "fpcode-survival";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xSpox_";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return plugin != null && plugin.isEnabled() && ranksAPI != null;
    }

    // ######################################################################################
    // PONIŻEJ ZMIENIAJ TYLKO TĘ SYGNATURĘ, ABY BYŁA ZGODNA Z PlaceholderAPI.jar
    // WYBIERZ JEDNĄ Z TRZECH OPCJI PONIŻEJ i usuń pozostałe!
    // ######################################################################################

    // OPCJA 1: (Najbardziej prawdopodobna dla nowszych PAPI)
    // @Override
    // public @Nullable String onPlaceholderRequest(@Nullable OfflinePlayer player, @NotNull String identifier) {

    // OPCJA 2: (Prawdopodobna dla wielu wersji PAPI)
    // @Override
    // public @Nullable String onPlaceholderRequest(OfflinePlayer player, @NotNull String identifier) {

    // OPCJA 3: (Dla starszych PAPI lub jeśli brakuje adnotacji)
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // ######################################################################################

        // --- PLACEHOLDERY DLA AKTUALNEGO GRACZA ---
        if (player != null) {
            // %fpcode-survival_money%
            if (identifier.equalsIgnoreCase("money")) {
                double money = Main.getMoney(player.getName());
                return String.format("%.2f", money);
            }

            // %fpcode-survival_kills%
            if (identifier.equalsIgnoreCase("kills")) {
                return String.valueOf(BanAPI.getPlayerStatKills(player.getName()));
            }

            // %fpcode-survival_deaths%
            if (identifier.equalsIgnoreCase("deaths")) {
                return String.valueOf(BanAPI.getPlayerStatDeaths(player.getName()));
            }
            if (identifier.equalsIgnoreCase("online-all")) {
                return String.valueOf(BungeeAPI.getCachedOnlinePlayers("ALL"));
            }
            if (identifier.equalsIgnoreCase("suffix")) {
                String suffix = "";
                if(PlayerAPI.getSkull(player)){
                    suffix = "§f☠";
                }
                if(Vanish.vanishlist.get(player)){
                    suffix = HexAPI.hex("§3[☢]");
                }
                return suffix;
            }
            if (identifier.equalsIgnoreCase("kd")) {
                int kills = BanAPI.getPlayerStatKills(player.getName());
                int deaths = BanAPI.getPlayerStatDeaths(player.getName()); // Poprawione
                float kd = deaths == 0 ? kills : (float) kills / deaths;  // Zabezpieczenie przed dzieleniem przez 0
                return String.format("%.2f", kd); // Zaokrąglenie do 2 miejsc po przecinku (opcjonalnie)
            }

            if (identifier.equalsIgnoreCase("playerscount")) {
                return String.valueOf(BanAPI.getPlayersCountSurvival());
            }
        }

        // --- PLACEHOLDERY DLA INNYCH GRACZY (np. money_GraczXYZ) ---
        if (identifier.startsWith("money_")) {
            String targetPlayerName = identifier.substring("money_".length());
            double money = Main.getMoney(targetPlayerName);
            return String.format("%.2f", money);
        }

        // --- PLACEHOLDERY DLA TOPKI ---
        // Topka zabójstw
        // %fpcode-survival-top-kills-1-name%
        if (identifier.matches("top-kills-(\\d+)-name")) {
            int rank = Integer.parseInt(identifier.split("-")[2]);
            // TUTAJ MUSISZ ZASTĄPIĆ "test" PRAWDZIWĄ METODĄ, NP. Main.getTopKillsPlayerName(rank)
            return String.valueOf(BanAPI.getTopKillsName(rank)); // Placeholder, do zaimplementowania
        }
        // %fpcode-survival-top-kills-1-amount%
        if (identifier.matches("top-kills-(\\d+)-amount")) {
            int rank = Integer.parseInt(identifier.split("-")[2]);
            // TUTAJ MUSISZ ZASTĄPIĆ "0" PRAWDZIWĄ METODĄ, NP. Main.getTopKillsAmount(rank)
            return String.valueOf(BanAPI.getTopKilsAmount(rank)); // Placeholder, do zaimplementowania
        }

        // Topka śmierci
        // %fpcode-survival-top-deaths-1-name%
        if (identifier.matches("top-deaths-(\\d+)-name")) {
            int rank = Integer.parseInt(identifier.split("-")[2]);
            // TUTAJ MUSISZ ZASTĄPIĆ "test2" PRAWDZIWĄ METODĄ, NP. Main.getTopDeathsPlayerName(rank)
            return String.valueOf(BanAPI.getTopDeathsName(rank)); // Placeholder, do zaimplementowania
        }
        // %fpcode-survival-top-deaths-1-amount%
        if (identifier.matches("top-deaths-(\\d+)-amount")) {
            int rank = Integer.parseInt(identifier.split("-")[2]);
            // TUTAJ MUSISZ ZASTĄPIĆ "2" PRAWDZIWĄ METODĄ, NP. Main.getTopDeathsAmount(rank)
            return String.valueOf(BanAPI.getTopDeathsAmount(rank)); // Placeholder, do zaimplementowania
        }
        if (identifier.matches("top-deaths-(\\d+)-full")) {
            int rank = Integer.parseInt(identifier.split("-")[2]);
            String ret = "&7" + rank + ".";
            if(!BanAPI.getTopDeathsName(rank).equals("-")){
                ret = "&7"+rank+". &f" + BanAPI.getTopDeathsName(rank) + " &8[&9" + BanAPI.getTopDeathsAmount(rank) + "&8]";
            }
            return ret;
        }
        if (identifier.matches("top-kills-(\\d+)-full")) {
            int rank = Integer.parseInt(identifier.split("-")[2]);
            String ret = "&7" + rank + ".";
            if(!BanAPI.getTopKillsName(rank).equals("-")){
                ret = "&7"+rank+". &f" + BanAPI.getTopKillsName(rank) + " &8[&9" + BanAPI.getTopKilsAmount(rank) + "&8]";
            }
            return ret;
        }

        // Jeśli żaden z warunków nie został spełniony, zwróć null
        return null;
    }
}