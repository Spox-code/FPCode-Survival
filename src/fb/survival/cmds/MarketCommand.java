package fb.survival.cmds; // Sugerowany pakiet

import fb.core.api.HexAPI; // Zakładam, że masz klasę HexAPI do kolorowania tekstu
import fb.core.api.RanksAPI; // Zakładam, że masz RanksAPI
import fb.survival.api.MarketManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material; // Import Material
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays; // Import Arrays
import java.util.List;

public class MarketCommand implements CommandExecutor, TabExecutor {

    private RanksAPI ranksAPI; // Zmieniamy na pole, aby przekazywać w konstruktorze

    public MarketCommand(RanksAPI ranksAPI) {
        this.ranksAPI = ranksAPI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HexAPI.hex("&cTa komenda jest tylko dla graczy!"));
            return true;
        }

        Player player = (Player) sender;


        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "wystaw":
                if (args.length < 2) {
                    player.sendMessage(HexAPI.hex("&cUżycie: /rynek wystaw <cena>"));
                    return true;
                }
                handleListCommand(player, args);
                break;
            case "otworz":
                MarketManager.openMarketGUI(player, 0); // Otwórz pierwszą stronę GUI
                break;
            default:
                sendHelpMessage(player);
                break;
        }

        return true;
    }

    private void handleListCommand(Player player, String[] args) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(HexAPI.hex("&cNie masz żadnego przedmiotu w ręku, aby go wystawić!"));
            return;
        }

        double price;
        try {
            price = Double.parseDouble(args[1]);
            if (price <= 0) {
                player.sendMessage(HexAPI.hex("&cCena musi być większa od zera!"));
                return;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(HexAPI.hex("&cNiepoprawna cena. Podaj liczbę."));
            return;
        }

        MarketManager.addListing(player, itemInHand.clone(), price); // Przekaż kopię przedmiotu
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(HexAPI.hex("§8--- #0096fc§lRynek§r§8 ---"));
        player.sendMessage(HexAPI.hex("&b/rynek wystaw <cena> &7- Wystawia przedmiot trzymany w ręce na rynek."));
        player.sendMessage(HexAPI.hex("&b/rynek otworz &7- Otwiera GUI rynku z dostępnymi przedmiotami."));
        // Możesz dodać więcej komend w przyszłości, np. /rynek moje, /rynek anuluj <id>
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if(args.length == 1){
            tab.add("otworz");
            tab.add("wystaw");
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("wystaw")){
                tab.add("<cena>");
            }
        }
        return tab;
    }
}