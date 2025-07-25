package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import org.bukkit.Bukkit; // Potrzebne do interakcji z innymi graczami
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue; // Potrzebne do przechowywania danych o graczu
import org.bukkit.plugin.java.JavaPlugin; // Potrzebne do pobrania instancji pluginu

import java.util.HashMap;
import java.util.Map;

public class Vanish implements CommandExecutor {

    private static RanksAPI ra;
    // Zmieniamy na Map<Player, Boolean> i inicjalizujemy ją tutaj.
    // Lepsze jest przechowywanie statusu vanish w metadanych gracza,
    // ale dla uproszczenia na razie zostawimy HashMapę.
    public static Map<Player, Boolean> vanishlist = new HashMap<>();
    private final JavaPlugin plugin; // Dodajemy referencję do głównego pluginu

    public Vanish(RanksAPI ra, JavaPlugin plugin){
        Vanish.ra = ra;
        this.plugin = plugin; // Przypisujemy instancję pluginu
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.vanish")){
                boolean isInVanish = vanishlist.getOrDefault(p, false);

                if(isInVanish){
                    vanishlist.put(p, false);
                    p.sendTitle(HexAPI.hex("#0096fc§lVANISH"), HexAPI.hex("§fJestes teraz #0096fcwidczony"));
                    for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                        onlinePlayer.showPlayer(plugin, p);
                    }
                    p.getLocation().getWorld().strikeLightningEffect(p.getLocation());

                } else {
                    vanishlist.put(p, true);
                    p.sendTitle(HexAPI.hex("#0096fc§lVANISH"), HexAPI.hex("§fJestes teraz #0096fcniewidczony"));

                    for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                        if(!ra.hasPermission(onlinePlayer, "fb.vanish")) {
                            onlinePlayer.hidePlayer(plugin, p);
                        }
                    }
                }
            } else {
                // Gracz nie ma wymaganych uprawnień.
                p.sendMessage("§cNie posiadasz uprawnien ( fb.vanish )");
            }
        } else {
            // Komenda została użyta z konsoli.
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return true; // Zwracamy true, aby poinformować Bukkit, że komenda została poprawnie obsłużona.
    }
}