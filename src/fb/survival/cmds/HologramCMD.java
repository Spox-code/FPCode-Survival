package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.NPCAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors; // Dodano import dla Collectors

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
                        if (args[1].equalsIgnoreCase("nearby")) {
                            if (args.length < 3) {
                                p.sendMessage(HexAPI.hex("§cUzycie: /hologram remove nearby <promien>"));
                                return false;
                            }
                            try{
                                int radius = Integer.parseInt(args[2]);
                                NPCAPI.removeNearbyHolograms(p.getLocation(), radius);
                                p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZostaly usuniete wszystkie hologramy w promieniu #0096fc" + radius + " kratek!"));
                            } catch (NumberFormatException e) {
                                p.sendMessage(HexAPI.hex("§cMusisz podac liczbe jako promien."));
                            }
                        } else if (args[1].equalsIgnoreCase("atlocation")) {
                            NPCAPI.removeAndDespawnMultiLineHologram(p.getLocation()); // Spróbuj usunąć wieloliniowy
                            NPCAPI.removeAndDespawnHologram(p.getLocation()); // Spróbuj usunąć pojedynczy
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fProba usuniecia hologramu w Twojej lokalizacji."));
                        } else {
                            // Domyślne zachowanie: usuwanie w promieniu (jak było) - choć lepiej to doprecyzować,
                            // bo obecne removeNearbyHolograms działa po prostu na lokalizacji.
                            // Możesz chcieć usunąć konkretny hologram po ID, jeśli go zapisałeś.
                            // Na potrzeby tego zadania zakładam, że "amount" to nadal promień dla spójności.
                            try{
                                int radius = Integer.parseInt(args[1]);
                                NPCAPI.removeNearbyHolograms(p.getLocation(), radius);
                                p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZostaly usuniete wszystkie hologramy w promieniu #0096fc" + radius + " kratek!"));
                            } catch (NumberFormatException e) {
                                p.sendMessage(HexAPI.hex("§cMusisz podac liczbe jako odleglosc."));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (args[1].equalsIgnoreCase("Magic")) {
                            List<String> magicHologramLines = Arrays.asList(
                                    HexAPI.hex(" §f▛▞▙ #0096fc§lM A G I K #0096fc▜▚▘ "), // Bardziej ozdobny tytuł
                                    HexAPI.hex("§7§o( Kliknij, aby ulepszyć )"),
                                    HexAPI.hex("§fPopraw swoje statystyki §bi perki!"), // Poprawiony tekst
                                    HexAPI.hex("§fZdobywaj przewagę i dominuj!"), // Nowa linia
                                    HexAPI.hex("§fWymaga §a$$$ §fi trochę §eMagii!") // Dodano "trochę Magii"
                            );
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), magicHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCMagika!"));
                        } else if (args[1].equalsIgnoreCase("Misje")) {
                            List<String> missionHologramLines = Arrays.asList(
                                    HexAPI.hex(" §f▰▱ #0096fc§lS Y S T E M §lM I S J I #0096fc▱▰ "), // Bardziej ozdobny tytuł
                                    HexAPI.hex("§7§o( Kliknij, aby odkryć nowe wyzwania )"),
                                    HexAPI.hex("§b⟪ KLIKNIJ ⟫ §faby przeglądać dostępne misje!"),
                                    HexAPI.hex("§fUkończ zadania i zdobądź #0096fcCenne Łupy§f!"), // Dodano wykrzyknik
                                    HexAPI.hex("§eGotowy na przygodę?") // Nowa, zachęcająca linia
                            );
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), missionHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCMisje!"));
                        } else if (args[1].equalsIgnoreCase("Kowadlo")) {
                            List<String> anvilHologramLines = Arrays.asList(
                                    HexAPI.hex(" §f⚒ #0096fc§lK O W A D Ł O #0096fc⚒ "), // Bardziej ozdobny tytuł
                                    HexAPI.hex("§7§o( Kliknij, aby naprawić przedmioty )"),
                                    HexAPI.hex("§fNapraw swoje narzędzia i zbroje!"), // Nowa linia
                                    HexAPI.hex("§fWymagany poziom: #0096fc5 LVL§f!") // Wyróżniony poziom
                            );
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), anvilHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCKowadlo!"));
                        } else if (args[1].equalsIgnoreCase("Rynek")) { // NOWY HOLOGRAM DLA RYNKU
                            List<String> marketHologramLines = Arrays.asList(
                                    HexAPI.hex(" §f❖ #0096fc§lR Y N E K §lP R Z E D M I O T Ó W #0096fc❖ "),
                                    HexAPI.hex("§7§o( Kliknij, aby kupować i sprzedawać )"),
                                    HexAPI.hex("§fZnajdź najlepsze oferty lub wystaw własne przedmioty!"),
                                    HexAPI.hex("§b⟪ KLIKNIJ ⟫ §faby otworzyć rynek!")
                            );
                            // Używamy funkcji, która otworzy GUI rynku po interakcji z NPC
                            // Zakładam, że NPCAPI.saveAndCreateMultiLineHologram ma parametr dla akcji,
                            // lub że masz inny sposób na przypisanie akcji do NPC (np. przez NPC Listener).
                            // Na potrzeby hologramu, samo wyświetlenie wystarczy, ale jeśli chcesz interakcję,
                            // to musiałbyś dodać logikę w NPCAPI lub listenerze, który wywoła
                            // MarketManager.openMarketGUI(player, 0); przy kliknięciu na tego konkretnego NPC/hologram.
                            NPCAPI.saveAndCreateMultiLineHologram(p.getLocation(), marketHologramLines, 0.25, 1);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram #0096FCRynek!"));
                        }
                        else {
                            // Tworzenie własnego, pojedynczego hologramu
                            // Linię tekstową tworzymy z reszty argumentów, używając spacji i zamieniając "_" na spacje.
                            String customName = Arrays.stream(args).skip(1)
                                    .collect(Collectors.joining(" "))
                                    .replace("_", " ");
                            if (customName.isEmpty()) {
                                p.sendMessage(HexAPI.hex("§cUzycie /hologram create <nazwa> lub /hologram create <typ_hologramu>"));
                                return false;
                            }
                            NPCAPI.saveAndCreateHologram(p.getLocation(), HexAPI.hex(customName), 2.0); // Domyślne przesunięcie
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fStworzyles hologram: #0096FC" + customName));
                        }
                    } else {
                        p.sendMessage(HexAPI.hex("§cUzycie: /hologram <remove/create> <nearby/atlocation/radius/type/text>"));
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")) {
                        p.sendMessage(HexAPI.hex("§cUzycie: /hologram <remove/create> <nearby/atlocation/radius/type/text>"));
                    } else {
                        p.sendMessage(HexAPI.hex("§cUzycie: /hologram <remove/create> <nearby/atlocation/radius/type/text>"));
                    }
                } else {
                    p.sendMessage(HexAPI.hex("§cUzycie: /hologram <remove/create> <nearby/atlocation/radius/type/text>"));
                }
            }else{
                p.sendMessage(HexAPI.hex("§cNie posiadasz uprawnien (fb.hologram)"));
            }
        }else{
            sender.sendMessage(HexAPI.hex("§cTa komenda jest tylko dla graczy!"));
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
                        tab.add("nearby");
                        tab.add("atlocation");
                        tab.add("<radius>");
                    } else if (args[0].equalsIgnoreCase("create")) {
                        tab.add("Magic");
                        tab.add("Misje");
                        tab.add("Kowadlo");
                        tab.add("Rynek"); // Nowa opcja dla TabComplete
                        tab.add("<Twoja_nazwa_hologramu>");
                    }
                } else if (args.length == 3 && args[0].equalsIgnoreCase("remove") && args[1].equalsIgnoreCase("nearby")) {
                    tab.add("<radius>");
                }
            }
        }
        return tab;
    }
}