package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.NPCAPI; // Upewnij się, że to jest poprawny import
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType; // Dodaj import dla EntityType

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NPCCMD implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public NPCCMD(RanksAPI ra){
        NPCCMD.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.npc")){
                if(args.length == 2){
                    if(args[0].equalsIgnoreCase("create")){
                        String npcType = args[1]; // Pobierz typ NPC z argumentów
                        // Sprawdź, czy podany typ NPC jest prawidłowym EntityType (opcjonalne, ale zalecane)
                        try {
                            EntityType.valueOf(npcType.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            p.sendMessage(HexAPI.hex("§cNieprawidłowy typ NPC: #0096fc" + npcType + "§c. Dostępne typy to np.: WITCH, VILLAGER, ZOMBIE."));
                            return true;
                        }

                        // Używamy saveAndSpawnNPC z typem NPC
                        NPCAPI.saveAndSpawnNPC(p.getLocation(), npcType.toUpperCase()); // Zapisujemy typ dużymi literami
                        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fPomyślnie stworzono i zapisano NPC typu: #0096fc" + npcType));
                    }else if(args[0].equalsIgnoreCase("remove")){
                        try{
                            int amount = Integer.parseInt(args[1]);
                            int removedCount = 0;
                            List<Location> locationsToRemove = new ArrayList<>();

                            // Iteruj po mapie, aby znaleźć NPC-ów do usunięcia
                            for (java.util.Map.Entry<Location, String> entry : NPCAPI.getSavedNpcData().entrySet()) {
                                Location savedLoc = entry.getKey();
                                if (savedLoc != null && savedLoc.getWorld() != null && savedLoc.isWorldLoaded()) {
                                    if (p.getLocation().distance(savedLoc) <= amount) {
                                        locationsToRemove.add(savedLoc);
                                    }
                                }
                            }

                            for (Location loc : locationsToRemove) {
                                // Usuń NPC-a z danych i ze świata
                                NPCAPI.removeAndDespawnNPC(loc);
                                removedCount++;
                            }

                            // Dodatkowo usuń wszystkie byty typu NPC w promieniu, które mogły nie być śledzone
                            NPCAPI.removeNearbyNPCs(p.getLocation(), amount);

                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZostalo usunietych #0096fc" + removedCount + " §fNPC-ow z #0096fc" + amount + " kratek (oraz wszelkie inne w poblizu)."));
                        } catch (NumberFormatException e) {
                            p.sendMessage("§cMusisz podac liczbe jako odleglosc.");
                        }
                    }else{
                        p.sendMessage("§cUzycie /npc <create/remove> <typNPC/odleglosc>");
                    }
                }else{
                    p.sendMessage("§cUzycie /npc <create/remove> <typNPC/odleglosc>");
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.npc )");
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
            if(ra.hasPermission(p, "fb.npc")){
                if(args.length == 1){
                    tab.add("create");
                    tab.add("remove");
                }else if(args.length == 2){
                    if(args[0].equalsIgnoreCase("remove")){
                        tab.add("<ilosc>"); // Sugestia dla odległości
                    } else if (args[0].equalsIgnoreCase("create")) {
                        // Sugerowanie typów NPC
                        return Arrays.stream(EntityType.values())
                                .filter(type -> type.isAlive() && type != EntityType.PLAYER) // Filtruj tylko żywe byty, nie graczy
                                .map(Enum::name) // Pobierz nazwy enumów (np. WITCH, VILLAGER)
                                .filter(name -> name.startsWith(args[1].toUpperCase())) // Filtruj na podstawie tego, co gracz wpisał
                                .collect(Collectors.toList());
                    }
                }
            }
        }
        return tab;
    }
}