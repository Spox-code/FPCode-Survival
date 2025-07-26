package fb.survival.events;

import fb.core.api.HexAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SingChange implements Listener {

    @EventHandler
    public void SignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        String[] lines = event.getLines(); // Pobierz wszystkie linie tabliczki

        // Sprawdź, czy pierwsza linia to "[PaySign]" (bez uwzględniania wielkości liter)
        if (lines[0] != null && ChatColor.stripColor(HexAPI.hex(lines[0])).equalsIgnoreCase("[PaySign]")) {
            double price = 0;
            boolean isPriceValid = false;

            // Spróbuj sparsować liczbę z trzeciej linii
            if (lines[2] != null && !lines[2].isEmpty()) {
                try {
                    price = Double.parseDouble(ChatColor.stripColor(HexAPI.hex(lines[2]))); // Usuń kolory przed parsowaniem
                    isPriceValid = true;
                } catch (NumberFormatException e) {
                    player.sendMessage(HexAPI.hex("§cTrzecia linia musi być poprawną liczbą (ceną)!"));
                    // Opcjonalnie: Zresetuj tekst, aby gracz musiał poprawić
                    event.setCancelled(true); // Anuluj zmianę tabliczki jeśli cena jest niepoprawna
                    return;
                }
            } else {
                player.sendMessage(HexAPI.hex("§cTrzecia linia musi zawierać cenę!"));
                event.setCancelled(true);
                return;
            }

            // Ustaw pierwszą linię na stylowany "[PAY]"
            event.setLine(0, HexAPI.hex("#0096fc&l[ P A Y ]")); // Kolor #0096fc i pogrubienie

            // Ustaw drugą linię jako pustą lub z ozdobnikiem
            event.setLine(1, HexAPI.hex("§7" + player.getName())); // Mały ozdobnik

            // Ustaw trzecią linię na sformatowaną cenę (zawsze dwumiejscową po przecinku)
            event.setLine(2, HexAPI.hex("§fCena: §b" + String.format("%.2f$", price))); // Biały "Cena:", jasnoniebieska cena

            // Ustaw czwartą linię z instrukcją
            event.setLine(3, HexAPI.hex("§ePPM, aby zaplacic!")); // Żółty kolor

            player.sendMessage(HexAPI.hex("§aPomyślnie utworzono tabliczkę płatności!"));
        }
    }
}
