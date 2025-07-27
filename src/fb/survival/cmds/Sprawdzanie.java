package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.ServerAPI;
import fb.survival.gui.gui.AdminItemsGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sprawdzanie implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public Sprawdzanie(RanksAPI ra){
        Sprawdzanie.ra = ra;
    }
    public static Map<Player, Location> playerlocationbeforeCheck = new HashMap<>();
    public static Map<Player, Player> admincheckplayer = new HashMap<>();
    public static Map<Player, Boolean> playerisCheck = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.sprawdzanie")){
                if(args.length == 2){
                    if(args[0].equalsIgnoreCase("ustaw")){
                        if(args[1].equalsIgnoreCase("sprawdzarka")){
                            ServerAPI.setSprawdzanie(p.getLocation());
                            p.sendTitle(HexAPI.hex("#0096fc§lSPRAWDZANIE"), HexAPI.hex("§fUstawiles lokalizacje #0096fcsprawdzarki!"));
                        } else{
                            sendHelp(p);
                        }
                    }else if(args[0].equalsIgnoreCase("gracz")){
                        Player cel = Bukkit.getPlayerExact(args[1]);
                        if(cel != null){
                            if(!cel.getName().equals(p.getName())){
                                playerlocationbeforeCheck.put(cel, cel.getLocation());
                                admincheckplayer.put(p, cel);
                                playerisCheck.put(cel, true);
                                playerlocationbeforeCheck.put(p, p.getLocation());
                                p.teleport(ServerAPI.getSprawdzanie());
                                cel.teleport(ServerAPI.getSprawdzanie());
                                for(Player ps : Bukkit.getOnlinePlayers()){
                                    ps.sendMessage("");
                                    ps.sendMessage(HexAPI.hex("   #0096fc§lSPRAWDZANIE"));
                                    ps.sendMessage(HexAPI.hex("   §8» §fGracz #0096fc" + cel.getName() + " §fjest sprawdzany"));
                                    ps.sendMessage(HexAPI.hex("   §8» §fPrzez administratora #0096fc" + p.getName()));
                                    ps.sendMessage("");
                                }
                            }else{
                                p.sendMessage("§cNie mozesz sprawdzac samego siebie");
                            }
                        }else{
                            p.sendMessage("§cTen gracz jest OFFLINE");
                        }
                    } else{
                        sendHelp(p);
                    }
                }else if(args.length == 1){
                    Player cel = admincheckplayer.get(p);
                    if(cel != null) {
                        if (args[0].equalsIgnoreCase("czysty")) {
                            cel.teleport(playerlocationbeforeCheck.get(cel));
                            p.teleport(playerlocationbeforeCheck.get(p));
                            playerisCheck.put(cel, false);
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fc§lSPRAWDZANIE"));
                                ps.sendMessage(HexAPI.hex("   §8» §fGracz #0096fc" + cel.getName() + " §fokazal sie"));
                                ps.sendMessage(HexAPI.hex("   §8» #0096fcCzysty"));
                                ps.sendMessage("");
                            }
                            admincheckplayer.put(p, null);
                        }else if (args[0].equalsIgnoreCase("cheater")) {
                            cel.teleport(ServerAPI.getSpawn());
                            cel.damage(10000000);
                            p.teleport(playerlocationbeforeCheck.get(p));
                            playerisCheck.put(cel, false);
                            p.chat("/tempban " + cel.getName() + " 7d Cheaty");
                            admincheckplayer.put(p, null);
                        } else if (args[0].equalsIgnoreCase("bw")) {
                            cel.teleport(ServerAPI.getSpawn());
                            cel.damage(10000000);
                            p.teleport(playerlocationbeforeCheck.get(p));
                            playerisCheck.put(cel, false);
                            p.chat("/tempban " + cel.getName() + " 7d Brak wspopracy");
                            admincheckplayer.put(p, null);
                        }else if (args[0].equalsIgnoreCase("przyznajesie")) {
                            cel.teleport(ServerAPI.getSpawn());
                            cel.damage(10000000);
                            p.teleport(playerlocationbeforeCheck.get(p));
                            playerisCheck.put(cel, false);
                            p.chat("/tempban " + cel.getName() + " 7d Przyznanie sie");
                            admincheckplayer.put(p, null);
                        } else{
                            sendHelp(p);
                        }
                    }else{
                        p.sendMessage("§cGracz sprawdzany jest OFFLINE");
                    }
                } else{
                    sendHelp(p);
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.sprawdzanie )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
    private static void sendHelp(Player p){
        p.sendMessage("");
        p.sendMessage(HexAPI.hex("§8§m--------§r #0096fcSprawdzanie §8§m--------"));
        p.sendMessage("");
        p.sendMessage(HexAPI.hex("#0096fc/spr ustaw sprawdzarka §8- §fUstaw sprawdzarke"));
        p.sendMessage(HexAPI.hex("#0096fc/spr gracz <nick> §8- §fSprawdz gracza"));
        p.sendMessage(HexAPI.hex("#0096fc/spr cheater §8- §fZbanuj gracza za cheaty"));
        p.sendMessage(HexAPI.hex("#0096fc/spr bw §8- §fZbanuj gracza za brak wspopracy"));
        p.sendMessage(HexAPI.hex("#0096fc/spr przyznajesie §8- §fZbanuj gracza za przyznanie sie do cheatow"));
        p.sendMessage(HexAPI.hex("#0096fc/spr czysty §8- §fWypusc gracza jako czystego"));
        p.sendMessage("");
        p.sendMessage(HexAPI.hex("§8§m--------§r #0096fcSprawdzanie §8§m--------"));
        p.sendMessage("");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.sprawdzanie")){
                if(args.length == 1){
                    if(admincheckplayer.get(p) != null){
                        list.add("cheater");
                        list.add("bw");
                        list.add("przyznajesie");
                        list.add("czysty");
                    }else{
                        list.add("ustaw");
                        list.add("gracz");
                    }
                }else if(args.length == 2){
                    if(args[0].equalsIgnoreCase("ustaw")){
                        list.add("sprawdzarka");
                    }else if(args[0].equalsIgnoreCase("gracz")){
                        for(Player ps : Bukkit.getOnlinePlayers()){
                            list.add(ps.getName());
                        }
                    }
                }
            }
        }
        return list;
    }
}
