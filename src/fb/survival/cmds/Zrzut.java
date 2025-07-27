package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.DropManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList; // Dodaj ten import
import java.util.Arrays; // Dodaj ten import
import java.util.HashMap;
import java.util.List; // Dodaj ten import
import java.util.Map;

public class Zrzut implements CommandExecutor {

    static RanksAPI ra;
    // Mapa przechowująca lokalizację skrzynki ze zrzutu
    // oraz mapę bloków, które zostały zniszczone wokół tej skrzynki.
    // Wewnętrzna mapa: Location bloku -> Material bloku przed zniszczeniem.
    public static Map<Location, Map<Location, Material>> zrzutChestsWithDestroyedBlocks = new HashMap<>();
    public static Map<Location, Material> blocksToRestore = new HashMap<>();

    public Zrzut(RanksAPI ra){
        Zrzut.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.zrzut")){
                Location playerLoc = p.getLocation();

                int radius = 4;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Location blockLoc = playerLoc.clone().add(x, y, z);
                            Block block = blockLoc.getBlock();

                            // Zapisujemy bloki, które mogą zostać zniszczone przez eksplozję
                            if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                                blocksToRestore.put(blockLoc.clone(), block.getType());
                            }
                        }
                    }
                }
                p.getWorld().createExplosion(playerLoc, 4.0f, true, true);

                // Znajdujemy blok pod graczem, upewniając się, że jest to stabilne podłoże po eksplozji.
                Block blockBelowPlayer = playerLoc.clone().subtract(0, 1, 0).getBlock();
                if (blockBelowPlayer.getType() == Material.AIR || blockBelowPlayer.getType() == Material.WATER || blockBelowPlayer.getType() == Material.LAVA) {
                    Location tempLoc = playerLoc.clone().subtract(0, 1, 0);
                    while ((tempLoc.getBlock().getType() == Material.AIR || tempLoc.getBlock().getType() == Material.WATER || tempLoc.getBlock().getType() == Material.LAVA) && tempLoc.getY() > p.getWorld().getMinHeight()) {
                        tempLoc.subtract(0, 1, 0);
                    }
                    blockBelowPlayer = tempLoc.add(0, 1,0).getBlock();
                }

                blockBelowPlayer.setType(Material.CHEST);

                // Dodajemy lokalizację skrzynki i mapę zniszczonych bloków
                zrzutChestsWithDestroyedBlocks.put(blockBelowPlayer.getLocation(), blocksToRestore);

                // --- Dodanie hologramu zrzutu ---
                List<String> hologramLines = Arrays.asList(
                        HexAPI.hex("#0096fc&lNOWY ZRZUT!"), // Żółty pogrubiony tekst
                        "&b&lKliknij skrzynkę, aby otworzyć!", // Jasnoniebieski pogrubiony tekst
                        "&7&o(Teren zostanie przywrócony po otwarciu)" // Biały, kursywa, słabszy tekst
                );
                List<String> newhologram = hologramLines.reversed();
                double spacing = 0.25;
                double initialYOffset = 1.0; // Offset Y od bloku (skrzynki)
                DropManager.createDropHologram(blockBelowPlayer.getLocation(), newhologram, spacing, initialYOffset);
                // --- Koniec dodawania hologramu ---

                p.sendMessage(HexAPI.hex("Wykonałeś zrzut! Skrzynka pojawiła się pod Tobą, teren został zniszczony i pojawił się ogień."));

            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.zrzut )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}