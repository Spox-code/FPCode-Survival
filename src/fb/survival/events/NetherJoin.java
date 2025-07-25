package fb.survival.events;

import fb.core.api.HexAPI;
import fb.survival.api.ServerAPI; // Zakładam, że ServerAPI jest dostępne i działa poprawnie
import fb.survival.items.NetherPrzepustka;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent; // Upewnij się, że masz ten import
import org.bukkit.inventory.ItemStack;

public class NetherJoin implements Listener {

    @EventHandler
    public void onNetherPortal(PlayerPortalEvent event) { // Zmieniona nazwa metody dla lepszej czytelności
        Player player = event.getPlayer();

        // Sprawdzamy, czy przyczyną teleportacji jest portal do Netheru
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            // 1. Sprawdzamy, czy Nether jest włączony (ServerAPI.isNether())
            if (!ServerAPI.isNether()) {
                event.setCancelled(true); // Anuluj zdarzenie
                player.sendTitle(HexAPI.hex("#0096fc§lNETHER"), HexAPI.hex("§fNether jest aktualnie #0096fcwylaczony."), 10, 70, 20);
                return;
            }

            ItemStack requiredPass = NetherPrzepustka.getNetherPass();

            if (!player.getInventory().containsAtLeast(requiredPass, 1)) {
                event.setCancelled(true);
                player.sendTitle(HexAPI.hex("#0096fc§lNETHER"), HexAPI.hex("§fPotrzebujesz #0096fcPrzepustki Netheru."), 10, 70, 20);
            }

        }
    }
}