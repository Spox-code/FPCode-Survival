package fb.survival.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class HatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Sprawdź, czy nadawca komendy jest graczem
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Tylko gracze mogą używać tej komendy!");
            return true; // Komenda obsłużona
        }

        Player player = (Player) sender;

        // Pobierz przedmiot, który gracz trzyma w głównej ręce
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // Sprawdź, czy gracz trzyma jakiś przedmiot
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Musisz trzymać przedmiot w ręce, aby założyć go na głowę!");
            return true;
        }

        // Sprawdź, czy przedmiot może być założony jako hełm
        // To jest uproszczone sprawdzenie. Możesz dodać bardziej zaawansowaną logikę,
        // jeśli chcesz zezwolić tylko na konkretne typy przedmiotów (np. tylko zbroje na głowę).
        // Na razie zakładamy, że każdy przedmiot może być "czapką".

        // Pobierz przedmiot, który gracz ma aktualnie na głowie
        ItemStack helmet = player.getInventory().getHelmet();

        // Ustaw przedmiot z ręki na głowę gracza
        player.getInventory().setHelmet(itemInHand);

        // Usuń przedmiot z ręki gracza
        player.getInventory().setItemInMainHand(null);

        // Jeżeli gracz miał coś na głowie, dodaj to z powrotem do ekwipunku
        if (helmet != null && helmet.getType() != Material.AIR) {
            // Spróbuj dodać przedmiot do ekwipunku.
            // Jeśli ekwipunek jest pełny, przedmiot zostanie upuszczony na ziemię.
            player.getInventory().addItem(helmet).values().forEach(remainingItem -> {
                player.getWorld().dropItemNaturally(player.getLocation(), remainingItem);
                player.sendMessage(ChatColor.YELLOW + "Twój poprzedni hełm został upuszczony na ziemię, ponieważ Twój ekwipunek jest pełny!");
            });
        }

        player.sendMessage(ChatColor.GREEN + "Założyłeś przedmiot na głowę!");
        return true; // Komenda obsłużona pomyślnie
    }
}