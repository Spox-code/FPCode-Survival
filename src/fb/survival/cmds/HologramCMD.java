package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.NPCAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.Location; // Dodaj ten import

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HologramCMD implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public HologramCMD(RanksAPI ra){
        HologramCMD.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.hologram")){
                if(args.length >= 2){
                    if(args[0].equalsIgnoreCase("remove")){
                        // Sprawdź, czy chcemy usunąć konkretny hologram, czy wszystkie w promieniu
                        if (args[1].equalsIgnoreCase("nearby")) { // Nowa opcja: /hologram remove nearby <promien>
                            if (args.length < 3) {
                                p.sendMessage("§cUzycie: /hologram remove nearby <promien>");
                                return false;
                            }
                            try{
                                int radius = Integer.parseInt(args[2]);
                                NPCAPI.removeNearbyHolograms(p.getLocation(), radius);
                                p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZostaly usuniete wszystkie hologramy z #0096fc" + radius + " kratek"));
                            } catch (NumberFormatException e) {
                                p.sendMessage("§cMusisz podac liczbe jako promien.");
                            }
                        } else if (args[1].equalsIgnoreCase("atlocation")) { // Nowa opcja: /hologram remove atlocation
                            NPCAPI.removeAndDespawnMultiLineHologram(p.getLocation()); // Spróbuj usunąć wieloliniowy
                            NPCAPI.removeAndDespawnHologram(p.getLocation()); // Spróbuj usunąć pojedynczy
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fProba usuniecia hologramu w Twojej lokalizacji."));
                        } else {
                            // Domyślne zachowanie: usuwanie w promieniu (jak było)
                            try{
                                int amount = Integer.parseInt(args[1]);
                                // Tutaj będziemy usuwać byty, ale dane musimy usunąć inaczej,
                                // bo removeNearbyHolograms nie wie, który konkretny hologram usunąć.
                                // Na razie zostawię jak jest, ale to miejsce do ulepszenia.
                                NPCAPI.removeNearbyHolograms(p.getLocation(), amount);
                                p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZostaly usuniete wszystkie hologramy z #0096fc" + amount + " kratek"));
                            } catch (NumberFormatException e) {
                                p.sendMessage("§cMusisz podac liczbe jako odleglosc.");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        // Tworzenie hologramu Magika z wieloma liniami
                        if (args[1].equalsIgnoreCase("Magic")) {
                            List<String> magicHologramLines = Arrays.asList(
                                    HexAPI.hex("#0096fc§lMAGIK"),
                                    HexAPI.hex("§7§o( Kliknij, aby ulepszyć )"),
                                    HexAPI.hex("§fUlepsz swoje statystyki!"),
                                    HexAPI.hex("§fAby ulepszyc perki musisz posiadac §a$$$")
                            );
                            // Używamy saveAndCreateMultiLineHologram
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), magicHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCMagic"));
                        } else if (args[1].equalsIgnoreCase("Misje")) {
                            List<String> missionHologramLines = Arrays.asList(
                                    HexAPI.hex("#0096fc◄ #0096fc§lSYSTEM MISJI §l&l►"),
                                    HexAPI.hex("§7§o( Kliknij, aby ulepszyć )"),
                                    HexAPI.hex("§b⟪ Kliknij ⟫ §faby przeglądać misje!"),
                                    HexAPI.hex("§fUkończ i zdobądź #0096fcCenne Łupy")
                            );
                            // Używamy saveAndCreateMultiLineHologram
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), missionHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCMisje"));
                        } else if (args[1].equalsIgnoreCase("Kowadlo")) {
                            List<String> missionHologramLines = Arrays.asList(
                                    HexAPI.hex("#0096fc§lKowadlo"),
                                    HexAPI.hex("§7§o( Kliknij, aby naprawic )"),
                                    HexAPI.hex("§fWymagany LVL #0096fc5LVL§f!")
                            );
                            // Używamy saveAndCreateMultiLineHologram
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), missionHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCKowadlo"));
                        } else {
                            // Tworzenie własnego, pojedynczego hologramu
                            StringBuilder customNameBuilder = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                customNameBuilder.append(args[i]).append(" ");
                            }
                            String customName = customNameBuilder.toString().trim().replace("_", " ");
                            if (customName.isEmpty()) {
                                p.sendMessage("§cUzycie /hologram <remove/create> <odleglosc/nazwa>");
                                return false;
                            }
                            // Używamy saveAndCreateHologram dla pojedynczych
                            NPCAPI.saveAndCreateHologram(p.getLocation(), HexAPI.hex(customName), 2.0);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FC" + customName));
                        }
                    } else {
                        p.sendMessage("§cUzycie /hologram <remove/create> <odleglosc/nazwa>");
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) {
                        p.sendMessage("§cUzycie /hologram <remove/create> <nearby/atlocation/odleglosc/nazwa>");
                    } else {
                        p.sendMessage("§cUzycie /hologram <remove/create> <nearby/atlocation/odleglosc/nazwa>");
                    }
                } else {
                    p.sendMessage("§cUzycie /hologram <remove/create> <nearby/atlocation/odleglosc/nazwa>");
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.hologram )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> tab = new ArrayList<>();
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.hologram")){
                if(args.length == 1){
                    tab.add("create");
                    tab.add("remove");
                }else if(args.length == 2){
                    if(args[0].equalsIgnoreCase("remove")){
                        tab.add("nearby"); // Nowa opcja
                        tab.add("atlocation"); // Nowa opcja
                        tab.add("<radius>"); // Sugeruj liczbę dla starej komendy remove
                    } else if (args[0].equalsIgnoreCase("create")) {
                        tab.add("Magic");
                        tab.add("Misje");
                        tab.add("Kowadlo");
                        tab.add("<Twoja_nazwa_hologramu>"); // Sugeruj tworzenie własnego
                    }
                } else if (args.length == 3 && args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("nearby")) {
                    tab.add("<radius>"); // Sugeruj promień dla /hologram remove nearby
                }
            }
        }
        return tab;
    }
}